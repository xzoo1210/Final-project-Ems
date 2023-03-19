package com.ems.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDetailResponse extends TaskShortResponse {
    private String description;
    private List<TaskShortResponse> childrenTask;
    private TaskShortResponse parentTask;
}
