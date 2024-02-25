package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Response;
import com.rd.t1d.enums.ReactionType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ResponseRepository extends Neo4jRepository<Response, Long> {

    @Query("MATCH (a:User)<-[r:RESPONSE_REACTED_BY]-(b:Response) WHERE ID(a) = $userId AND ID(b) = $responseId AND r.reaction_type = $reactionType return b")
    List<Response> findResponseReactedByUser(Long userId, Long responseId, ReactionType reactionType);

    @Query("MATCH (a:User), (b:Response) WHERE ID(a) = $userId AND ID(b) = $responseId CREATE (a)<-[r:RESPONSE_REACTED_BY {reacted_at: $reactedAt, reaction_type: $reactionType}]-(b)")
    void createResponseReaction(Long userId, Long responseId, ZonedDateTime reactedAt, ReactionType reactionType);

    @Query("MATCH (a:User)<-[r:RESPONSE_REACTED_BY {reaction_type: $reactionType}]-(b:Response) WHERE ID(a) = $userId AND ID(b) = $responseId delete r")
    void deleteResponseReaction(Long userId, Long responseId, ZonedDateTime reactedAt, ReactionType reactionType);

    @Query("MATCH (a:User), (b:Response) WHERE ID(a) = $userId AND ID(b) = $responseId CREATE (a)<-[r:RESPONDED_BY]-(b)")
    void createRespondedBy(Long userId, Long responseId);

    @Query("MATCH (p:Response) WHERE ID(p) = $responseId return p")
    Response findResponseById(Long responseId);

    @Query("MATCH (a:Post), (b:Response) WHERE ID(a) = $postId AND ID(b) = $responseId CREATE (a)-[r:POST_RESPONSE]->(b)")
    void createPostResponse(Long postId, Long responseId);

}
