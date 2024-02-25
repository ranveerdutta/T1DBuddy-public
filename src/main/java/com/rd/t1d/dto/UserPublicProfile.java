package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.StoredFile;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.utils.DateUtils;
import lombok.Data;
import org.apache.catalina.Store;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class UserPublicProfile {

    @JsonProperty("about")
    private String about;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    private String country;

    @JsonProperty("relationship")
    private String relationship;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("glucometer_type")
    private String glucometerType;

    @JsonProperty("injection_type")
    private String injectionType;

    @JsonProperty("insulin_type")
    private String insulinType;

    @JsonProperty("on_cgm")
    private Boolean onCgm;

    @JsonProperty("pump_type")
    private String pumpType;

    @JsonProperty("normal_bg_min")
    private Integer normalBgMin;

    @JsonProperty("normal_bg_max")
    private Integer normalBgMax;

    @JsonProperty("bg_unit")
    private String bgUnit;

    @JsonProperty("profile_photo_id")
    private String profilePhotoId;

    @JsonProperty("date-of-detection")
    private Date dateOfDetection;

    public static UserPublicProfile createPublicProfile(User user, StoredFile profilePhoto){
        UserPublicProfile profile = new UserPublicProfile();
        profile.setAbout(user.getAboutMe());

        profile.setFirstName(user.getFirstName());
        if(StringUtils.isNotBlank(user.getLastName())){
            profile.setLastName(user.getLastName());
        }

        profile.setGender(user.getGender() != null ? user.getGender().getDisplayStr() : "");

        profile.setCity(user.getCity());
        if(StringUtils.isNotBlank(user.getState())){
            profile.setState(user.getState());
        }
        if(StringUtils.isNotBlank(user.getCountry())){
            profile.setCountry(user.getCountry());
        }


        profile.setRelationship(user.getRelationship() != null ? user.getRelationship().getDisplayStr() : "");

        if(user.getDateOfDetection() != null){
            profile.setAge(DateUtils.yearDiffFromToday(user.getDateOfDetection()));
            profile.setDateOfDetection(user.getDateOfDetection());
        }

        profile.setGlucometerType(user.getGlucometerType());
        profile.setPumpType(user.getPumpType());
        profile.setNormalBgMin(user.getNormalBgMin());
        profile.setNormalBgMax(user.getNormalBgMax());
        profile.setOnCgm(user.getOnCgm());
        profile.setBgUnit(user.getBgUnit() != null ? user.getBgUnit().getDisplayStr() : "");
        profile.setInsulinType(user.getInsulinType() != null ? user.getInsulinType().getDisplayStr() : "");
        profile.setInjectionType(user.getInjectionType() != null ? user.getInjectionType().getDisplayStr() : "");
        if(profilePhoto != null){
            profile.setProfilePhotoId(profilePhoto.getFileId());
        }
        return profile;
    }
}
