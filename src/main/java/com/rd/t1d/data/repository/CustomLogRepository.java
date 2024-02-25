package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.InsulinDose;
import com.rd.t1d.data.entity.projection.AccessoryResult;
import com.rd.t1d.data.entity.projection.BgReadingResult;
import com.rd.t1d.data.entity.projection.ExerciseResult;
import com.rd.t1d.data.entity.projection.InsulinDoseResult;
import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.enums.BgUnit;
import com.rd.t1d.enums.InsulinDoseType;
import com.rd.t1d.enums.InsulinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomLogRepository {

    @Autowired
    private Neo4jClient neo4jClient;

    public List<BgReadingResult> findAllBgReadingResult(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        return new ArrayList(this.neo4jClient
                .query("MATCH (m:User)-[r:BG_READING]->(n:Bg_Level) where m.email = $email and r.measured_at >= datetime($startDate) and r.measured_at <= datetime($endDate) RETURN ID(r) as logId, r.measured_at as measuredAt, r.raw_bg as rawBg, n.bg_number as bgNumber, n.bg_unit as bgUnit order by r.measured_at asc")
                .bind(email).to("email")
                .bind(startDate).to("startDate")
                .bind(endDate).to("endDate")
                .fetchAs(BgReadingResult.class)
                .mappedBy((typeSystem, record) -> new BgReadingResult(record.get("logId").asLong(),
                        record.get("measuredAt").asZonedDateTime(),
                        record.get("bgNumber").asDouble(), "NULL".equalsIgnoreCase(record.get("rawBg").toString()) ? null : record.get("rawBg").asDouble(), BgUnit.valueOf(record.get("bgUnit").asString())))
                .all());
    }


    public List<InsulinDoseResult> findAllInsulinDoseResult(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        return new ArrayList(this.neo4jClient
                .query("MATCH (m:User)-[r:INSULIN_TAKEN]->(n:Insulin_Dose) where m.email = $email and r.taken_at >= datetime($startDate) and r.taken_at <= datetime($endDate) RETURN ID(r) as logId, r.taken_at as takenAt, r.insulin_dose_type as insulinDoseType, n.quantity as quantity, n.insulin_type as insulinType order by r.taken_at asc")
                .bind(email).to("email")
                .bind(startDate).to("startDate")
                .bind(endDate).to("endDate")
                .fetchAs(InsulinDoseResult.class)
                .mappedBy((typeSystem, record) -> new InsulinDoseResult(record.get("logId").asLong(),
                        record.get("takenAt").asZonedDateTime(),
                        record.get("quantity").asDouble(), InsulinType.valueOf(record.get("insulinType").asString()), InsulinDoseType.valueOf(record.get("insulinDoseType").asString())))
                .all());
    }

    public List<ExerciseResult> findAllExerciseResult(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        return new ArrayList(this.neo4jClient
                .query("MATCH (m:User)-[r:EXERCISE_DONE]->(n:Exercise_Type) where m.email = $email and r.done_at >= datetime($startDate) and r.done_at <= datetime($endDate) RETURN ID(r) as logId, r.done_at as doneAt, r.duration_in_minutes as durationInMinutes, r.more_details as moreDetails, n.type as exerciseType order by r.done_at asc")
                .bind(email).to("email")
                .bind(startDate).to("startDate")
                .bind(endDate).to("endDate")
                .fetchAs(ExerciseResult.class)
                .mappedBy((typeSystem, record) -> new ExerciseResult(record.get("logId").asLong(),
                        record.get("doneAt").asZonedDateTime(),
                        record.get("durationInMinutes").asLong(), record.get("moreDetails").asString(), record.get("exerciseType").asString()))
                .all());
    }

    public List<AccessoryResult> findAllAccessoryResult(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        return new ArrayList(this.neo4jClient
                .query("MATCH (m:User)-[r:ACCESSORY_CHANGED]->(n:Accessory_Type) where m.email = $email and r.changed_at >= datetime($startDate) and r.changed_at <= datetime($endDate) RETURN ID(r) as logId, r.changed_at as changedAt, n.type as accessoryType order by r.changed_at asc")
                .bind(email).to("email")
                .bind(startDate).to("startDate")
                .bind(endDate).to("endDate")
                .fetchAs(AccessoryResult.class)
                .mappedBy((typeSystem, record) -> new AccessoryResult(record.get("logId").asLong(),
                        record.get("changedAt").asZonedDateTime(),
                        record.get("accessoryType").asString()))
                .all());
    }

    public List<BasalRate> getActiveBasalRateList(Long userId){
        return new ArrayList(this.neo4jClient
                .query("MATCH (a:User)-[r:BASAL_RATE]->(b:Insulin_Dose) WHERE ID(a) = $userId and r.end_date IS NULL return ID(r) as id, r.start_date as startDate,"
                        + " r.start_time as startTime, r.end_time as endTime, r.created_at as createdAt, r.updated_at as updatedAt, b.quantity as quantity, b.insulin_type as insulinType order by r.start_time asc")
                .bind(userId).to("userId")
                .fetchAs(BasalRate.class)
                .mappedBy((typeSystem, record) -> new BasalRate(record.get("id").asLong(),
                        record.get("startDate").asZonedDateTime(),
                        null ,
                        record.get("startTime").asLocalTime(),
                        record.get("endTime").asLocalTime(),
                        new InsulinDose(record.get("quantity").asDouble(), InsulinType.valueOf(record.get("insulinType").asString())),
                        record.get("createdAt").asZonedDateTime(),
                        record.get("updatedAt").asZonedDateTime()))
                .all());
    }

    public List<BasalRate> getActiveBasalRateList(Long userId, ZonedDateTime startDate, ZonedDateTime endTime){
        return new ArrayList(this.neo4jClient
                .query("MATCH (a:User)-[r:BASAL_RATE]->(b:Insulin_Dose) WHERE ID(a) = $userId and r.start_date < $endDate and r.end_date IS NULL return ID(r) as id, r.start_date as startDate,"
                        + " r.start_time as startTime, r.end_time as endTime, r.created_at as createdAt, r.updated_at as updatedAt, b.quantity as quantity, b.insulin_type as insulinType order by r.start_time asc")
                .bind(userId).to("userId")
                .bind(endTime).to("endDate")
                .fetchAs(BasalRate.class)
                .mappedBy((typeSystem, record) -> new BasalRate(record.get("id").asLong(),
                        record.get("startDate").asZonedDateTime(),
                        null ,
                        record.get("startTime").asLocalTime(),
                        record.get("endTime").asLocalTime(),
                        new InsulinDose(record.get("quantity").asDouble(), InsulinType.valueOf(record.get("insulinType").asString()))
                        ))
                .all());
    }


    public List<BasalRate> getBasalRateList(Long userId, ZonedDateTime startDate, ZonedDateTime endTime){
        return new ArrayList(this.neo4jClient
                .query("MATCH (a:User)-[r:BASAL_RATE]->(b:Insulin_Dose) WHERE ID(a) = $userId and ((r.start_date >= $startDate and r.start_date < $endDate) or (r.start_date < $startDate and r.end_date > $startDate)) and r.end_date IS NOT NULL"
                        + " return ID(r) as id, r.start_date as startDate, r.end_date as endDate,"
                        + " r.start_time as startTime, r.end_time as endTime, b.quantity as quantity, b.insulin_type as insulinType order by r.start_date, r.start_time asc")
                .bind(userId).to("userId")
                .bind(startDate).to("startDate")
                .bind(endTime).to("endDate")
                .fetchAs(BasalRate.class)
                .mappedBy((typeSystem, record) -> new BasalRate(record.get("id").asLong(),
                        record.get("startDate").asZonedDateTime(),
                        record.get("endDate").asZonedDateTime() ,
                        record.get("startTime").asLocalTime(),
                        record.get("endTime").asLocalTime(),
                        new InsulinDose(record.get("quantity").asDouble(), InsulinType.valueOf(record.get("insulinType").asString()))
                        ))
                .all());
    }



}
