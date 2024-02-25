package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.BgUnit;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Bg_Level")
public class BgLevel extends BaseNode{

    public BgLevel(){}

    public BgLevel(Double bgNumber, BgUnit bgUnit) {
        this.bgNumber = bgNumber;
        this.bgUnit = bgUnit;
    }

    @Property("bg_number")
    @JsonProperty("bg_number")
    private Double bgNumber;

    @Property("bg_unit")
    @JsonProperty("bg_unit")
    private BgUnit bgUnit;

}
