package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Food;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends Neo4jRepository<Food, Long> {


}
