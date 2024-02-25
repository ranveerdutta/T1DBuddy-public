package com.rd.t1d.data.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.Gender;
import com.rd.t1d.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class UserResult implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    private String country;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("follow")
    private Boolean follow;

    @JsonProperty("profile_photo_id")
    private String profilePhotoId;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    public UserResult(Long id, String email, String firstName, String lastName, String city, String state, String country, String dateOfDetection, String gender, Boolean follow, String profilePhotoId, ZonedDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.country = country;
        if(dateOfDetection == null) this.age = 0;
        else{
            Date date = DateUtils.parseDate("yyyy-MM-dd'T'HH:mm:ss'Z'", dateOfDetection);
            if(date == null) {
                date = DateUtils.parseDate("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", dateOfDetection);
                if(date == null) this.age = 0;
                else this.age = DateUtils.yearDiffFromToday(date);
            }
            else this.age = DateUtils.yearDiffFromToday(date);
        }
        this.gender = Gender.valueOf(gender).getDisplayStr();
        this.follow = follow;
        this.profilePhotoId = ("null".equalsIgnoreCase(profilePhotoId)) ? null : profilePhotoId;
        this.createdAt = createdAt;
    }
}
