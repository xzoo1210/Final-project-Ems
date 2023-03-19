package com.ems.api.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponse<T> {
    private Page<T> page;
}
