package com.ems.api.service.impl;

import com.ems.api.entity.Contact;
import com.ems.api.entity.Event;
import com.ems.api.entity.Post;
import com.ems.api.entity.PostComment;
import com.ems.api.dto.request.*;
import com.ems.api.dto.response.PostCommentResponse;
import com.ems.api.dto.response.PublicEventPostResponse;
import com.ems.api.dto.response.SearchPostResponse;
import com.ems.api.repository.ContactRepo;
import com.ems.api.repository.EventRepo;
import com.ems.api.repository.PostCommentRepo;
import com.ems.api.repository.PostRepo;
import com.ems.api.service.PostService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = AppException.class)
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostCommentRepo postCommentRepo;

    @Override
    public Post getById(Long id, Constant.TypeConstant.PostType postType) {
        return postRepo.findByIdAndPostType(id, postType).orElse(null);
    }

    @Override
    public PublicEventPostResponse getPublicById(Long id) {
        //increase view
        Post post = postRepo.findByIdAndPostType(id, Constant.TypeConstant.PostType.EVENT).orElse(null);
        if (post == null)
            return null;
        else {
            post.setViews(post.getViews() + 1);
            return modelMapper.map(postRepo.save(post), PublicEventPostResponse.class);
        }
    }

    @Override
    public PostCommentResponse createComment(PostCommentRequest input, String creator) throws AppException {
        Contact user = contactRepo.findByAccountEmail(creator);
        PostComment comment = modelMapper.map(input, PostComment.class);
        comment.setCreatorId(user.getId());
        return modelMapper.map(postCommentRepo.save(comment), PostCommentResponse.class);
    }

    @Override
    public Post deletePostTeam(Long postId) throws AppException {
        Post post = postRepo.getOne(postId);
        if(!Constant.TypeConstant.PostType.TEAM.equals(post.getPostType())){
            throw new AppException(Constant.ErrorCode.POST_IS_NOT_POST_TEAM.name());
        }
        postRepo.delete(post);
        return post;
    }

    @Override
    public PostCommentResponse updateComment(PostCommentRequest input, String creator) {
        PostComment comment = postCommentRepo.getOne(input.getId());
        comment.setContent(input.getContent());
        return modelMapper.map(postCommentRepo.save(comment), PostCommentResponse.class);
    }

    @Override
    public PostCommentResponse deleteComment(Long cmtId) {
        PostComment comment = postCommentRepo.getOne(cmtId);
        postCommentRepo.delete(comment);
        return modelMapper.map(comment, PostCommentResponse.class);
    }

    @Override
    public Post likePost(Long id, String userEmail) {
        Post post = postRepo.getOne(id);
        Contact userContact = contactRepo.findByAccountEmail(userEmail);
        String likes = post.getLikes();
        if(likes.isEmpty()){
            likes=userContact.getId().toString();
            post.setLikes(likes);
        }else {
            Set<String> setLike = new HashSet<>(Arrays.asList(likes.split(",")));
            if(setLike.contains(userContact.getId().toString())){
                setLike.remove(userContact.getId().toString());
            }else{
                setLike.add(userContact.getId().toString());
            }
            post.setLikes(setLike.stream().collect(Collectors.joining(",")));
        }
        return postRepo.save(post);
    }

    @Override
    public Post create(CreatePostRequest input, String creator, Constant.TypeConstant.PostType postType) throws AppException {
        Contact currentUserContact = contactRepo.findByAccountEmail(creator);
        Post newPost = new Post();
        newPost.setId(null);
        if (Constant.TypeConstant.PostType.EVENT.equals(postType)) {
            newPost.setTeam(null);
            Optional<Event> existedEvent = eventRepo.findById(((CreatePostEventRequest) input).getEventId());
            if (!existedEvent.isPresent())
                throw new AppException(Constant.ErrorCode.EVENT_NOT_EXIST.name());
//            newPost.setEvent(existedEvent.get());
            newPost.setEventId(existedEvent.get().getId());
        }
        if (Constant.TypeConstant.PostType.TEAM.equals(postType)) {
            newPost.setEvent(null);
            Optional<Contact> existedTeam = contactRepo.findById(((CreatePostTeamRequest) input).getTeamId());
            if (!existedTeam.isPresent())
                throw new AppException(Constant.ErrorCode.TEAM_ID_NOT_EXIST.name());
            if (Constant.TypeConstant.ContactType.TEAM.name().equals(existedTeam.get().getContactType()))
                throw new AppException(Constant.ErrorCode.CONTACT_TYPE_IS_NOT_TEAM.name());
//            newPost.setTeam(existedTeam.get());
            newPost.setTeamId(existedTeam.get().getId());
        }
        newPost.setCreatorId(currentUserContact.getId());
        newPost.setSubject(input.getSubject());
        newPost.setContent(input.getContent());
        newPost.setPostType(postType);
        newPost.setOverviewDescription(input.getOverviewDescription());
        newPost.setOverviewImagePath(input.getOverviewImagePath());
        newPost.setStatus(Constant.TypeConstant.PostStatus.ACTIVE);
        return postRepo.save(newPost);
    }

    @Override
    public Post update(UpdatePostRequest post) throws AppException {
        Post currentPost = postRepo.findById(post.getId()).orElse(null);
        if (currentPost == null)
            throw new AppException(Constant.ErrorCode.POST_ID_NOT_EXIST.name());
        currentPost.setContent(post.getContent());
        currentPost.setSubject(post.getSubject());
        currentPost.setOverviewDescription(post.getOverviewDescription());
        currentPost.setOverviewImagePath(post.getOverviewImagePath());
        currentPost.setStatus(post.getStatus());
        return postRepo.save(currentPost);
    }

    @Override
    public SearchPostResponse<Post> searchPostManage(SearchPostRequest request, String userEmail) throws AppException {
        SearchPostResponse<Post> searchPostResponse = new SearchPostResponse();
        Contact currentUserContact = contactRepo.findByAccountEmail(userEmail);
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Post> postPage = postRepo.search(request.getSearchKey(),
                request.getEventId(), request.getTeamId(), request.getPostType(),
                pageableRequested);
        List<Post> foundPosts = postPage.getContent().stream().map(p->{
            p.setNoComment(postCommentRepo.countPostCommentByPostId(p.getId()));
            p.setIsLiked(p.getLikes().contains(currentUserContact.getId().toString()));
            p.setNoLike(p.getLikes().isEmpty() ? 0 : p.getLikes().split(",").length);
            return p;
        }).collect(Collectors.toList());
        searchPostResponse.setPostPage(new PageImpl<>(foundPosts, pageableRequested, postPage.getTotalElements()));
        return searchPostResponse;
    }

    @Override
    public SearchPostResponse<PublicEventPostResponse> searchPublic(SearchPublicPostRequest request) throws AppException {
        SearchPostResponse<PublicEventPostResponse> searchPostResponse = new SearchPostResponse();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Post> postPage = postRepo.searchPublic(request.getSearchKey(),
                Constant.TypeConstant.PostType.EVENT,
                Constant.TypeConstant.PostStatus.ACTIVE,
                request.getEventId(),
                pageableRequested);
        List<PublicEventPostResponse> foundPosts = postPage.getContent()
                .stream().map(
                        p -> modelMapper.map(p, PublicEventPostResponse.class)
                ).collect(Collectors.toList());
        searchPostResponse.setPostPage(new PageImpl<>(foundPosts, pageableRequested, postPage.getTotalElements()));
        return searchPostResponse;
    }

    @Override
    public SearchPostResponse<PostCommentResponse> findComments(Long id, PagingRequest request) throws AppException {
        SearchPostResponse<PostCommentResponse> searchPostResponse = new SearchPostResponse();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<PostComment> commentPage = postCommentRepo.findByPostId(id, pageableRequested);
        List<PostCommentResponse> foundComments = commentPage.getContent()
                .stream().map(
                        p -> modelMapper.map(p, PostCommentResponse.class)
                ).collect(Collectors.toList());
        searchPostResponse.setPostPage(new PageImpl<>(foundComments, pageableRequested, commentPage.getTotalElements()));
        return searchPostResponse;
    }
}
