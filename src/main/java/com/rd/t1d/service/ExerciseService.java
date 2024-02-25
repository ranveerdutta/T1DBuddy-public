package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.ExerciseType;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.ExerciseResult;
import com.rd.t1d.data.entity.relations.ExerciseDone;
import com.rd.t1d.data.repository.CustomLogRepository;
import com.rd.t1d.data.repository.ExerciseTypeRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.UserLogReading;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class ExerciseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private CustomLogRepository customLogRepository;

    @Transactional
    public void addExercise(String email, ExerciseDone exercise){

        User user = userRepository.findUserObjectByEmail(email);

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();

        if(null == exercise.getDoneAt()){
            exercise.setDoneAt(currentDateTime);
        }

        ExerciseType exerciseType = exerciseTypeRepository.findByType(exercise.getExerciseType().getType());

        if(null == exerciseType){
            exerciseType = exercise.getExerciseType();
            exerciseType.setCreatedAt(currentDateTime);
            exerciseType.setUpdatedAt(currentDateTime);
            exerciseTypeRepository.save(exerciseType);
        }

        userRepository.createExerciseDone(user.getId(), exerciseType.getId(), exercise.getDoneAt(), exercise.getDurationInMinutes(), exercise.getMoreDetails());

        log.info("Added exercise log for the user: " + email);

    }

    @Transactional
    public List<ExerciseResult> getExerciseResult(String email, ZonedDateTime startDate, ZonedDateTime endDate){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        return getExerciseResult(user, startDate, endDate);

    }

    @Transactional
    public List<ExerciseResult> getExerciseResult(User user, ZonedDateTime startDate, ZonedDateTime endDate){

        return customLogRepository.findAllExerciseResult(user.getEmail(), startDate, endDate);

    }

    @Transactional
    public void deleteExerciseLog(User user, UserLogReading logReading){

        if(userRepository.findExerciseLogCount(user.getEmail(), logReading.getLogId(), logReading.getLogTime()) != 1){
            throw new T1DBuddyException(ErrorCode.INVALID_LOG);
        }

        userRepository.deleteExerciseDone(user.getEmail(), logReading.getLogId(), logReading.getLogTime());

        log.info("Deleted exercise log id:" + logReading.getLogId() + " for the user: " + user.getEmail());
    }
}
