package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Invitation;
import com.rd.t1d.data.entity.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository  extends Neo4jRepository<Invitation, Long> {
    Invitation findByInvitationKey(@Param("invitation_key") String invitationKey);

    Invitation findByInvitationKeyAndEmail(@Param("invitation_key") String invitationKey, @Param("email") String email);

    Invitation findByEmail(@Param("email") String email, User invitedBy);
}
