package com.ems.api.dto.request;

import lombok.Data;
import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingOrder {
    private Sort.Direction direction = Sort.Direction.ASC;
    private String property;
    private Boolean ignoreCase = true;
    private Sort.NullHandling nullHandling = Sort.NullHandling.NATIVE;
}
