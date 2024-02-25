package com.rd.t1d.data.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class PostHeader implements Serializable {

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("image_id")
    private String imageId;

    @JsonProperty("video_url")
    private String videoUrl;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("use_avatar")
    private Boolean useAvatar;

    @JsonProperty("author_first_name")
    private String authorFirstName;
    
    @JsonProperty("author_last_name")
    private String authorLastName;

    @JsonProperty("author_avatar")
    private String authorAvatar;

    @JsonProperty("author_email")
    private String authorEmail;

    @JsonProperty("response_count")
    private Long responseCount;

    @JsonProperty("reaction_count")
    private Long reactionCount;

    @JsonProperty("is_liked")
    private Boolean isLiked;

    @JsonProperty("is_bookmarked")
    private Boolean isBookmarked;

    public PostHeader(Long postId, String title, String content, String imageId, String videoUrl, ZonedDateTime createdAt, Boolean useAvatar, String authorFirstName, String authorLastName, String authorAvatar,
                      String authorEmail, Long responseCount, Long reactionCount) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
        this.useAvatar = useAvatar;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorAvatar = authorAvatar;
        this.authorEmail = authorEmail;
        this.responseCount = responseCount;
        this.reactionCount = reactionCount;
    }

    public PostHeader(Long postId, String title, String content, String imageId, String videoUrl, ZonedDateTime createdAt, Boolean useAvatar, String authorFirstName, String authorLastName, String authorAvatar,
                      String authorEmail, Long responseCount, Long reactionCount, Boolean isLiked) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
        this.useAvatar = useAvatar;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorAvatar = authorAvatar;
        this.authorEmail = authorEmail;
        this.responseCount = responseCount;
        this.reactionCount = reactionCount;
        this.isLiked = isLiked;
    }

    public PostHeader(Long postId, String title, String content, String imageId, String videoUrl, ZonedDateTime createdAt, Boolean useAvatar, String authorFirstName, String authorLastName, String authorAvatar,
                      String authorEmail, Long responseCount, Long reactionCount, Boolean isLiked, Boolean isBookmarked) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.imageId = imageId;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
        this.useAvatar = useAvatar;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorAvatar = authorAvatar;
        this.authorEmail = authorEmail;
        this.responseCount = responseCount;
        this.reactionCount = reactionCount;
        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;
    }
}
