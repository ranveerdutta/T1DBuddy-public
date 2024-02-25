package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.ExerciseType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseTypeRepository extends Neo4jRepository<ExerciseType, Long> {

    ExerciseType findByType(String type);

}
