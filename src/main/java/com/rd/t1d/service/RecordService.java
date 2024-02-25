package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.Record;
import com.rd.t1d.data.entity.node.StoredFile;
import com.rd.t1d.data.entity.node.Tag;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.repository.RecordRepository;
import com.rd.t1d.data.repository.StoredFileRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class RecordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoredFileRepository storedFileRepository;

    @Autowired
    private RecordRepository recordRepository;

    public void addNewRecord(String email, Record record){
        if(null == record || StringUtils.isBlank(record.getFileId()) || StringUtils.isBlank(record.getDescription()) || null == record.getRecordDate()){
            throw new T1DBuddyException(ErrorCode.INVALID_RECORD_CONTENT, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findUserObjectByEmail(email);
        if(null == user) {
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        List<StoredFile> storedFileList = storedFileRepository.findByFileId(record.getFileId());
        if(null == storedFileList || storedFileList.size() == 0){
            log.error("invalid file id: " + record.getFileId() + " for the user: " + email);
            throw new T1DBuddyException(ErrorCode.INVALID_RECORD_CONTENT, HttpStatus.BAD_REQUEST);
        }
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        record.setCreatedAt(currentDateTime);
        record.setUpdatedAt(currentDateTime);

        recordRepository.save(record);
        recordRepository.createUserRecord(user.getId(), record.getId());

        log.info("Created record id:" + record.getId() + " for the user: " + user.getEmail());
    }

    public List<Record> getAllMedicalRecords(String email){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user) {
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        return recordRepository.getAllMedicalRecords(user.getId());
    }
}
