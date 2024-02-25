package com.rd.t1d.data.entity.node;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Device_Info")
public class DeviceInfo extends BaseNode{

    @JsonProperty("os_version")
    @Property("os_version")
    private String osVersion;

    @JsonProperty("brand")
    @Property("brand")
    private String brand;

    @JsonProperty("device")
    @Property("device")
    private String device;

    @JsonProperty("model")
    @Property("model")
    private String model;

    @JsonProperty("device_id")
    @Property("device_id")
    private String deviceId;

    public DeviceInfo(String osVersion, String brand, String device, String model, String deviceId) {
        this.osVersion = osVersion;
        this.brand = brand;
        this.device = device;
        this.model = model;
        this.deviceId = deviceId;
    }
}
