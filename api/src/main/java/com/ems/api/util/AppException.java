package com.ems.api.util;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends Exception {
    private String code;
    private String customMessage = null;

    public AppException(Exception e, String code) {
        super(e);
        this.code = code;
    }

    public AppException(String code) {
        this.code = code;
    }

    public AppException(String code, String customMessage) {
        this.code = code;
        this.customMessage = customMessage;
    }

    @Override
    public String getMessage() {
        return customMessage != null ? customMessage : super.getMessage();
    }
}
