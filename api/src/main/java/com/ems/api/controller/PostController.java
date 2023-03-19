package com.ems.api.controller;

import com.ems.api.entity.Post;
import com.ems.api.dto.request.*;
import com.ems.api.dto.response.*;
import com.ems.api.service.EventContactService;
import com.ems.api.service.PostService;
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
@RequestMapping(Constant.ControllerMapping.POST)
public class PostController {
    protected final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private PostService postService;
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value = "create-post-event", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> create(@RequestBody CreatePostEventRequest input) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), userEmail, Constant.TypeConstant.EventFeature.POST,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(postService.create(input, userEmail, Constant.TypeConstant.PostType.EVENT));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "create-post-team", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> create(HttpServletRequest request, @RequestBody CreatePostTeamRequest input) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(postService.create(input, creator, Constant.TypeConstant.PostType.TEAM));
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
    public ResponseEntity<MessagesResponse<Post>> updatePostEvent(HttpServletRequest request, @RequestBody UpdatePostRequest input) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), userEmail, Constant.TypeConstant.EventFeature.POST,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(postService.update(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "update-post-team", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> updatePostTeam(HttpServletRequest request, @RequestBody UpdatePostRequest input) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(postService.update(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "delete-post-team/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> deletePostTeam(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {

            messagesResponse.setData(postService.deletePostTeam(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchPostResponse<PublicEventPostResponse>>> searchPublic(HttpServletRequest request, @RequestBody SearchPublicPostRequest input) {
        String methodName = "searchPublic";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(postService.searchPublic(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "search-post-manage", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchPostResponse>> searchPostManage(HttpServletRequest request, @RequestBody SearchPostRequest input) {
        String methodName = "searchPostManage";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
                String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            if(Constant.TypeConstant.PostType.EVENT.equals(input.getPostType())){
                eventContactService.checkAuthorInEvent(input.getEventId(), userEmail, Constant.TypeConstant.EventFeature.POST,
                        Constant.TypeConstant.EventMemberAccess.VIEW);
            }
            messagesResponse.setData(postService.searchPostManage(input,userEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchPostResponse<PostCommentResponse>>> findComments(HttpServletRequest request,
                                                                                          @PathVariable Long id,
                                                                                          @RequestBody PagingRequest input
    ) {
        String methodName = "findComments";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            messagesResponse.setData(postService.findComments(id, input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "event/{id}", method = RequestMethod.GET)
    public ResponseEntity<MessagesResponse<PublicEventPostResponse>> getPublicPostEventById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getPublicPostEventById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(postService.getPublicById(id));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "event/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> getPostEventById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getPostEventById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(postService.getById(id, Constant.TypeConstant.PostType.EVENT));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "team/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> getPostTeamById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getPostTeamById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(postService.getById(id, Constant.TypeConstant.PostType.TEAM));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "team/{id}/like", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Post>> likePost(@PathVariable Long id) {
        String methodName = "likePost";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(postService.likePost(id, userEmail));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = "create-comment", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<PostCommentResponse>> createComment(@RequestBody PostCommentRequest input) {
        String methodName = "createComment";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(postService.createComment(input, creator));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "delete-comment/{cmtId}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<PostCommentResponse>> deleteComment(HttpServletRequest request, @PathVariable Long cmtId) {
        String methodName = "deleteComment";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(postService.deleteComment(cmtId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "update-comment", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<PostCommentResponse>> updateComment(@RequestBody PostCommentRequest input) {
        String methodName = "updateComment";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(postService.updateComment(input, creator));
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
