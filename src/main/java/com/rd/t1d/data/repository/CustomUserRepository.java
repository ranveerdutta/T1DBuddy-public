package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.projection.UserResult;
import com.rd.t1d.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomUserRepository {

    @Autowired
    private Neo4jClient neo4jClient;

    public List<UserResult> findAllUsers(String email, ZonedDateTime createdAt, int batchSize){
        return new ArrayList(this.neo4jClient
                .query(""
                        + " MATCH (n:User) where n.email <> $email and n.role='MEMBER' and n.created_at < $createdAt CALL { WITH n MATCH (n)<-[f:FOLLOW]-(m:User) where m.email = $email RETURN count(f) > 0 AS follow}"
                        + " CALL { WITH n OPTIONAL MATCH (n)-[pp:PROFILE_PHOTO]->(p:Stored_File) RETURN p.file_id AS photo_id}"
                        + " return ID(n) as id, n.first_name as firstName, n.last_name as lastName, n.email as email, n.city as city, n.state as state, n.country as country,"
                        + " n.date_of_detection as dateOfDetection, n.gender as gender, n.created_at as createdAt, follow as follow, photo_id as photoId order by n.created_at desc limit $batchSize"
                )
                .bind(email).to("email")
                .bind(createdAt).to("createdAt")
                .bind(batchSize).to("batchSize")
                .fetchAs(UserResult.class)
                .mappedBy((typeSystem, record) -> new UserResult(record.get("id").asLong(),
                        record.get("email").asString(), record.get("firstName").asString(), record.get("lastName").asString(), record.get("city").asString(),
                        record.get("state").asString(), record.get("country").asString(), record.get("dateOfDetection").asString(),
                        record.get("gender").asString(), record.get("follow").asBoolean(),
                        record.get("photoId").asString(), record.get("createdAt").asZonedDateTime()))
                .all());
    }

    public List<UserResult> findFollowingUsers(String email, ZonedDateTime createdAt, int batchSize){
        return new ArrayList(this.neo4jClient
                .query(""
                        + " MATCH (n:User)<-[ff:FOLLOW]-(n1:User) where n1.email = $email and n.role='MEMBER' and n.created_at < $createdAt"
                        + " CALL { WITH n OPTIONAL MATCH (n)-[pp:PROFILE_PHOTO]->(p:Stored_File) RETURN p.file_id AS photo_id}"
                        + " return ID(n) as id, n.first_name as firstName, n.last_name as lastName, n.email as email, n.city as city, n.state as state, n.country as country,"
                        + " n.date_of_detection as dateOfDetection, n.gender as gender, n.created_at as createdAt, photo_id as photoId order by n.created_at desc limit $batchSize"
                )
                .bind(email).to("email")
                .bind(createdAt).to("createdAt")
                .bind(batchSize).to("batchSize")
                .fetchAs(UserResult.class)
                .mappedBy((typeSystem, record) -> new UserResult(record.get("id").asLong(),
                        record.get("email").asString(), record.get("firstName").asString(), record.get("lastName").asString(), record.get("city").asString(),
                        record.get("state").asString(), record.get("country").asString(), record.get("dateOfDetection").asString(),
                        record.get("gender").asString(), Boolean.TRUE,
                        record.get("photoId").asString(), record.get("createdAt").asZonedDateTime()))
                .all());
    }

    public List<UserResult> findFollowerUsers(String email, ZonedDateTime createdAt, int batchSize){
        return new ArrayList(this.neo4jClient
                .query(""
                        + " MATCH (n:User)-[ff:FOLLOW]->(n1:User) where n1.email = $email and n.role='MEMBER' and n.created_at < $createdAt CALL { WITH n MATCH (n)<-[f:FOLLOW]-(m:User) where m.email = $email RETURN count(f) > 0 AS follow}"
                        + " CALL { WITH n OPTIONAL MATCH (n)-[pp:PROFILE_PHOTO]->(p:Stored_File) RETURN p.file_id AS photo_id}"
                        + " return ID(n) as id, n.first_name as firstName, n.last_name as lastName, n.email as email, n.city as city, n.state as state, n.country as country,"
                        + " n.date_of_detection as dateOfDetection, n.gender as gender, n.created_at as createdAt, follow as follow, photo_id as photoId order by n.created_at desc limit $batchSize"
                )
                .bind(email).to("email")
                .bind(createdAt).to("createdAt")
                .bind(batchSize).to("batchSize")
                .fetchAs(UserResult.class)
                .mappedBy((typeSystem, record) -> new UserResult(record.get("id").asLong(),
                        record.get("email").asString(), record.get("firstName").asString(), record.get("lastName").asString(), record.get("city").asString(),
                        record.get("state").asString(), record.get("country").asString(), record.get("dateOfDetection").asString(),
                        record.get("gender").asString(), record.get("follow").asBoolean(),
                        record.get("photoId").asString(), record.get("createdAt").asZonedDateTime()))
                .all());
    }
}
