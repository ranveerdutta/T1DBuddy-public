package com.rd.t1d.exception;

public enum ErrorCode {

    GENERIC_ERROR("Error occurred"),
    UNAUTHORIZED_REQUEST("Unauthorized request"),
    WRONG_RELATION("Wrong Relation"),
    WRONG_SCOPE("Wrong Scope"),
    WRONG_ROLE("Wrong Role"),
    WRONG_BG_UNIT("Wrong BG unit"),
    WRONG_BG_READING_SOURCE("Wrong BG reading source"),
    WRONG_REACTION_TYPE("Wrong reaction type"),
    WRONG_FILE_TYPE("Wrong file type"),
    WRONG_STATUS("Wrong Status"),
    WRONG_INVITATION_KEY("Wrong invitation key"),
    WRONG_EMAIL("Wrong email id"),
    WRONG_REMINDER_TYPE_CODE("Wrong reminder type"),
    INVALID_DATE("Invalid date"),
    INVALID_USER("Invalid user"),
    INVALID_REMINDER_STATUS("Invalid reminder status"),
    INVALID_REMINDER_TYPE("Invalid reminder type"),
    NO_MATCHING_REMINDER("No matching reminder"),
    NOT_PUMP_USER("Not a pump user"),
    WRONG_POST_TYPE("Wrong post type"),
    POST_NOT_FOUND("Post not found"),
    INVALID_POST_CONTENT("Invalid post content"),
    TAG_ALREADY_AVAILABLE("Tag is already available"),
    ALREADY_REQUESTED("Connection request already sent"),
    ALREADY_REGISTERED("User is already registered"),
    NOT_FOLLOWING("User not following"),
    ALREADY_FOLLOWING("User is already being followed"),
    NO_PENDING_REQUEST_FOUND("No pending request found"),
    FILE_NOT_FOUND("File not found"),
    INVALID_LOG_TYPE("This log type is not supported"),
    INVALID_LOG("No matching log found"),
    ALREADY_INVITED("This user was already invited by you"),
    ALREADY_LIKED("Already liked by this user"),
    ALREADY_BOOKMARKED("Already bookmarked by this user"),
    NOT_BOOKMARKED("Not bookmarked by this user"),
    INVALID_RECORD_CONTENT("Invalid record content"),
    NO_REACTION_FOUND("Not reacted by this user"),
    INVALID_FILE("Invalid file");

    private String msg;

    ErrorCode(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
