package com.ems.api.dto.request;

import lombok.Data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRequest {
    protected List<PagingOrder> orders;
    protected int page;
    protected int size;
}
