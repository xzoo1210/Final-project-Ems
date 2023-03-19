package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothUpdateRequest extends BoothRequest {
    private Long id;
    private Constant.TypeConstant.BoothStatus status;
}
