package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.ReminderType;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.relations.Reminder;
import com.rd.t1d.data.repository.CustomReminderRepository;
import com.rd.t1d.data.repository.ReminderTypeRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReminderService {

    private static final String REMINDER_STATUS_UPCOMING = "Upcoming";

    private static final String REMINDER_STATUS_NOTIFIED = "Notified";

    private static final String REMINDER_STATUS_DONE = "Done";

    private static final String REMINDER_STATUS_DELETED = "Deleted";

    @Autowired
    private ReminderTypeRepository reminderTypeRepository;

    @Autowired
    private CustomReminderRepository customReminderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<ReminderType> getReminderTypeList(){
        return reminderTypeRepository.findReminderTypeList();
    }

    @Transactional
    public void createReminder(String email, Reminder reminder){
        log.info("Received add reminder for the user: " + email);
        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        ReminderType reminderType = reminderTypeRepository.findReminderType(reminder.getReminderType().getCode());

        if(null == reminderType){
            throw new T1DBuddyException(ErrorCode.WRONG_REACTION_TYPE, HttpStatus.BAD_REQUEST);
        }

        ZonedDateTime now = ZonedDateTime.now();

        if(null == reminder.getReminderTime() || reminder.getReminderTime().isBefore(now)){
            throw new T1DBuddyException(ErrorCode.INVALID_DATE, HttpStatus.BAD_REQUEST);
        }

        Long reminderId  = null;
        if(StringUtils.isBlank(reminder.getMoreDetails())){
            reminderId = reminderTypeRepository.createReminder(user.getId(), reminderType.getId(), now, reminder.getReminderTime(), REMINDER_STATUS_UPCOMING);
        }else{
            reminderId = reminderTypeRepository.createReminder(user.getId(), reminderType.getId(), now, reminder.getReminderTime(), REMINDER_STATUS_UPCOMING, reminder.getMoreDetails());
        }

        notificationService.createReminderNotification(reminderId, reminderType, user, reminder.getReminderTime());

    }

    @Transactional
    public void updateReminderStatus(String email, Long reminderId, String status){
        log.info("Received update reminder status for the user: " + email);

        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isBlank(status) || !(REMINDER_STATUS_NOTIFIED.equals(status) || REMINDER_STATUS_DELETED.equals(status) || REMINDER_STATUS_DONE.equals(status))){
            throw new T1DBuddyException(ErrorCode.INVALID_REMINDER_STATUS, HttpStatus.BAD_REQUEST);
        }

        Optional<Reminder> reminderOptional = customReminderRepository.findReminderById(user.getId(), reminderId);

        if(reminderOptional.isEmpty()){
            throw new T1DBuddyException(ErrorCode.NO_MATCHING_REMINDER, HttpStatus.BAD_REQUEST);
        }

        Reminder reminder = reminderOptional.get();

        if(REMINDER_STATUS_NOTIFIED.equals(status) && !REMINDER_STATUS_UPCOMING.equals(reminder.getStatus())){
            throw new T1DBuddyException(ErrorCode.INVALID_REMINDER_STATUS, HttpStatus.BAD_REQUEST);
        }

        if(REMINDER_STATUS_DELETED.equals(status) && !REMINDER_STATUS_UPCOMING.equals(reminder.getStatus())){
            throw new T1DBuddyException(ErrorCode.INVALID_REMINDER_STATUS, HttpStatus.BAD_REQUEST);
        }

        if(REMINDER_STATUS_DONE.equals(status) && !(REMINDER_STATUS_UPCOMING.equals(reminder.getStatus()) || REMINDER_STATUS_NOTIFIED.equals(reminder.getStatus()))){
            throw new T1DBuddyException(ErrorCode.INVALID_REMINDER_STATUS, HttpStatus.BAD_REQUEST);
        }

        reminderTypeRepository.updateReminderStatus(reminderId, ZonedDateTime.now(), status);

        if(REMINDER_STATUS_DONE.equals(status) && REMINDER_STATUS_UPCOMING.equals(reminder.getStatus())){
            notificationService.cancelNotification(reminderId, NotificationService.NOTIFICATION_TYPE_REMINDER);
        }

    }

    @Transactional
    public List<Reminder> getPendingReminders(String email){
        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        return customReminderRepository.findPendingReminders(user.getId());
    }
}
