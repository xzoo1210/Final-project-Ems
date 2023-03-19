package com.ems.api.controller;


import com.ems.api.entity.Task;
import com.ems.api.dto.request.SearchTaskRequest;
import com.ems.api.dto.request.TaskRequest;
import com.ems.api.dto.request.UpdateTaskRequest;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.TaskDetailResponse;
import com.ems.api.dto.response.TaskShortResponse;
import com.ems.api.service.TaskService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.TASK)
public class TaskController {
    protected final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private TaskService taskService;

    @RequestMapping(value = {"search"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<Task>>> search(HttpServletRequest request, @RequestBody SearchTaskRequest searchTaskRequest) {
        String methodName = "search";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(taskService.search(searchTaskRequest, currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"create"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Task>> create(@RequestBody TaskRequest taskRequest) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Task task = taskService.create(taskRequest, currentUserEmail);
            messagesResponse.setData(task);
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"update"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Task>> update(@RequestBody UpdateTaskRequest updateTaskRequest) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(taskService.update(updateTaskRequest, currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<TaskDetailResponse>> getById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(taskService.getById(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"short/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<TaskShortResponse>> getShortTaskById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getShortTaskById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(taskService.getShortTaskById(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
