package com.ems.api.service;

import com.ems.api.entity.Task;
import com.ems.api.dto.request.SearchTaskRequest;
import com.ems.api.dto.request.TaskRequest;
import com.ems.api.dto.request.UpdateTaskRequest;
import com.ems.api.dto.response.SearchTaskResponse;
import com.ems.api.dto.response.TaskDetailResponse;
import com.ems.api.dto.response.TaskShortResponse;
import com.ems.api.util.AppException;

public interface TaskService {
    SearchTaskResponse search(SearchTaskRequest searchTaskRequest, String currentUserEmail) throws AppException;

    Task create(TaskRequest taskRequest, String currentUserEmail) throws AppException;

    TaskDetailResponse update(UpdateTaskRequest updateTaskRequest, String currentUserEmail) throws AppException;

    TaskDetailResponse getById(Long taskId) throws AppException;

    TaskShortResponse getShortTaskById(Long taskId) throws AppException;
}
