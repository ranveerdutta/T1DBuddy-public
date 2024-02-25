package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.StoredFile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoredFileRepository extends Neo4jRepository<StoredFile, Long> {

    public List<StoredFile> findByFileId(String fileId);

    @Query("MATCH (a:User)-[r:PROFILE_PHOTO]->(b:Stored_File) WHERE ID(a) = $userId return b")
    StoredFile findProfilePhoto(Long userId);

}

