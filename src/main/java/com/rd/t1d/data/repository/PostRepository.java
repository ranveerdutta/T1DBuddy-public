package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Post;
import com.rd.t1d.enums.ReactionType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface PostRepository extends Neo4jRepository<Post, Long> {

    @Query("MATCH (a:User)<-[r:POST_REACTED_BY]-(b:Post) WHERE ID(a) = $userId AND ID(b) = $postId AND r.reaction_type = $reactionType return b")
    List<Post> findPostReactedByUser(Long userId, Long postId, ReactionType reactionType);

    @Query("MATCH (a:User)-[r:POST_BOOKMARKED]->(b:Post) WHERE ID(a) = $userId AND ID(b) = $postId return b")
    List<Post> findPostBookmarkedByUser(Long userId, Long postId);

    @Query("MATCH (a:User), (b:Post) WHERE ID(a) = $userId AND ID(b) = $postId CREATE (a)<-[r:POST_REACTED_BY {reacted_at: $reactedAt, reaction_type: $reactionType}]-(b)")
    void createPostReaction(Long userId, Long postId, ZonedDateTime reactedAt, ReactionType reactionType);

    @Query("MATCH (a:User)<-[r:POST_REACTED_BY {reaction_type: $reactionType}]-(b:Post) WHERE ID(a) = $userId AND ID(b) = $postId delete r")
    void deletePostReaction(Long userId, Long postId, ReactionType reactionType);


    @Query("MATCH (a:User), (b:Post) WHERE ID(a) = $userId AND ID(b) = $postId CREATE (a)-[r:POST_BOOKMARKED]->(b)")
    void bookmarkPost(Long userId, Long postId);

    @Query("MATCH (a:User)-[r:POST_BOOKMARKED]->(b:Post) WHERE ID(a) = $userId AND ID(b) = $postId delete r")
    void deleteBookmark(Long userId, Long postId);

    @Query("MATCH (a:User), (b:Post) WHERE ID(a) = $userId AND ID(b) = $postId CREATE (a)-[r:POST_CREATED]->(b)")
    void createUserPost(Long userId, Long postId);

    @Query("MATCH (p:Post) WHERE ID(p) = $postId return p")
    Post findPostById(Long postId);

}
