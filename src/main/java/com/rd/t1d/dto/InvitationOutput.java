package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvitationOutput {

    @JsonProperty("invitation_key")
    private String invitationKey;

    @JsonProperty("email")
    private String email;

    @JsonProperty("invited_by")
    private String invitedBy;

}
