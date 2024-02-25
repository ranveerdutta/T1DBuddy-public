package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.projection.PostHeader;
import com.rd.t1d.dto.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomPostRepository {

    @Autowired
    private Neo4jClient neo4jClient;

    public List<PostHeader> findAllPostHeader(String email, ZonedDateTime createdAt, int batchSize){
        return new ArrayList(this.neo4jClient
                .query(""
                        + " MATCH (n:Post)<-[rpc:POST_CREATED]-(m:User) where n.created_at<$createdAt  CALL { WITH n MATCH (n)-[rpr:POST_RESPONSE]->(r:Response) RETURN count(r) AS response_count}"
                        + " CALL { WITH n MATCH (n)-[rb:POST_REACTED_BY]->(p:User) RETURN count(rb) AS reaction_count}"
                        + " CALL { WITH n MATCH (n)-[rb1:POST_REACTED_BY]->(p1:User) where p1.email = $email RETURN count(rb1)> 0 AS isLiked}"
                        + " return ID(n) as postId, n.title as title, n.content as content, n.created_at as createdAt, n.image_id as imageId, n.video_url as videoUrl, n.use_avatar as useAvatar, m.first_name as authorFirstName, m.last_name as authorLastName, m.avatar_name as authorAvatar, m.email as authorEmail, response_count as responseCount, reaction_count as reactionCount, isLiked order by n.created_at desc limit $batchSize"
                )
                .bind(createdAt).to("createdAt")
                .bind(batchSize).to("batchSize")
                .bind(email).to("email")
                .fetchAs(PostHeader.class)
                .mappedBy((typeSystem, record) -> new PostHeader(record.get("postId").asLong(),
                        record.get("title").asString(), record.get("content").asString(), record.get("imageId").asString(), record.get("videoUrl").asString(), record.get("createdAt").asZonedDateTime(),
                        record.get("useAvatar").asBoolean(), record.get("authorFirstName").asString(), record.get("authorLastName").asString(),
                        record.get("authorAvatar").asString(), record.get("authorEmail").asString(),
                        record.get("responseCount").asLong(), record.get("reactionCount").asLong(), record.get("isLiked").asBoolean()))
                .all());
    }

    public List<PostHeader> findBookmarkedPostHeader(String email, ZonedDateTime createdAt, int batchSize){
        return new ArrayList(this.neo4jClient
                .query(""
                        + " MATCH (n:Post)<-[rpc:POST_CREATED]-(m:User) MATCH (n)<-[r1:POST_BOOKMARKED]-(m1:User)  where n.created_at<$createdAt and m1.email=$email CALL { WITH n MATCH (n)-[POST_RESPONSE]->(r:Response) RETURN count(r) AS response_count}"
                        + " CALL { WITH n MATCH (n)-[rb:POST_REACTED_BY]->(p:User) RETURN count(rb) AS reaction_count}"
                        + " CALL { WITH n MATCH (n)-[rb1:POST_REACTED_BY]->(p1:User) where p1.email = $email RETURN count(rb1)> 0 AS isLiked}"
                        + " return ID(n) as postId, n.title as title, n.content as content, n.created_at as createdAt, n.image_id as imageId, n.video_url as videoUrl, n.use_avatar as useAvatar, m.first_name as authorFirstName, m.last_name as authorLastName, m.avatar_name as authorAvatar, m.email as authorEmail, response_count as responseCount, reaction_count as reactionCount, isLiked order by n.created_at desc limit $batchSize"
                )
                .bind(createdAt).to("createdAt")
                .bind(batchSize).to("batchSize")
                .bind(email).to("email")
                .fetchAs(PostHeader.class)
                .mappedBy((typeSystem, record) -> new PostHeader(record.get("postId").asLong(),
                        record.get("title").asString(), record.get("content").asString(), record.get("imageId").asString(), record.get("videoUrl").asString(), record.get("createdAt").asZonedDateTime(),
                        record.get("useAvatar").asBoolean(), record.get("authorFirstName").asString(), record.get("authorLastName").asString(),
                        record.get("authorAvatar").asString(), record.get("authorEmail").asString(),
                        record.get("responseCount").asLong(), record.get("reactionCount").asLong(), record.get("isLiked").asBoolean()))
                .all());
    }

    public Optional<PostHeader> findPostHeaderById(String email, Long postId){
        return this.neo4jClient
                .query(""
                        + " MATCH (n:Post)<-[rpc:POST_CREATED]-(m:User) where ID(n) = $postId  CALL { WITH n MATCH (n)-[rps:POST_RESPONSE]->(r:Response) RETURN count(r) AS response_count}"
                        + " CALL { WITH n MATCH (n)-[rb:POST_REACTED_BY]->(p:User) RETURN count(rb) AS reaction_count}"
                        + " CALL { WITH n MATCH (n)-[rpr:POST_REACTED_BY]->(p1:User) where p1.email = $email RETURN count(rpr)> 0 AS isLiked}"
                        + " CALL { WITH n MATCH (n)<-[rpb:POST_BOOKMARKED]-(p2:User) where p2.email = $email RETURN count(rpb)> 0 AS isBookmarked}"
                        + " return ID(n) as postId, n.title as title, n.content as content, n.image_id as imageId, n.video_url as videoUrl, n.created_at as createdAt, n.use_avatar as useAvatar, m.first_name as authorFirstName, m.last_name as authorLastName, m.avatar_name as authorAvatar, m.email as authorEmail, response_count as responseCount, reaction_count as reactionCount, isLiked, isBookmarked"
                )
                .bind(postId).to("postId")
                .bind(email).to("email")
                .fetchAs(PostHeader.class)
                .mappedBy((typeSystem, record) -> new PostHeader(record.get("postId").asLong(),
                        record.get("title").asString(), record.get("content").asString(), record.get("imageId").asString(), record.get("videoUrl").asString(), record.get("createdAt").asZonedDateTime(),
                        record.get("useAvatar").asBoolean(), record.get("authorFirstName").asString(), record.get("authorLastName").asString(),
                        record.get("authorAvatar").asString(), record.get("authorEmail").asString(),
                        record.get("responseCount").asLong(), record.get("reactionCount").asLong(),
                        record.get("isLiked").asBoolean(), record.get("isBookmarked").asBoolean())).first();
    }

    public List<PostResponse> findAllPostResponse(String email, Long postId){
        return new ArrayList(this.neo4jClient
                .query(""
                        + " match(n:Post)-[rps:POST_RESPONSE]->(m:Response)-[r:RESPONDED_BY]->(u:User) where ID(n)=$postId"
                        + " CALL { WITH m MATCH (m)-[rb:RESPONSE_REACTED_BY]->(p:User) RETURN count(rb) AS reaction_count}"
                        + " CALL { WITH m MATCH (p1:User)<-[rrr:RESPONSE_REACTED_BY]-(m) where p1.email = $email RETURN count(rrr)> 0 AS isLiked}"
                        + " return ID(m) as responseId, m.content as content, m.created_at as createdAt, m.use_avatar as useAvatar, u.first_name as authorFirstName, u.last_name as authorLastName, u.email as authorEmail, u.avatar_name as authorAvatar, reaction_count as reactionCount, isLiked order by m.created_at asc"
                )
                .bind(postId).to("postId")
                .bind(email).to("email")
                .fetchAs(PostResponse.class)
                .mappedBy((typeSystem, record) -> new PostResponse(record.get("responseId").asLong(),
                        record.get("content").asString(), record.get("createdAt").asZonedDateTime(), record.get("useAvatar").asBoolean(),
                        record.get("authorFirstName").asString(), record.get("authorLastName").asString(),
                        record.get("authorEmail").asString(), record.get("authorAvatar").asString(), record.get("reactionCount").asLong(), record.get("isLiked").asBoolean()))
                .all());
    }
}
