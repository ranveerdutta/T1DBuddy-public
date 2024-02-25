package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.BgLevel;
import com.rd.t1d.enums.BgUnit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BgLevelRepository extends Neo4jRepository<BgLevel, Long> {

    BgLevel findByBgNumberAndBgUnit(@Param("bg_number")Double bgNumber, @Param("bg_unit") BgUnit bgUnit);
}
