package com.guiswitehome.tplinkutil.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PoePortConfig implements Serializable {

    @JsonProperty("port")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer portNumber;
    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PoeStatus status;
    @JsonProperty("priority")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PoePriority priority;
    @JsonProperty("powerStatus")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PoePowerStatus powerStatus;
    @JsonProperty("powerLimit")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer powerLimit;
    @JsonProperty("current")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double current;
    @JsonProperty("voltage")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double voltage;
    @JsonProperty("wattage")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double wattage;

}
