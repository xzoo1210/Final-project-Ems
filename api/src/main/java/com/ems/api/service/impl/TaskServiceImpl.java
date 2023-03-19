package com.ems.api.service.impl;

import com.ems.api.dto.request.SearchTaskRequest;
import com.ems.api.dto.request.SendMailRequest;
import com.ems.api.dto.request.TaskRequest;
import com.ems.api.dto.request.UpdateTaskRequest;
import com.ems.api.dto.response.*;
import com.ems.api.entity.*;
import com.ems.api.repository.ContactRepo;
import com.ems.api.repository.EventRepo;
import com.ems.api.repository.TaskRepo;
import com.ems.api.service.ActivityLogDetailService;
import com.ems.api.service.ActivityLogService;
import com.ems.api.service.EmailService;
import com.ems.api.service.TaskService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.DateUtil;
import com.ems.api.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {AppException.class, Exception.class})
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ActivityLogService activityLogService;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ActivityLogDetailService activityLogDetailService;
    @Autowired
    private EmailService emailService;

    @Override
    public SearchTaskResponse search(SearchTaskRequest request, String currentUserEmail) throws AppException {
        SearchTaskResponse<TaskShortResponse> response = new SearchTaskResponse();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        Long assigneeId = request.getAssigneeId();//!=null? request.getAssigneeId(): currentUserContact.getId();
        Page<Task> foundPageTasks;
        if (request.getEventId() != null || request.getTeamId() != null || request.getAssigneeId() != null) {
            foundPageTasks = taskRepo.search(request.getSearchKey(),
                    request.getEventId(), request.getTeamId(), assigneeId,
                    request.getStatus(),
                    PageUtil.createPageRequest(request));
        } else {
            foundPageTasks = taskRepo.findAllByAssigneeId(currentUserContact.getId(),
                    request.getSearchKey(), request.getStatus(), PageUtil.createPageRequest(request));
        }
        Task currentTask = request.getCurrentTaskId() == null?null:taskRepo.getOne(request.getCurrentTaskId());
        Set<Long> childrenTaskId  = currentTask==null?Collections.emptySet():currentTask.getChildrenTask().stream().map(c -> c.getId()).collect(Collectors.toSet());
        List<TaskShortResponse> foundTasks = foundPageTasks.getContent().stream()
                .filter(t -> {
                    if (request.getCurrentTaskId() == null) {
                        return true;
                    } else {
                        if (t.getId().equals(request.getCurrentTaskId())) {
                            return false;
                        } else {
                            if (childrenTaskId.isEmpty()) {
                                return true;
                            } else {
                                return !childrenTaskId.contains(t.getId());
                            }
                        }
                    }
                })
                .map(t -> {
                    TaskShortResponse taskShortResponse = modelMapper.map(t, TaskShortResponse.class);
                    taskShortResponse.setAssignee(modelMapper.map(t.getAssignee(), ContactShortResponse.class));
                    taskShortResponse.setAssigner(modelMapper.map(t.getAssigner(), ContactShortResponse.class));
                    taskShortResponse.setEvent(modelMapper.map(t.getEvent(), EventResponse.class));
                    return taskShortResponse;
                }).collect(Collectors.toList());

        response.setTaskPage(new PageImpl<>(foundTasks, pageableRequested, foundPageTasks.getTotalElements()));
        return response;
    }

    @Override
    public Task create(TaskRequest taskRequest, String currentUserEmail) throws AppException {
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        Event event = eventRepo.getOne(taskRequest.getEventId());
        Task newTask = modelMapper.map(taskRequest, Task.class);
        newTask.setProgress(0);
        newTask.setAssignerId(currentUserContact.getId());
        sendMailAssignedTask(newTask, event.getName(), event.getCode(), currentUserContact.getFirstName());
//        sendMail(newTask.getAssigneeId(), "Bạn được giao một công việc trên EMS", "Truy cập link http://103.75.186.211/task");
        newTask = taskRepo.save(newTask);
        newTask.setCode(event.getCode().toUpperCase() + "_" + newTask.getId());
        return taskRepo.save(newTask);
    }

    private void sendMailAssignedTask(Task task, String eventName, String eventCode, String assignerName) throws AppException {
        Contact assignee = contactRepo.getOne(task.getAssigneeId());
        Map<String, Object> rawData = new HashMap<>();
        rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, assignee.getFirstName());
        rawData.put(Constant.EmailVariable.TASK_NAME.value, task.getName());
        rawData.put(Constant.EmailVariable.EVENT_NAME.value, eventName);
        rawData.put(Constant.EmailVariable.EVENT_CODE.value, eventCode);
        rawData.put(Constant.EmailVariable.ASSIGNER_NAME.value, assignerName);
        rawData.put(Constant.EmailVariable.START_DATE.value, DateUtil.formatDate(task.getStartDate()));
        rawData.put(Constant.EmailVariable.END_DATE.value, DateUtil.formatDate(task.getEndDate()));
        rawData.put(Constant.EmailVariable.PROGRESS.value, task.getProgress());
        rawData.put(Constant.EmailVariable.TASK_DESCRIPTION.value, task.getDescription());
        SendMailRequest sendMailRequest = SendMailRequest.builder()
                .to(Collections.singletonList(assignee.getEmailAccount()))
                .emailType(Constant.EmailType.TASK_ASSIGNED)
                .rawData(rawData)
                .build();
        emailService.composeEmail(Collections.singletonList(sendMailRequest));
    }

    @Override
    public TaskDetailResponse update(UpdateTaskRequest updateRequest, String currentUserEmail) throws AppException {
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        Optional<Task> optCurrentTask = taskRepo.findById(updateRequest.getId());
        if (!optCurrentTask.isPresent()) {
            throw new AppException(Constant.ErrorCode.TASK_ID_NOT_EXIST.name());
        }
        Task currentTask = optCurrentTask.get();
        Contact currentAssigneeContact = contactRepo.getOne(currentTask.getAssigneeId());
        Event event = eventRepo.getOne(currentTask.getEventId());
//        if (!currentUserContactId.equals(currentTask.getAssignerId())
//                && !currentUserContactId.equals(currentTask.getAssigneeId()))
//            throw new AppException(Constant.ErrorCode.CURRENT_USER_IS_NOT_ASSIGNER_OR_ASSIGNEE.name());
        Map<String,Object> rawData = new HashMap<>();

        rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, currentAssigneeContact.getFirstName());
        rawData.put(Constant.EmailVariable.USERNAME.value, currentUserContact.getFirstName());
        rawData.put(Constant.EmailVariable.TASK_NAME.value, currentTask.getName());
        rawData.put(Constant.EmailVariable.EVENT_CODE.value, event.getCode());
        rawData.put(Constant.EmailVariable.EVENT_NAME.value, event.getName());
        ActivityLog activityLogTask = activityLogService.create(new ActivityLog(currentUserContact.getId(),
                Constant.TypeConstant.ActivityType.TASK, updateRequest.getId(), updateRequest.getNote()));
        List<ActivityLogDetail> activityLogDetails = new ArrayList<>();
        if (!Objects.equals(currentTask.getName(), updateRequest.getName())) {
            rawData.put(Constant.EmailVariable.TASK_NAME.value, currentTask.getName());
            rawData.put(Constant.EmailVariable.NEW_TASK_NAME.value, updateRequest.getName());
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "name", currentTask.getName(), updateRequest.getName()));
            currentTask.setName(updateRequest.getName());
        }
        if (!Objects.equals(currentTask.getStartDate().getTime(), updateRequest.getStartDate().getTime())) {

            rawData.put(Constant.EmailVariable.START_DATE.value, DateUtil.formatDate(currentTask.getStartDate()));
            rawData.put(Constant.EmailVariable.NEW_START_DATE.value, DateUtil.formatDate(updateRequest.getStartDate()));
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "startDate", String.valueOf(currentTask.getStartDate().getTime()), String.valueOf(updateRequest.getStartDate().getTime())));
            currentTask.setStartDate(updateRequest.getStartDate());
        }
        if (!Objects.equals(currentTask.getEndDate().getTime(), updateRequest.getEndDate().getTime())) {

            rawData.put(Constant.EmailVariable.START_DATE.value, DateUtil.formatDate(currentTask.getEndDate()));
            rawData.put(Constant.EmailVariable.NEW_START_DATE.value, DateUtil.formatDate(updateRequest.getEndDate()));
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "endDate", String.valueOf(currentTask.getEndDate().getTime()), String.valueOf(updateRequest.getEndDate().getTime())));
            currentTask.setEndDate(updateRequest.getEndDate());
        }
        if (!Objects.equals(currentTask.getParentTaskId(), updateRequest.getParentTaskId())) {
            Task oldParentTask = currentTask.getParentTask();
            String oldParentCode = oldParentTask != null ? oldParentTask.getCode() : "";
            String newParentCode = updateRequest.getParentTaskId() != null ? taskRepo.getOne(updateRequest.getParentTaskId()).getCode() : "";

            rawData.put(Constant.EmailVariable.PARENT_TASK_NAME.value,oldParentCode);
            rawData.put(Constant.EmailVariable.NEW_START_DATE.value, newParentCode);
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "parentTaskCode", oldParentCode, newParentCode));
            currentTask.setParentTaskId(updateRequest.getParentTaskId());
        }
        if (!Objects.equals(currentTask.getDescription(), updateRequest.getDescription())) {

            rawData.put(Constant.EmailVariable.TASK_DESCRIPTION.value,currentTask.getDescription());
            rawData.put(Constant.EmailVariable.NEW_TASK_DESCRIPTION.value, updateRequest.getDescription());
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "description", currentTask.getDescription(), updateRequest.getDescription()));
            currentTask.setDescription(updateRequest.getDescription());
        }
        if (!Objects.equals(currentTask.getProgress(), updateRequest.getProgress())) {
            rawData.put(Constant.EmailVariable.PROGRESS.value,currentTask.getProgress());
            rawData.put(Constant.EmailVariable.NEW_PROGRESS.value, updateRequest.getProgress());
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "progress", String.valueOf(currentTask.getProgress()), String.valueOf(updateRequest.getProgress())));
            currentTask.setProgress(updateRequest.getProgress());
        }
        if (!Objects.equals(currentTask.getStatus(), updateRequest.getStatus())) {
            rawData.put(Constant.EmailVariable.STATUS.value,currentTask.getStatus());
            rawData.put(Constant.EmailVariable.NEW_STATUS.value, updateRequest.getStatus());
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "status", currentTask.getStatus().name(), updateRequest.getStatus().name()));
            currentTask.setStatus(updateRequest.getStatus());
        }
        if (!Objects.equals(currentTask.getAssigneeId(), updateRequest.getAssigneeId())) {
            Contact newAssigneeContact = contactRepo.getOne(updateRequest.getAssigneeId());

            rawData.put(Constant.EmailVariable.ASSIGNEE_NAME.value,currentAssigneeContact.getFirstName());
            rawData.put(Constant.EmailVariable.NEW_ASSIGNEE_NAME.value, newAssigneeContact.getFirstName());
            activityLogDetails.add(new ActivityLogDetail(activityLogTask.getId(), "assigneeName", currentAssigneeContact.getFirstName(), newAssigneeContact.getFirstName()));
            currentTask.setAssigneeId(updateRequest.getAssigneeId());
            sendMailAssignedTask(currentTask, event.getName(), event.getCode(), currentUserContact.getFirstName());
        }
        SendMailRequest sendMailRequest = SendMailRequest.builder()
                .to(Collections.singletonList(currentAssigneeContact.getEmailAccount()))
                .emailType(Constant.EmailType.TASK_UPDATED)
                .rawData(rawData).build();
        emailService.composeEmail(Collections.singletonList(sendMailRequest));
        activityLogDetailService.saveDetails(activityLogDetails);
        return modelMapper.map(taskRepo.save(currentTask), TaskDetailResponse.class);
    }

    @Override
    public TaskDetailResponse getById(Long taskId) {
        Task task = taskRepo.getOne(taskId);
        TaskDetailResponse tdr = modelMapper.map(task, TaskDetailResponse.class);
        tdr.setDescription(task.getDescription());
        return tdr;
    }

    @Override
    public TaskShortResponse getShortTaskById(Long taskId) throws AppException {
        Task task = taskRepo.getOne(taskId);
        return modelMapper.map(task, TaskShortResponse.class);
    }
}
