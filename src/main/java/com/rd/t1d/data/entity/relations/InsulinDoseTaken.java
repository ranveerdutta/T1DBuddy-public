package com.rd.t1d.data.entity.relations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.InsulinDose;
import com.rd.t1d.enums.InsulinDoseType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.ZonedDateTime;

@Data
@RelationshipProperties
public class InsulinDoseTaken {

    @Id
    @GeneratedValue
    private Long id;

    @Property("taken_at")
    @JsonProperty("taken_at")
    private ZonedDateTime takenAt;

    @JsonProperty("insulin_dose")
    @TargetNode
    private InsulinDose insulinDose;

    @Property("insulin_dose_type")
    @JsonProperty("insulin_dose_type")
    private InsulinDoseType insulinDoseType;

}
