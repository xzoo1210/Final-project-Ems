package com.ems.api.controller;

import com.ems.api.dto.response.FileResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.DateUtil;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Date;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.FILE)
public class FileController {
    private final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private Environment ev;
    @Value("${export.qr.path}")
    private String exportQrPath;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<FileResponse>> upload(HttpServletRequest request,
                                                                 @RequestParam("file") MultipartFile file) {
        String methodName = ".upload";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String[] fileName = file.getOriginalFilename().split("\\.");

            File dirUserUpload = new File(ev.getProperty("upload.userfile.path") + DateUtil.formatNow(DateUtil.DATE_FORMAT_DIR));
            if (!dirUserUpload.exists()) {
                if (!dirUserUpload.mkdirs()) {
                    throw new AppException(Constant.ErrorCode.CANNOT_CREATE_FOLDER.name());
                }
            }
            long countElementInDir = dirUserUpload.list() != null ? dirUserUpload.list().length : 0;


            String fileNameUnique = fileName[0] + "_" + countElementInDir + "_" + new Date().getTime() + "." + fileName[1];
            File serverFile = new File(dirUserUpload, fileNameUnique);
            FileResponse fr = new FileResponse();
            try (FileOutputStream fos = new FileOutputStream(serverFile);
                 BufferedOutputStream stream = new BufferedOutputStream(fos)) {
                stream.write(file.getBytes());
                fr.setPathFile(serverFile.getPath());
                fr.setFilename(file.getName());
                fr.setOriginalFilename(file.getOriginalFilename());
                fr.setContentType(file.getContentType());
                fr.setBytes(file.getBytes());
            } catch (Exception e) {
                throw new AppException(e, Constant.ErrorCode.SYSTEM_ERROR.name());
            }
            messagesResponse.setData(fr);
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-file", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(@RequestParam("path") String path) {
        String methodName = ".getFile";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        byte[] returnFileBytes = new byte[0];
        try {
            File serverFile = new File(path);
            try {
                returnFileBytes = Files.readAllBytes(serverFile.toPath());
            } catch (Exception e) {
                throw new AppException(e, "hi");
            }
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(returnFileBytes, HttpStatus.NO_CONTENT);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(returnFileBytes, HttpStatus.OK);
    }
    @RequestMapping(value = "/get-qr/{path}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getQr(@PathVariable String path) {
        String methodName = ".getFile";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        byte[] returnFileBytes = new byte[0];
        try {
            File serverFile = new File(exportQrPath+path);
            try {
                returnFileBytes = Files.readAllBytes(serverFile.toPath());
            } catch (Exception e) {
                throw new AppException(e, Constant.ErrorCode.SYSTEM_ERROR.name());
            }
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(returnFileBytes, HttpStatus.NO_CONTENT);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + path);
        return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_PNG).body(returnFileBytes);
//        return new ResponseEntity<>(returnFileBytes, HttpStatus.OK);
    }
}
