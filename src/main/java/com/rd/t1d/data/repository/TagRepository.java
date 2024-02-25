package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Tag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends Neo4jRepository<Tag, Long> {

    Tag findByTopicCode(String topicCode);
}
