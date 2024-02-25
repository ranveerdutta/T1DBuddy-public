package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.FoodContent;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodContentRepository extends Neo4jRepository<FoodContent, Long> {

    FoodContent findByContent(String content);
}
