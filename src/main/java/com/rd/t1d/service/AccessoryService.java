package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.AccessoryType;
import com.rd.t1d.data.entity.node.ReminderType;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.AccessoryResult;
import com.rd.t1d.data.entity.relations.AccessoryChanged;
import com.rd.t1d.data.entity.relations.Reminder;
import com.rd.t1d.data.repository.AccessoryTypeRepository;
import com.rd.t1d.data.repository.CustomLogRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.UserLogReading;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccessoryService {

    private static Map<String, String> accessoryToReminderCodeMap;

    static{
        accessoryToReminderCodeMap = new HashMap<>();
        accessoryToReminderCodeMap.put("NEEDLE", "needle_change");
        accessoryToReminderCodeMap.put("SYRINGE", "syringe_change");
        accessoryToReminderCodeMap.put("INFUSION_SET", "infusion_set_change");
        accessoryToReminderCodeMap.put("RESERVOIR", "reservoir_change");
        accessoryToReminderCodeMap.put("CGM", "cgm_change");
        accessoryToReminderCodeMap.put("LANCET", "lancet_change");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessoryTypeRepository accessoryTypeRepository;

    @Autowired
    private CustomLogRepository customLogRepository;

    @Autowired
    private ReminderService reminderService;

    @Transactional
    public void addAccessoryChange(String email, AccessoryChanged accessoryChanged){
        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();

        if(null == accessoryChanged.getChangedAt()){
            accessoryChanged.setChangedAt(currentDateTime);
        }

        AccessoryType accessoryType = accessoryTypeRepository.findByType(accessoryChanged.getAccessoryType().getType());

        if(null == accessoryType){
            accessoryType = accessoryChanged.getAccessoryType();
            accessoryType.setCreatedAt(currentDateTime);
            accessoryType.setUpdatedAt(currentDateTime);
            accessoryTypeRepository.save(accessoryType);
        }

        userRepository.createAccessoryChanged(user.getId(), accessoryType.getId(), accessoryChanged.getChangedAt(), accessoryChanged.getReminderTime());

        if(accessoryChanged.getReminderTime() != null){
            try{
                Reminder reminder = new Reminder();
                reminder.setReminderTime(accessoryChanged.getReminderTime());
                String reminderCode = accessoryToReminderCodeMap.get(accessoryChanged.getAccessoryType().getType());
                if(StringUtils.isBlank(reminderCode)){
                    throw new T1DBuddyException(ErrorCode.INVALID_REMINDER_TYPE);
                }
                ReminderType reminderType = new ReminderType();
                reminderType.setCode(reminderCode);
                reminder.setReminderType(reminderType);
                reminderService.createReminder(email, reminder);
            }catch(Exception ex){
                log.error("Error while creating the reminder: " + ex.getMessage());
            }
        }

        log.info("Added accessory log for the user: " + email);

    }

    @Transactional
    public List<AccessoryResult> getAccessoryResult(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        return getAccessoryResult(user, startDate, endDate);

    }

    @Transactional
    public List<AccessoryResult> getAccessoryResult(User user, ZonedDateTime startDate, ZonedDateTime endDate){

        return customLogRepository.findAllAccessoryResult(user.getEmail(), startDate, endDate);

    }

    @Transactional
    public void deleteAccessoryChange(User user, UserLogReading logReading){

        if(userRepository.findAccessoryLogCount(user.getEmail(), logReading.getLogId(), logReading.getLogTime()) != 1){
            throw new T1DBuddyException(ErrorCode.INVALID_LOG);
        }

        userRepository.deleteAccessoryChange(user.getEmail(), logReading.getLogId(), logReading.getLogTime());

        log.info("Deleted accessory log id:" + logReading.getLogId() + " for the user: " + user.getEmail());
    }
}
