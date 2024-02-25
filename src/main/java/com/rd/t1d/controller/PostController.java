package com.rd.t1d.controller;

import com.rd.t1d.data.entity.node.Post;
import com.rd.t1d.data.entity.node.Response;
import com.rd.t1d.data.entity.node.Tag;
import com.rd.t1d.data.entity.projection.PostHeader;
import com.rd.t1d.data.entity.relations.Reaction;
import com.rd.t1d.dto.PostDetails;
import com.rd.t1d.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.websocket.server.PathParam;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PutMapping("/user/{email}/topics")
    public ResponseEntity addTopicsOfInterest(@PathVariable("email") String email, @RequestBody List<Tag> tagList){
        postService.addTopicsOfInterest(email, tagList);
        return new ResponseEntity<>("Topics of interests added successfully", HttpStatus.OK);
    }

    @PostMapping("/user/{email}/post")
    public ResponseEntity createPost(@PathVariable("email") String email, @RequestBody Post post){
        postService.createPost(email, post);
        return new ResponseEntity<>("Post created successfully", HttpStatus.OK);
    }

    @PostMapping("/user/{email}/post/{id}/post-response")
    public ResponseEntity addPostResponse(@PathVariable("email") String email,
                                                 @PathVariable("id") Long postId, @RequestBody Response response){
        postService.createPostResponse(email, postId, response);
        return new ResponseEntity<>("Post response created successfully", HttpStatus.OK);
    }

    @PostMapping("/user/{email}/post/{id}/post-reaction")
    public ResponseEntity addPostReaction(@PathVariable("email") String email,
                                                 @PathVariable("id") Long postId, @RequestBody Reaction reaction){
        postService.addReaction(email, postId, null, reaction);
        return new ResponseEntity<>("Post reaction created successfully", HttpStatus.OK);
    }

    @DeleteMapping("/user/{email}/post/{id}/post-reaction")
    public ResponseEntity removePostReaction(@PathVariable("email") String email,
                                          @PathVariable("id") Long postId, @RequestBody Reaction reaction){
        postService.deleteReaction(email, postId, null, reaction);
        return new ResponseEntity<>("Post reaction removed successfully", HttpStatus.OK);
    }

    @PostMapping("/user/{email}/post/{id}/post-bookmark")
    public ResponseEntity bookmarkPost(@PathVariable("email") String email,
                                          @PathVariable("id") Long postId){
        postService.bookmarkPost(email, postId);
        return new ResponseEntity<>("Post bookmarked successfully", HttpStatus.OK);
    }

    @DeleteMapping("/user/{email}/post/{id}/post-bookmark")
    public ResponseEntity deleteBookmark(@PathVariable("email") String email,
                                       @PathVariable("id") Long postId){
        postService.deleteBookmark(email, postId);
        return new ResponseEntity<>("Post bookmark deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/user/{email}/response/{id}/response-reaction")
    public ResponseEntity addResponseReaction(@PathVariable("email") String email,
                                                  @PathVariable("id") Long responseId, @RequestBody Reaction reaction){
        postService.addReaction(email, null, responseId, reaction);
        return new ResponseEntity<>("Response reaction created successfully", HttpStatus.OK);
    }

    @DeleteMapping("/user/{email}/response/{id}/response-reaction")
    public ResponseEntity removeResponseReaction(@PathVariable("email") String email,
                                              @PathVariable("id") Long responseId, @RequestBody Reaction reaction){
        postService.deleteReaction(email, null, responseId, reaction);
        return new ResponseEntity<>("Response reaction deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/user/{email}/post-headers/{filter}")
    public List<PostHeader> getPostHeaders(@RequestParam("created_at") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime createdAt,
                                           @RequestParam("batch_size") int batchSize, @PathVariable("email") String email, @PathVariable("filter") String filter){
        return postService.fetchPostHeaderList(email, createdAt, batchSize, filter);
    }

    @GetMapping("/user/{email}/post-details/{post-id}")
    public PostDetails getPostDetails(@PathVariable("post-id") Long postId, @PathVariable("email") String email){
        return postService.fetchPostDetails(email, postId);
    }
}
