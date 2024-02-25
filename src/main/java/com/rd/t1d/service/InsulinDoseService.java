package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.InsulinDose;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.InsulinDoseResult;
import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.data.entity.relations.InsulinDoseTaken;
import com.rd.t1d.data.repository.CustomLogRepository;
import com.rd.t1d.data.repository.InsulinDoseRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.UserLogReading;
import com.rd.t1d.enums.InjectionType;
import com.rd.t1d.enums.InsulinDoseType;
import com.rd.t1d.enums.InsulinType;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class InsulinDoseService {

    @Autowired
    private InsulinDoseRepository insulinDoseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomLogRepository customLogRepository;

    @Transactional
    public void addInsulinDose(String email, InsulinDoseTaken insulinDoseTaken){
        ZonedDateTime currentDate = DateUtils.getCurrentZonedDateTime();

        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        if(null == insulinDoseTaken.getTakenAt()){
            insulinDoseTaken.setTakenAt(currentDate);
        }

        InsulinDose insulinDose = insulinDoseRepository.findByQuantityAndInsulinType(insulinDoseTaken.getInsulinDose().getQuantity(),
                insulinDoseTaken.getInsulinDose().getInsulinType());

        if(null == insulinDose){
            insulinDose = new InsulinDose();
            insulinDose.setInsulinType(insulinDoseTaken.getInsulinDose().getInsulinType());
            insulinDose.setQuantity(insulinDoseTaken.getInsulinDose().getQuantity());
            insulinDose.setCreatedAt(currentDate);
            insulinDose.setUpdatedAt(currentDate);
            insulinDoseRepository.save(insulinDose);
        }

        userRepository.createInsulinDoseTaken(user.getId(), insulinDose.getId(), insulinDoseTaken.getTakenAt(), insulinDoseTaken.getInsulinDoseType());

        log.info("Added insulin log for the user: " + email);
    }

    @Transactional
    public void addBasalRate(String email, List<BasalRate> basalRateList){
        ZonedDateTime currentDate = DateUtils.getCurrentZonedDateTime();
        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        if(!InjectionType.INSULIN_PUMP.equals(user.getInjectionType())){
            throw new T1DBuddyException(ErrorCode.NOT_PUMP_USER, HttpStatus.BAD_REQUEST);
        }


        //disable current basal rates
        int count = insulinDoseRepository.disableAllBasalRate(user.getId(), currentDate);
        log.info(count + " basal rate removed for the user:" + email);

        for(BasalRate basalRate : basalRateList){

            if(null == basalRate.getStartDate()){
                basalRate.setStartDate(currentDate);
            }
            basalRate.setCreatedAt(currentDate);
            basalRate.setUpdatedAt(currentDate);

            if(null == basalRate.getInsulinDose().getInsulinType()){
                basalRate.getInsulinDose().setInsulinType(user.getInsulinType());
            }

            InsulinDose insulinDose = insulinDoseRepository.findByQuantityAndInsulinType(basalRate.getInsulinDose().getQuantity(),
                    basalRate.getInsulinDose().getInsulinType());

            if(null == insulinDose){
                insulinDose = new InsulinDose();
                insulinDose.setInsulinType(basalRate.getInsulinDose().getInsulinType());
                insulinDose.setQuantity(basalRate.getInsulinDose().getQuantity());
                insulinDose.setCreatedAt(DateUtils.getCurrentZonedDateTime());
                insulinDose.setUpdatedAt(DateUtils.getCurrentZonedDateTime());
                insulinDoseRepository.save(insulinDose);
            }
            int createCount = insulinDoseRepository.createBasalRate(user.getId(), insulinDose.getId(), currentDate, basalRate.getStartTime(), basalRate.getEndTime());
            if(createCount == 1){
                log.info("Basal Rate created for the user:" + email);
            }
        }

    }

    public List<BasalRate> getCurrentBasalRate(String email){
        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        List<BasalRate> outputBasalRate = customLogRepository.getActiveBasalRateList(user.getId());

        return outputBasalRate;
    }

    @Transactional
    public List<InsulinDoseResult> getInsulinDoses(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        return getInsulinDoses(user, startDate, endDate);

    }

    @Transactional
    public List<InsulinDoseResult> getInsulinDoses(User user, ZonedDateTime startDate, ZonedDateTime endDate){

        return customLogRepository.findAllInsulinDoseResult(user.getEmail(), startDate, endDate);

    }
    @Transactional
    public void deleteInsulinDose(User user, UserLogReading logReading){

        if(userRepository.findInsulinDoseCount(user.getEmail(), logReading.getLogId(), logReading.getLogTime()) != 1){
            throw new T1DBuddyException(ErrorCode.INVALID_LOG);
        }

        userRepository.deleteInsulinDose(user.getEmail(), logReading.getLogId(), logReading.getLogTime());

        log.info("Deleted insulin log id:" + logReading.getLogId() + " for the user: " + user.getEmail());
    }

    @Transactional
    public List<BasalRate> getBasalRateList(User user, ZonedDateTime startDate, ZonedDateTime endDate){

        return customLogRepository.getActiveBasalRateList(user.getId());

    }
}
