package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.BgLevel;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.BgReadingResult;
import com.rd.t1d.data.entity.relations.BgReading;
import com.rd.t1d.data.repository.BgLevelRepository;
import com.rd.t1d.data.repository.CustomLogRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.UserLogReading;
import com.rd.t1d.enums.BgUnit;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class BgReadingService {

    private static BigDecimal BG_UNIT_FACTOR = new BigDecimal(18.0);

    @Autowired
    private BgLevelRepository bgLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomLogRepository customLogRepository;

    @Transactional
    public void addBgReading(String email, BgReading bgReading){
        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        addBgReading(user, bgReading);

    }

    public void addBgReading(User user, BgReading bgReading){

        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();

        if(null == bgReading.getMeasuredAt()){
            bgReading.setMeasuredAt(currentDateTime);
        }

        BgLevel bgLevel = bgLevelRepository.findByBgNumberAndBgUnit(bgReading.getBgLevel().getBgNumber(),
                bgReading.getBgLevel().getBgUnit());

        if(null == bgLevel){
            bgLevel = new BgLevel();
            bgLevel.setBgUnit(bgReading.getBgLevel().getBgUnit());
            bgLevel.setBgNumber(bgReading.getBgLevel().getBgNumber());
            bgLevel.setCreatedAt(currentDateTime);
            bgLevel.setUpdatedAt(currentDateTime);
            bgLevelRepository.save(bgLevel);
        }

        if(null == bgReading.getRawBg()){
            userRepository.createBgReading(user.getId(), bgLevel.getId(), bgReading.getMeasuredAt(), bgReading.getReadingSource());
        }else{
            userRepository.createBgReading(user.getId(), bgLevel.getId(), bgReading.getMeasuredAt(), bgReading.getReadingSource(), bgReading.getRawBg());
        }



        log.info("Added BG log for the user: " + user.getEmail());

    }

    @Transactional
    public List<BgReadingResult> getBgReading(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        User user = userRepository.findUserObjectByEmail(email);
        return getBgReading(user, startDate, endDate);
    }

    @Transactional
    public List<BgReadingResult> getBgReading(User user, ZonedDateTime startDate, ZonedDateTime endDate){

        List<BgReadingResult> result = customLogRepository.findAllBgReadingResult(user.getEmail(), startDate, endDate);

        result.forEach(reading -> {
            if(BgUnit.MGDL.equals(user.getBgUnit())
                    && BgUnit.MMOL.equals(reading.getBgUnit())){
                reading.setBgUnit(BgUnit.MGDL);
                BigDecimal temp = new BigDecimal(reading.getBgNumber());
                reading.setBgNumber(temp.multiply(BG_UNIT_FACTOR).setScale(0, RoundingMode.HALF_UP).doubleValue());
                if(reading.getRawBg() != null){
                    BigDecimal temp1 = new BigDecimal(reading.getRawBg());
                    reading.setRawBg(temp1.multiply(BG_UNIT_FACTOR).setScale(0, RoundingMode.HALF_UP).doubleValue());
                }
            }
            else if(BgUnit.MMOL.equals(user.getBgUnit())
                    && BgUnit.MGDL.equals(reading.getBgUnit())){
                reading.setBgUnit(BgUnit.MMOL);
                BigDecimal temp = new BigDecimal(reading.getBgNumber());
                reading.setBgNumber(temp.divide(BG_UNIT_FACTOR, 1, RoundingMode.HALF_UP).doubleValue());
                if(reading.getRawBg() != null){
                    BigDecimal temp1 = new BigDecimal(reading.getRawBg());
                    reading.setRawBg(temp1.divide(BG_UNIT_FACTOR, 1, RoundingMode.HALF_UP).doubleValue());
                }
            }
        });

        return result;
    }

    @Transactional
    public void deleteBgReading(User user, UserLogReading logReading){

        if(userRepository.findBgReadingCount(user.getEmail(), logReading.getLogId(), logReading.getLogTime()) != 1){
            throw new T1DBuddyException(ErrorCode.INVALID_LOG);
        }

        userRepository.deleteBgReading(user.getEmail(), logReading.getLogId(), logReading.getLogTime());

        log.info("Deleted BG log id:" + logReading.getLogId() + " for the user: " + user.getEmail());
    }
}
