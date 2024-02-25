package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.relations.*;
import com.rd.t1d.enums.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties
@Node("User")
public class User extends BaseNode {

    @Property("about_me")
    @JsonProperty("about_me")
    private String aboutMe;

    @Property("first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Property("last_name")
    @JsonProperty("last_name")
    private String lastName;

    @Property("avatar_name")
    @JsonProperty("avatar_name")
    private String avatarName;

    @Property("role")
    @JsonProperty("role")
    private Role role;

    @Property("email")
    @JsonProperty("email")
    private String email;

    @Property("gender")
    @JsonProperty("gender")
    private Gender gender;

    @Property("pin_code")
    @JsonProperty("pin_code")
    private String pinCode;
    
    @Property("city")
    @JsonProperty("city")
    private String city;

    @Property("state")
    @JsonProperty("state")
    private String state;

    @Property("country")
    @JsonProperty("country")
    private String country;

    @Property("dob")
    @JsonProperty("dob")
    private Date dob;

    @Property("fav_super_hero")
    @JsonProperty("fav_super_hero")
    private String favSuperHero;

    @Property("relationship")
    @JsonProperty("relationship")
    private Relation relationship;

    @Property("date_of_detection")
    @JsonProperty("date_of_detection")
    private Date dateOfDetection;

    @Property("glucometer_type")
    @JsonProperty("glucometer_type")
    private String glucometerType;

    @Property("injection_type")
    @JsonProperty("injection_type")
    private InjectionType injectionType;

    @Property("insulin_type")
    @JsonProperty("insulin_type")
    private InsulinType insulinType;

    @Property("on_cgm")
    @JsonProperty("on_cgm")
    private Boolean onCgm;

    @Property("pump_type")
    @JsonProperty("pump_type")
    private String pumpType;

    @Property("last_hba1c")
    @JsonProperty("last_hba1c")
    private Double lastHba1c;

    @Property("last_hba1c_date")
    @JsonProperty("last_hba1c_date")
    private Date lastHba1cDate;

    @Property("visibility_scope")
    @JsonProperty("visibility_scope")
    private Scope visibilityScope;

    @Property("normal_bg_min")
    @JsonProperty("normal_bg_min")
    private Integer normalBgMin;

    @Property("normal_bg_max")
    @JsonProperty("normal_bg_max")
    private Integer normalBgMax;

    @Property("bg_unit")
    @JsonProperty("bg_unit")
    private BgUnit bgUnit;

    @Property("api_secret")
    @JsonProperty("api_secret")
    private String apiSecret;

    @Property("fcm_token")
    @JsonProperty("fcm_token")
    private String fcmToken;

    @Relationship(type="CONNECTION_REQUESTED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<ConnectionRequest> connectionsRequested = new LinkedList<>();

    @Relationship(type="CONNECTED_TO", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Connection> connections = new LinkedList<>();

    @Relationship(type="BG_READING", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<BgReading> bgReadings = new LinkedList<>();

    @Relationship(type="INSULIN_TAKEN", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<InsulinDoseTaken> insulinDosesTaken = new LinkedList<>();

    @Relationship(type="BASAL_RATE", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<BasalRate> basalRateList = new LinkedList<>();

    @Relationship(type="FOOD_TAKEN", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Food> foodList = new LinkedList<>();

    @Relationship(type="POST_CREATED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Post> createdPosts = new LinkedList<>();

    @Relationship(type="TAG_CREATED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Tag> tagsCreated = new LinkedList<>();

    @Relationship(type="TAG_INTERESTED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Tag> tagsInterested = new LinkedList<>();

    @Relationship(type="EXERCISE_DONE", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<ExerciseDone> exerciseDone = new LinkedList<>();

    @Relationship(type="ACCESSORY_CHANGED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<AccessoryChanged> accessoryChanged = new LinkedList<>();

    @Relationship(type="PROFILE_PHOTO", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private StoredFile profilePhoto;

    @Relationship(type="DEVICE", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private DeviceInfo deviceInfo;

    @Relationship(type="POST_BOOKMARKED", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Post> bookmarkedPosts = new LinkedList<>();

    @Relationship(type="FOLLOW", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<User> followList = new LinkedList<>();

    @Relationship(type="REMINDER", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Reminder> reminderList = new LinkedList<>();

    @Relationship(type="MEDICAL_RECORD", direction = Relationship.Direction.OUTGOING)
    @JsonIgnore
    private List<Record> medicalRecordList = new LinkedList<>();

}
