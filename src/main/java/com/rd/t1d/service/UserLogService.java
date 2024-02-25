package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.AccessoryResult;
import com.rd.t1d.data.entity.projection.BgReadingResult;
import com.rd.t1d.data.entity.projection.ExerciseResult;
import com.rd.t1d.data.entity.projection.InsulinDoseResult;
import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.UserLogDetails;
import com.rd.t1d.dto.UserLogReading;
import com.rd.t1d.enums.UserLogType;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class UserLogService {

    @Autowired
    private BgReadingService bgReadingService;

    @Autowired
    private InsulinDoseService insulinDoseService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private AccessoryService accessoryService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserLogDetails getUserLogData(String email, ZonedDateTime startDate, ZonedDateTime endDate){

        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        UserLogDetails output = new UserLogDetails();

        List<BgReadingResult> bgReadingList = bgReadingService.getBgReading(user, startDate, endDate);
        output.setBgReadingList(bgReadingList);

        List<InsulinDoseResult> insulinDoseList = insulinDoseService.getInsulinDoses(user, startDate, endDate);
        output.setInsulinDoseList(insulinDoseList);

        List<BasalRate> basalRateList = insulinDoseService.getBasalRateList(user, startDate, endDate);
        output.setBasalRateList(basalRateList);

        List<ExerciseResult> exerciseResults = exerciseService.getExerciseResult(user, startDate, endDate);
        output.setExerciseList(exerciseResults);

        List<AccessoryResult> accessoryResults = accessoryService.getAccessoryResult(user, startDate, endDate);
        output.setAccessoryList(accessoryResults);

        output.generateStats(user);

        return output;
    }

    @Transactional
    public void deleteUserLogReading(String email, UserLogReading logReading){
        log.info("deleting logid: " + logReading.getLogId() + " for user: " + email);
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        if(UserLogType.BG.equals(logReading.getLogType())){
            bgReadingService.deleteBgReading(user, logReading);
        }else if(UserLogType.INSULIN.equals(logReading.getLogType())){
            insulinDoseService.deleteInsulinDose(user, logReading);
        }else if(UserLogType.EXERCISE.equals(logReading.getLogType())){
            exerciseService.deleteExerciseLog(user, logReading);
        }else if(UserLogType.ACCESSORY_CHANGE.equals(logReading.getLogType())){
            accessoryService.deleteAccessoryChange(user, logReading);
        }else{
            throw new T1DBuddyException(ErrorCode.INVALID_LOG_TYPE);
        }
    }


}
