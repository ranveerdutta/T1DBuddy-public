package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.Status;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ConnectionInput {

    @JsonProperty("from_email")
    private String fromEmail;

    @JsonProperty("to_email")
    private String toEmail;

    @JsonProperty("status")
    private Status status;
}
