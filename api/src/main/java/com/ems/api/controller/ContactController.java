package com.ems.api.controller;

import com.ems.api.entity.Contact;
import com.ems.api.entity.UserContact;
import com.ems.api.dto.request.CreateContactRequest;
import com.ems.api.dto.request.SearchContactRequest;
import com.ems.api.dto.request.UpdateContactRequest;
import com.ems.api.dto.request.UpdateProfileRequest;
import com.ems.api.dto.response.ContactShortResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.SearchContactResponse;
import com.ems.api.service.ContactService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.CONTACT)
public class ContactController {
    protected final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private ContactService contactService;


//    @RequestMapping(value = "create", method = RequestMethod.POST)
//    public ResponseEntity<MessagesResponse<Contact>> create(HttpServletRequest request, @RequestBody CreateContactRequest input) {
//        String methodName = "create";
//        long startTime = System.currentTimeMillis();
//        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
//        MessagesResponse messagesResponse = new MessagesResponse();
//        try {
//            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
//            messagesResponse.setData(contactService.createContact(input, creator));
//        } catch (AppException ex) {
//            LOGGER.error(ex.getMessage(), ex);
//            messagesResponse.setMessages(ex.getMessage());
//            messagesResponse.setErrorCode(ex.getCode());
//            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
//        }
//        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
//        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "update", method = RequestMethod.POST)
//    public ResponseEntity<MessagesResponse<Contact>> update(HttpServletRequest request, @RequestBody UpdateContactRequest input) {
//        String methodName = "updateContact";
//        long startTime = System.currentTimeMillis();
//        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
//        MessagesResponse messagesResponse = new MessagesResponse();
//        try {
//            messagesResponse.setData(contactService.updateContact(input));
//        } catch (AppException ex) {
//            LOGGER.error(ex.getMessage(), ex);
//            messagesResponse.setMessages(ex.getMessage());
//            messagesResponse.setErrorCode(ex.getCode());
//            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
//        }
//        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
//        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
//    }

    @RequestMapping(value = "/remove-other-user/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Contact>> removeOtherUser(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "removeOtherUser";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            contactService.removeOtherUser(id, user);
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchContactResponse<Contact>>> searchContact(@RequestBody SearchContactRequest searchContactRequest) {
        String methodName = "searchContact";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(contactService.searchContact(searchContactRequest, currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/load-profile"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Contact>> loadProfile(HttpServletRequest request) {
        String methodName = "loadProfileByAccountEmail";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(contactService.loadProfileByAccountEmail(currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/add-other-user/{otherUserContactId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<UserContact>> addOtherUser(HttpServletRequest request, @PathVariable Long otherUserContactId) {
        String methodName = "addOtherUser";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(contactService.addOtherUser(otherUserContactId, currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"/update-profile"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Contact>> updateProfile(HttpServletRequest request, @RequestBody UpdateProfileRequest updateProfileRequest) {
        String methodName = "updateProfile";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(contactService.updateProfile(updateProfileRequest));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Contact>> getById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(contactService.getById(id));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "short/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<ContactShortResponse>> getShortContactById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getShortContactById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(contactService.getShortContactById(id));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

}
