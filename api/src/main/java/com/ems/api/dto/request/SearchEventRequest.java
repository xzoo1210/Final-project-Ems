package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchEventRequest extends PagingRequest {
    private String searchKey;
    private Constant.TypeConstant.EventStatus status;
}
