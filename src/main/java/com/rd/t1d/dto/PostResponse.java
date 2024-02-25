package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class PostResponse{

    @JsonProperty("response_id")
    private Long responseId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("use_avatar")
    private Boolean useAvatar;

    @JsonProperty("author_first_name")
    private String authorFirstName;

    @JsonProperty("author_last_name")
    private String authorLastName;

    @JsonProperty("author_email")
    private String authorEmail;

    @JsonProperty("author_avatar")
    private String authorAvatar;

    @JsonProperty("reaction_count")
    private Long reactionCount;

    @JsonProperty("is_liked")
    private Boolean isLiked;

    public PostResponse(long responseId, String content, ZonedDateTime createdAt, Boolean useAvatar, String authorFirstName, String authorLastName, String authorEmail, String authorAvatar, long reactionCount) {
        this.responseId = responseId;
        this.content = content;
        this.createdAt = createdAt;
        this.useAvatar = useAvatar;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorEmail = authorEmail;
        this.authorAvatar = authorAvatar;
        this.reactionCount = reactionCount;
    }

    public PostResponse(long responseId, String content, ZonedDateTime createdAt, Boolean useAvatar, String authorFirstName, String authorLastName, String authorEmail, String authorAvatar, long reactionCount, Boolean isLiked) {
        this.responseId = responseId;
        this.content = content;
        this.createdAt = createdAt;
        this.useAvatar = useAvatar;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorEmail = authorEmail;
        this.authorAvatar = authorAvatar;
        this.reactionCount = reactionCount;
        this.isLiked = isLiked;
    }
}
