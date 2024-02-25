package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.BgLevel;
import com.rd.t1d.enums.BgReadingSource;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class BgReading {

    @Id
    @GeneratedValue
    private Long id;

    @Property("raw_bg")
    @JsonProperty("raw_bg")
    private Double rawBg;

    @Property("measured_at")
    @JsonProperty("measured_at")
    private ZonedDateTime measuredAt;

    @JsonProperty("bg_level")
    @TargetNode
    private BgLevel bgLevel;

    @Property("bg_reading_source")
    @JsonProperty("bg_reading_source")
    private BgReadingSource readingSource;

}
