package com.ems.api.dto.response;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {
    private String pathFile;
    private String filename;
    private String originalFilename;
    private String contentType;
    private byte[] bytes;
}
