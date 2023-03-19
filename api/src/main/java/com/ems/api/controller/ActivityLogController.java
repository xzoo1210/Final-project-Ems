package com.ems.api.controller;

import com.ems.api.dto.response.ActivityLogResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.service.ActivityLogService;
import com.ems.api.util.Constant;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.ACTIVITY_LOG)
public class ActivityLogController {
    protected final Logger LOGGER = LoggerFactory.getLogger(ActivityLogController.class);
    @Autowired
    private ActivityLogService activityLogService;

    @RequestMapping(value = {"/task/{taskId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<ActivityLogResponse>>> loadTaskActivities(@PathVariable Long taskId) {
        String methodName = "loadTaskActivities";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String creatorEmail =
                SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(activityLogService.findByType(Constant.TypeConstant.ActivityType.TASK, taskId));

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/event/{eventId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<ActivityLogResponse>>> loadEventActivities(@PathVariable Long eventId) {
        String methodName = "loadEventActivities";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String creatorEmail =
                SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(activityLogService.findByType(Constant.TypeConstant.ActivityType.EVENT, eventId));

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
