package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Record;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends Neo4jRepository<Record, Long> {

    @Query("MATCH (a:User), (b:Record) WHERE ID(a) = $userId AND ID(b) = $recordId CREATE (a)-[r:MEDICAL_RECORD]->(b)")
    void createUserRecord(Long userId, Long recordId);

    @Query("MATCH (m:User)-[r:MEDICAL_RECORD]->(n:Record) where ID(m)=$userId return n order by n.record_date desc")
    List<Record> getAllMedicalRecords(Long userId);
}
