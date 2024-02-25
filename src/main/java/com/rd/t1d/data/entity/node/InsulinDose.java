package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.enums.InsulinType;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Insulin_Dose")
public class InsulinDose extends BaseNode{

    @Property("quantity")
    @JsonProperty("quantity")
    private Double quantity;

    @Property("insulin_type")
    @JsonProperty("insulin_type")
    private InsulinType insulinType;

    public InsulinDose() {
    }

    public InsulinDose(Double quantity, InsulinType insulinType) {
        this.quantity = quantity;
        this.insulinType = insulinType;
    }
}
