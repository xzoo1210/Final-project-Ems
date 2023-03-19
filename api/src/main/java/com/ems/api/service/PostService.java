package com.ems.api.service;

import com.ems.api.entity.Post;
import com.ems.api.dto.request.*;
import com.ems.api.dto.response.PostCommentResponse;
import com.ems.api.dto.response.PublicEventPostResponse;
import com.ems.api.dto.response.SearchPostResponse;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;

public interface PostService {
    Post create(CreatePostRequest input, String creator, Constant.TypeConstant.PostType postType) throws AppException;

    Post update(UpdatePostRequest post) throws AppException;

    SearchPostResponse<Post> searchPostManage(SearchPostRequest input, String userEmail) throws AppException;

    SearchPostResponse<PublicEventPostResponse> searchPublic(SearchPublicPostRequest input) throws AppException;

    SearchPostResponse<PostCommentResponse> findComments(Long id, PagingRequest request) throws AppException;

    Post getById(Long id, Constant.TypeConstant.PostType postType);

    PublicEventPostResponse getPublicById(Long id);

    PostCommentResponse createComment(PostCommentRequest input, String creator) throws AppException;

    Post deletePostTeam(Long postId) throws AppException;

    PostCommentResponse updateComment(PostCommentRequest input, String creator)throws AppException;

    PostCommentResponse deleteComment(Long cmtId) throws AppException;

    Post likePost(Long id, String userEmail);
}
