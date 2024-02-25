package com.rd.t1d.data.entity.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class ExerciseResult  implements Serializable {

    @JsonProperty("log_id")
    private Long logId;

    @JsonProperty("done_at")
    private ZonedDateTime doneAt;

    @JsonProperty("duration_in_minutes")
    private Long durationInMinutes;

    @JsonProperty("more_details")
    private String moreDetails;

    @JsonProperty("exercise_type")
    private String exerciseType;

    public ExerciseResult(Long logId, ZonedDateTime doneAt, Long durationInMinutes, String moreDetails, String exerciseType) {
        this.logId = logId;
        this.doneAt = doneAt;
        this.durationInMinutes = durationInMinutes;
        this.moreDetails = moreDetails;
        this.exerciseType = exerciseType;
    }
}
