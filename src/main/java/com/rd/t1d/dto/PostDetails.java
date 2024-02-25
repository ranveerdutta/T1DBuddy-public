package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.projection.PostHeader;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class PostDetails {

    @JsonProperty("post_header")
    private PostHeader postHeader;

    @JsonProperty("post_response_list")
    private List<PostResponse> postResponseList;
}

