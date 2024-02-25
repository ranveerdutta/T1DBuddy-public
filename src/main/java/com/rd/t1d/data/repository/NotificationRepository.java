package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.Notification;
import com.rd.t1d.enums.ReactionType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends Neo4jRepository<Notification, Long> {

    @Query("MATCH (a:Notification) WHERE a.status = 'INIT' AND a.notification_time <= $zonedDateTime return a order by a.created_at asc limit $count")
    List<Notification> fetchActiveNotifications(ZonedDateTime zonedDateTime, Integer count);

    @Query("MATCH (a:Notification) WHERE a.notification_type = $notificationType AND a.unique_id = $uniqueId set a.status='CANCELED', a.updatedAt=$now")
    void cancelNotification(Long uniqueId, String notificationType, ZonedDateTime now);

    @Query("MATCH (a:Notification) WHERE a.notification_type = $notificationType AND a.unique_id = $uniqueId set a.status='SENT', a.updatedAt=$now")
    void markNotificationAsSent(Long uniqueId, String notificationType, ZonedDateTime now);

    @Query("MATCH (a:Notification) WHERE ID(a) = $notificationId set a.retry_count=$retryCount, a.updatedAt=$now")
    void updateRetryCount(Long notificationId, Integer retryCount, ZonedDateTime now);
}