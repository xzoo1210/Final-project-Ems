package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPostRequest extends PagingRequest {
    private String searchKey;
    private Constant.TypeConstant.PostType postType;
    private Long eventId;
    private Long teamId;
}
