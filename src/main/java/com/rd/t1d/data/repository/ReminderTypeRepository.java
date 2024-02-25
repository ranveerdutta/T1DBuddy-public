package com.rd.t1d.data.repository;


import com.rd.t1d.data.entity.node.ReminderType;
import com.rd.t1d.data.entity.relations.Reminder;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReminderTypeRepository extends Neo4jRepository<ReminderType, Long> {

    @Query("MATCH (m:Reminder_Type) WHERE m.is_active = true RETURN m")
    List<ReminderType> findReminderTypeList();

    @Query("MATCH (m:Reminder_Type) WHERE m.code = $reminderCode RETURN m")
    ReminderType findReminderType(String reminderCode);

    @Query("MATCH (a:User), (b:Reminder_Type) WHERE ID(a) = $userId AND ID(b) = $reminderTypeId CREATE (a)-[r:REMINDER {created_at: $now, updated_at: $now, reminder_time: $reminderTime, status: $status}]->(b) return ID(r)")
    Long createReminder(Long userId, Long reminderTypeId, ZonedDateTime now, ZonedDateTime reminderTime, String status);

    @Query("MATCH (a:User), (b:Reminder_Type) WHERE ID(a) = $userId AND ID(b) = $reminderTypeId CREATE (a)-[r:REMINDER {created_at: $now, updated_at: $now, reminder_time: $reminderTime, status: $status, more_details: $moreDetails}]->(b) return ID(r)")
    Long createReminder(Long userId, Long reminderTypeId, ZonedDateTime now, ZonedDateTime reminderTime, String status, String moreDetails);

    @Query("MATCH (m:User)-[r:REMINDER]->(n:Reminder_Type) WHERE ID(r) = $reminderId set r.status= $status, r.updated_at= $now")
    void updateReminderStatus(Long reminderId, ZonedDateTime now, String status);

}
