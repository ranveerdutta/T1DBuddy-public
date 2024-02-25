package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.User;
import lombok.Data;

@Data
public class UserProfileInput extends User {

    @JsonProperty("profile_photo_id")
    private String profilePhotoId;
}
