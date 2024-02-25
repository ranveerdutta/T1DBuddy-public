package com.rd.t1d.data.entity.node;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Reminder_Type")
public class ReminderType extends  BaseNode{

    @Property("code")
    @JsonProperty("code")
    private String code;

    @Property("name")
    @JsonProperty("name")
    private String name;

    @Property("message")
    @JsonProperty("message")
    private String message;

    @Property("is_active")
    @JsonProperty("is_active")
    private Boolean isActive;

    public ReminderType() {
    }

    public ReminderType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
