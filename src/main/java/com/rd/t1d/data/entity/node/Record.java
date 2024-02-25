package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.ZonedDateTime;

@Data
@Node("Record")
public class Record extends BaseNode {

    @Property("record_date")
    @JsonProperty("record_date")
    private ZonedDateTime recordDate;

    @Property("description")
    @JsonProperty("description")
    private String description;

    @Property("file_id")
    @JsonProperty("file_id")
    private String fileId;

}
