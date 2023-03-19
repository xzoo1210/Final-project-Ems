package com.ems.api.controller;

import com.ems.api.entity.Contact;
import com.ems.api.entity.TeamContact;
import com.ems.api.dto.request.SearchUserToAddToTeam;
import com.ems.api.dto.request.TeamRequest;
import com.ems.api.dto.request.TeamUpdateRequest;
import com.ems.api.dto.response.*;
import com.ems.api.service.ContactService;
import com.ems.api.service.EventContactService;
import com.ems.api.service.TeamService;
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
import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.TEAM)
public class TeamController {
    protected final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);
    @Autowired
    private ContactService contactService;
    @Autowired
    private EventContactService eventContactService;
    @Autowired
    private TeamService teamService;

    @RequestMapping(value = {"{teamId}/add-user/{userToAddId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<TeamContact>> addUser(HttpServletRequest request,
                                                                 @PathVariable Long userToAddId,
                                                                 @PathVariable Long teamId) {
        String methodName = "addUser";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(contactService.addOtherUserToTeam(userToAddId, teamId, currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"{teamId}/remove-user/{userToRemoveId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<TeamContact>> removeUser(HttpServletRequest request,
                                                                 @PathVariable Long userToRemoveId,
                                                                 @PathVariable Long teamId) {
        String methodName = "removeUser";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(contactService.removeUserFromTeam(userToRemoveId, teamId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"{teamId}/grant-leader-role/{userId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<TeamContact>> grantLeaderRole(HttpServletRequest request,
                                                                 @PathVariable Long userId,
                                                                 @PathVariable Long teamId) {
        String methodName = "grantLeaderRole";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(contactService.grantLeaderRole(userId, teamId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Contact>> create(HttpServletRequest request, @RequestBody TeamRequest input) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.ORGANIZATION, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(contactService.createTeam(input, creator));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Contact>> update(HttpServletRequest request, @RequestBody TeamUpdateRequest input) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.ORGANIZATION, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(contactService.updateTeam(input));
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
    public ResponseEntity<MessagesResponse<TeamResponse>> getById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        messagesResponse.setData(contactService.getTeamById(id, currentUser));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "find-all-by-event/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<TeamShortResponse>>> findAllByEventId(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "findAllByEventId";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(contactService.findAllByEventId(id));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "find-by-event/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<TeamShortResponse>>> findByEventId(@PathVariable Long id) {
        String methodName = "findByEventId";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String creator = SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(contactService.findByEventId(id, creator));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"/search-user-to-add"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<ContactShortResponse>>> searchUserToAdd(HttpServletRequest request,
                                                                                        @RequestBody SearchUserToAddToTeam input) {
        String methodName = "searchUserToAdd";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(contactService.searchUserToAddToTeam(input,currentUser));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"{teamId}/load-all-member"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<TeamContactResponse>>> loadAllMember(HttpServletRequest request,
                                                                                      @PathVariable Long teamId) {
        String methodName = "loadAllMember";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String currentUserEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();

            messagesResponse.setData(teamService.loadAllMember(teamId,currentUserEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
