package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.InsulinDose;
import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.enums.InsulinType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface InsulinDoseRepository extends Neo4jRepository<InsulinDose, Long> {

    InsulinDose findByQuantityAndInsulinType(@Param("quantity")Double quantity, @Param("insulin_type") InsulinType insulinType);

    @Query("MATCH (a:User)-[r:BASAL_RATE]->(b:Insulin_Dose) WHERE ID(a) = $userId and r.end_date IS NULL set r.end_date = $currentDate, r.updated_at = $currentDate return count(r)")
    int disableAllBasalRate(Long userId, ZonedDateTime currentDate);

    @Query("MATCH (a:User), (b:Insulin_Dose) WHERE ID(a) = $userId and ID(b)=$insulinDoseId" +
            " CREATE (a)-[r: BASAL_RATE{start_date:$currentDate, start_time: $startTime, end_time:$endTime, created_at:$currentDate, updated_at:$currentDate}]->(b) return count(r)")
    int createBasalRate(Long userId, Long insulinDoseId, ZonedDateTime currentDate, LocalTime startTime, LocalTime endTime);
}
