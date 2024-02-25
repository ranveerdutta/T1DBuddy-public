package com.rd.t1d.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.rd.t1d.data.entity.node.Notification;
import com.rd.t1d.data.entity.node.ReminderType;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.repository.NotificationRepository;
import com.rd.t1d.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    public static final String NOTIFICATION_TYPE_REMINDER = "Reminder";
    public static final String NOTIFICATION_TYPE_FOLLOWER = "Follower";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${fcm.notification.max.retry}")
    private Integer maxRetryCount;

    @Value("${fcm.notification.batch.size}")
    private Integer notificationBatchSize;

    @Async
    @Transactional
    public void createReminderNotification(Long reminderId, ReminderType reminderType, User user, ZonedDateTime reminderTime){

        if(StringUtils.isBlank(user.getFcmToken())){
            log.info("can't create notification for the user: " + user.getEmail());
            return;
        }
        Notification notification = new Notification();
        notification.setNotificationType(NOTIFICATION_TYPE_REMINDER);
        notification.setBody(reminderType.getMessage());
        notification.setTitle("Reminder");
        notification.setUniqueId(reminderId);
        notification.setUserEmail(user.getEmail());
        ZonedDateTime now = ZonedDateTime.now();
        notification.setCreatedAt(now);
        notification.setUpdatedAt(now);
        notification.setNotificationTime(reminderTime);
        notification.setStatus("INIT");

        notificationRepository.save(notification);
    }

    @Async
    @Transactional
    public void createFollowerNotification(Long followId, User toUser, String fromUserName){

        if(StringUtils.isBlank(toUser.getFcmToken())){
            log.info("can't create notification for the user: " + toUser.getEmail());
            return;
        }
        Notification notification = new Notification();
        notification.setNotificationType(NOTIFICATION_TYPE_FOLLOWER);
        notification.setBody(fromUserName + " started following you.");
        notification.setTitle("New connection");
        notification.setUniqueId(followId);
        notification.setUserEmail(toUser.getEmail());
        ZonedDateTime now = ZonedDateTime.now();
        notification.setCreatedAt(now);
        notification.setUpdatedAt(now);
        notification.setNotificationTime(now);
        notification.setStatus("INIT");

        notificationRepository.save(notification);
    }

    @Async
    @Transactional
    public void cancelNotification(Long uniqueId, String notificationType){

        notificationRepository.cancelNotification(uniqueId, notificationType, ZonedDateTime.now());
    }


    @Transactional
    public  void sendMessage(Notification notification) {
        User user = userRepository.findUserObjectByEmail(notification.getUserEmail());
        if(null == user){
            log.info("can't send notification because user does not exist: " + notification.getUserEmail());
            cancelNotification(notification.getUniqueId(), notification.getNotificationType());
            return;
        }
        try{
            com.google.firebase.messaging.Notification nObj = com.google.firebase.messaging.Notification.builder()
                    .setTitle(notification.getTitle())
                    .setBody(notification.getBody())
                    .build();
            Message message = Message.builder()
                    .setNotification(nObj)
                    .setToken(user.getFcmToken())
                    .build();
            FirebaseMessaging.getInstance().send(message);
            log.info("successfully sent notification id: " + notification.getId());
            notificationRepository.markNotificationAsSent(notification.getUniqueId(), notification.getNotificationType(), ZonedDateTime.now());
        }catch(Exception ex){
            log.error("could not send notification id: " + notification.getId());
            log.error("error while sending notification", ex);
            handleError(notification);
        }
    }

    private void handleError(Notification notification){
        Integer retryCount = notification.getRetryCount() == null ? 1 : notification.getRetryCount() + 1;
        notificationRepository.updateRetryCount(notification.getId(), retryCount, ZonedDateTime.now());
        if(retryCount >= maxRetryCount){
            cancelNotification(notification.getUniqueId(), notification.getNotificationType());
        }

    }

    @Transactional
    public void sendNotifications(){
        log.info("starting job to send fcm notifications");
        ZonedDateTime now = ZonedDateTime.now();
        now = now.plusMinutes(15);
        List<Notification> notificationList = notificationRepository.fetchActiveNotifications(now, notificationBatchSize);
        log.info("total active notification count: " + notificationList.size());
        for(Notification notification : notificationList){
            sendMessage(notification);
        }
        log.info("finished job to send fcm notifications");
    }
}
