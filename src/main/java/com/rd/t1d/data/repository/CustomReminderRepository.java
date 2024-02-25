package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.InsulinDose;
import com.rd.t1d.data.entity.node.ReminderType;
import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.data.entity.relations.Reminder;
import com.rd.t1d.enums.InsulinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomReminderRepository {

    @Autowired
    private Neo4jClient neo4jClient;

    public Optional<Reminder> findReminderById(Long userId, Long reminderId){
        return this.neo4jClient
                .query("MATCH (m:User)-[r:REMINDER]->(n:Reminder_Type) WHERE ID(m) = $userId AND ID(r) = $reminderId RETURN ID(r) as id, r.created_at as createdAt, r.updated_at as updatedAt, r.reminder_time as reminderTime, r.status as status, r.more_details as moreDetails")
                .bind(userId).to("userId")
                .bind(reminderId).to("reminderId")
                .fetchAs(Reminder.class)
                .mappedBy((typeSystem, record) -> new Reminder(record.get("id").asLong(),
                        record.get("createdAt").asZonedDateTime(),
                        record.get("updatedAt").asZonedDateTime() ,
                        record.get("reminderTime").asZonedDateTime(),
                        record.get("status").asString(), record.get("moreDetails").asString()))
                .first();
    }

    public List<Reminder> findPendingReminders(Long userId){
        return new ArrayList(this.neo4jClient
                .query("MATCH (m:User)-[r:REMINDER]->(n:Reminder_Type) WHERE ID(m) = $userId and r.status <> 'Done' and r.status <> 'Deleted' RETURN ID(r) as id, r.created_at as createdAt, r.updated_at as updatedAt, r.reminder_time as reminderTime, " +
                                "r.status as status, r.more_details as moreDetails, n.code as code, n.name as name order by r.reminder_time asc")
                .bind(userId).to("userId")
                .fetchAs(Reminder.class)
                .mappedBy((typeSystem, record) -> new Reminder(record.get("id").asLong(),
                        record.get("createdAt").asZonedDateTime(),
                        record.get("updatedAt").asZonedDateTime() ,
                        record.get("reminderTime").asZonedDateTime(),
                        record.get("status").asString(), record.get("moreDetails").asString(),
                        new ReminderType(record.get("code").asString(), record.get("name").asString())))
                .all());
    }

}
