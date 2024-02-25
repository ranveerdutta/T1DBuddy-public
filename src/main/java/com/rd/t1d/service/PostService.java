package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.Post;
import com.rd.t1d.data.entity.node.Response;
import com.rd.t1d.data.entity.node.Tag;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.PostHeader;
import com.rd.t1d.data.entity.relations.Reaction;
import com.rd.t1d.data.repository.*;
import com.rd.t1d.dto.PostDetails;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private CustomPostRepository customPostRepository;

    @Transactional
    public void createTopic(String email, Tag tag){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        Tag existingTag = tagRepository.findByTopicCode(tag.getTopic().toLowerCase());
        if(existingTag != null){
            throw new T1DBuddyException(ErrorCode.TAG_ALREADY_AVAILABLE, HttpStatus.BAD_REQUEST);
        }
        createNewTagObject(user, tag);
    }

    public void createNewTagObject(User user, Tag tag){
        tag.setTopicCode(tag.getTopic().toLowerCase());
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        tag.setCreatedAt(currentDateTime);
        tag.setUpdatedAt(currentDateTime);
        tagRepository.save(tag);
        userRepository.createTag(user.getId(), tag.getId());
    }

    @Transactional
    public void createPost(String email, Post post){
        if(null == post){
            throw new T1DBuddyException(ErrorCode.INVALID_POST_CONTENT);
        }
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        post.setCreatedAt(currentDateTime);
        post.setUpdatedAt(currentDateTime);

        List<Tag> outputTags = createTagEntityList(user, post.getTags());
        post.setTags(outputTags);
        postRepository.save(post);
        postRepository.createUserPost(user.getId(), post.getId());

        log.info("Created post id:" + post.getId() + " for the user: " + user.getEmail());
    }

    public List<Tag> createTagEntityList(User user, List<Tag> tagList){
        List<Tag> outputTags = new LinkedList<>();
        for(Tag tag : tagList){
            Tag existingTag = tagRepository.findByTopicCode(tag.getTopic().toLowerCase());
            if(existingTag != null){
                outputTags.add(existingTag);
            }else{
                createNewTagObject(user, tag);
                outputTags.add(tag);
            }
        }
        return outputTags;
    }

    @Transactional
    public void createPostResponse(String email, Long parentPostId, Response response){
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        Post parentPost = postRepository.findPostById(parentPostId);

        if(null == parentPost){
            log.error("hash key of parent response is invalid");
            throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        response.setCreatedAt(currentDateTime);
        response.setUpdatedAt(currentDateTime);

        responseRepository.save(response);
        responseRepository.createPostResponse(parentPost.getId(), response.getId());

        responseRepository.createRespondedBy(user.getId(), response.getId());

        log.info("Created post response id:" + response.getId() + " for the user: " + user.getEmail());
    }


    @Transactional
    public void addReaction(String email, Long postId, Long responseId, Reaction reaction){
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        if(null == reaction || null == reaction.getReactionType()){
            log.error("no value passed for reaction");
            throw new T1DBuddyException(ErrorCode.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
        }

        reaction.setReactedAt(currentDateTime);

        if(postId != null){
            Post post = postRepository.findPostById(postId);
            if(null == post){
                throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            List<Post> likedPost = postRepository.findPostReactedByUser(user.getId(), post.getId(), reaction.getReactionType());
            if(likedPost != null && likedPost.size() > 0){
                throw new T1DBuddyException(ErrorCode.ALREADY_LIKED);
            }

            postRepository.createPostReaction(user.getId(), post.getId(), reaction.getReactedAt(), reaction.getReactionType());
            log.info("Created reaction for post id:" + post.getId() + " by the user: " + user.getEmail());

        }else if(responseId != null){
            Response response = responseRepository.findResponseById(responseId);
            if(null == response){
                throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            List<Response> likedResponse = responseRepository.findResponseReactedByUser(user.getId(), response.getId(), reaction.getReactionType());
            if(likedResponse != null && likedResponse.size() > 0){
                throw new T1DBuddyException(ErrorCode.ALREADY_LIKED);
            }

            responseRepository.createResponseReaction(user.getId(), response.getId(), reaction.getReactedAt(), reaction.getReactionType());
            log.info("Created reaction for response id:" + response.getId() + " by the user: " + user.getEmail());
        }


    }

    @Transactional
    public void deleteReaction(String email, Long postId, Long responseId, Reaction reaction){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        if(null == reaction || null == reaction.getReactionType()){
            log.error("no value passed for reaction");
            throw new T1DBuddyException(ErrorCode.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
        }


        if(postId != null){
            Post post = postRepository.findPostById(postId);
            if(null == post){
                throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            List<Post> likedPost = postRepository.findPostReactedByUser(user.getId(), post.getId(), reaction.getReactionType());
            if(null == likedPost || likedPost.size() == 0){
                throw new T1DBuddyException(ErrorCode.NO_REACTION_FOUND);
            }

            postRepository.deletePostReaction(user.getId(), post.getId(), reaction.getReactionType());
            log.info("Deleted reaction for post id:" + post.getId() + " by the user: " + user.getEmail());
        }else if(responseId != null){
            Response response = responseRepository.findResponseById(responseId);
            if(null == response){
                throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            List<Response> likedResponse = responseRepository.findResponseReactedByUser(user.getId(), response.getId(), reaction.getReactionType());
            if(null == likedResponse || likedResponse.size() == 0){
                throw new T1DBuddyException(ErrorCode.NO_REACTION_FOUND);
            }

            responseRepository.deleteResponseReaction(user.getId(), response.getId(), reaction.getReactedAt(), reaction.getReactionType());
            log.info("Deleted reaction for response id:" + response.getId() + " by the user: " + user.getEmail());
        }


    }

    @Transactional
    public void bookmarkPost(String email, Long postId){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }


        Post post = postRepository.findPostById(postId);
        if(null == post){
            throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        List<Post> bookmarkedPost = postRepository.findPostBookmarkedByUser(user.getId(), post.getId());
        if(bookmarkedPost != null && bookmarkedPost.size() > 0){
            throw new T1DBuddyException(ErrorCode.ALREADY_BOOKMARKED);
        }

        postRepository.bookmarkPost(user.getId(), post.getId());

        log.info("Bookmarked post id:" + post.getId() + " by the user: " + user.getEmail());

    }

    @Transactional
    public void deleteBookmark(String email, Long postId){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }


        Post post = postRepository.findPostById(postId);
        if(null == post){
            throw new T1DBuddyException(ErrorCode.POST_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        List<Post> bookmarkedPost = postRepository.findPostBookmarkedByUser(user.getId(), post.getId());
        if(null == bookmarkedPost || bookmarkedPost.size() == 0){
            throw new T1DBuddyException(ErrorCode.NOT_BOOKMARKED);
        }

        postRepository.deleteBookmark(user.getId(), post.getId());

        log.info("Delete bookmarking post id:" + post.getId() + " by the user: " + user.getEmail());

    }

    @Transactional
    public void addTopicsOfInterest(String email, List<Tag> tagList){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        //below method internally creates TAG_CREATED relation(BAD CODING PRACTICE)
        List<Tag> outputTagList = createTagEntityList(user, tagList);

        user.setTagsInterested(outputTagList);

        userRepository.save(user);
    }

    @Transactional
    public List<PostHeader> fetchPostHeaderList(String email, ZonedDateTime createdAt, int batchSize, String filter){
        if(null == createdAt) createdAt = DateUtils.getCurrentZonedDateTime();
        if("all".equalsIgnoreCase(filter)){
            return customPostRepository.findAllPostHeader(email, createdAt, batchSize);
        }else if("bookmarked".equalsIgnoreCase(filter)){
            return customPostRepository.findBookmarkedPostHeader(email, createdAt, batchSize);
        }else{
            throw new T1DBuddyException(ErrorCode.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public PostDetails fetchPostDetails(String email, Long postId){
        Optional<PostHeader> postHeaderOptional = customPostRepository.findPostHeaderById(email, postId);

        if(postHeaderOptional.isEmpty()) throw new T1DBuddyException(ErrorCode.INVALID_POST_CONTENT);

        PostDetails postDetails = new PostDetails();
        postDetails.setPostHeader(postHeaderOptional.get());
        postDetails.setPostResponseList(customPostRepository.findAllPostResponse(email, postHeaderOptional.get().getPostId()));
        return postDetails;
    }


}


