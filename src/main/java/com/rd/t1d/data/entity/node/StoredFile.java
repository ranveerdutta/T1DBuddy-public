package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Stored_File")
@Data
public class StoredFile extends BaseNode{

    @Property("file_id")
    @JsonProperty("file_id")
    private String fileId;

    @Property("file_name")
    @JsonProperty("file_name")
    private String fileName;

    @Property("parent_folder_id")
    @JsonProperty("parent_folder_id")
    private String parentFolderId;

    @Property("content_type")
    @JsonProperty("content_type")
    private String contentType;

    @Property("created_by_email")
    @JsonProperty("created_by_email")
    private String createdByEmail;

}
