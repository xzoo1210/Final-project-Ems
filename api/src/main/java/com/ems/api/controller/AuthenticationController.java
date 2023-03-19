package com.ems.api.controller;

import com.ems.api.dto.request.SendMailRequest;
import com.ems.api.entity.Account;
import com.ems.api.dto.request.ChangePasswordRequest;
import com.ems.api.dto.request.LoginRequest;
import com.ems.api.dto.request.RegisterRequest;
import com.ems.api.dto.response.LoginResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.service.EmailService;
import com.ems.api.service.impl.AuthenticationServiceImpl;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.JwtUtil;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(Constant.ControllerMapping.AUTHENTICATION)
public class AuthenticationController {
    protected final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private AuthenticationServiceImpl authenticationService;
    @Value("${server.api}")
    private String domainApi;
    @Autowired
    private EmailService emailService;
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<LoginResponse>> login(HttpServletRequest request, @RequestBody LoginRequest loginReqest) {
        String methodName = "login";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();

        try {
            messagesResponse.setData(authenticationService.login(loginReqest));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Account>> register(HttpServletRequest request, @RequestBody RegisterRequest registerRequest) {
        String methodName = "register";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();

        try {
            messagesResponse.setData(authenticationService.register(registerRequest));
            String token = JwtUtil.createToken(registerRequest.getEmail());
            SendMailRequest sendMailRequest = new SendMailRequest();
            sendMailRequest.setTo(Collections.singletonList(registerRequest.getEmail()));
            sendMailRequest.setEmailType(Constant.EmailType.ACTIVATE_REGISTER);
            Map<String, Object> rawData = new HashMap<>();
            rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, registerRequest.getCreateContactRequest().getFirstName());
            rawData.put(Constant.EmailVariable.ACTIVATE_ACCOUNT.value,
                    domainApi+ Constant.ControllerMapping.AUTHENTICATION +"/active-account/"+token);
            rawData.put(Constant.EmailVariable.LOGO_URL.value, "https://res.cloudinary.com/dcgjui0yd/image/upload/v1671031958/logo_ixfins.png");
            sendMailRequest.setRawData(rawData);
            emailService.composeEmail(Collections.singletonList(sendMailRequest));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Account>> changePassword( @RequestBody ChangePasswordRequest request) {
        String methodName = "changePassword";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();

        try {
            messagesResponse.setData(authenticationService.changePassword(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/active-account/{token}", method = RequestMethod.GET)
    public void activeAccount(HttpServletResponse response, @PathVariable String token) {
        String methodName = "activeAccount";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        try {
            String link = authenticationService.activeAccount(token);
            response.sendRedirect(link);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
    }
}
