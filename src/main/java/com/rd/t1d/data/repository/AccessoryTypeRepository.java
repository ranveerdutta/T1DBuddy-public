package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.AccessoryType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessoryTypeRepository extends Neo4jRepository<AccessoryType, Long> {

    AccessoryType findByType(String type);

}
