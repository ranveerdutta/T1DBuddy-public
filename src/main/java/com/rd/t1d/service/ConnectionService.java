package com.rd.t1d.service;

import com.rd.t1d.utils.DateUtils;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.relations.Connection;
import com.rd.t1d.data.entity.relations.ConnectionRequest;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.ConnectionInput;
import com.rd.t1d.enums.Status;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class ConnectionService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void sendConnectionRequest(ConnectionInput connectionInput){
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        User fromUser = userRepository.findByEmail(connectionInput.getFromEmail());
        if(null == fromUser){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        User toUser = userRepository.findByEmail(connectionInput.getToEmail());
        if(null == toUser){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        List<ConnectionRequest> connectionRequestedList = fromUser.getConnectionsRequested();

        for(ConnectionRequest request : connectionRequestedList){
            if(request.getTargetUser().getEmail().equals(toUser.getEmail())){
                if(Status.REQUESTED.equals(request.getStatus())){
                    throw new T1DBuddyException(ErrorCode.ALREADY_REQUESTED, HttpStatus.BAD_REQUEST);
                }
            }
        }

        ConnectionRequest request = new ConnectionRequest();
        request.setRequestedAt(currentDateTime);
        request.setCreatedAt(currentDateTime);
        request.setUpdatedAt(currentDateTime);
        request.setStatus(Status.REQUESTED);
        request.setTargetUser(toUser);
        connectionRequestedList.add(request);
        userRepository.save(fromUser);
    }

    @Transactional
    public void respondConnectionRequest(ConnectionInput connectionInput){
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        if(!(Status.ACCEPTED.equals(connectionInput.getStatus())
                || Status.REJECTED.equals(connectionInput.getStatus())
                || Status.CANCELED.equals(connectionInput.getStatus()))){
            throw new T1DBuddyException(ErrorCode.WRONG_STATUS, HttpStatus.BAD_REQUEST);
        }
        User fromUser = userRepository.findByEmail(connectionInput.getFromEmail());
        if(null == fromUser){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        User toUser = userRepository.findByEmail(connectionInput.getToEmail());
        if(null == toUser){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }
        List<ConnectionRequest> connectionRequestedList = null;
        if(Status.CANCELED.equals(connectionInput.getStatus())){
            connectionRequestedList = fromUser.getConnectionsRequested();
        }else{
            connectionRequestedList = toUser.getConnectionsRequested();
        }

        boolean pendingRequestFound = false;
        for(ConnectionRequest request : connectionRequestedList){
            if(request.getTargetUser().getEmail().equals(fromUser.getEmail())){
                if(Status.REQUESTED.equals(request.getStatus())){
                    request.setStatus(connectionInput.getStatus());
                    request.setUpdatedAt(currentDateTime);
                    userRepository.save(toUser);
                    pendingRequestFound = true;
                }
            }
        }

        if(!pendingRequestFound){
            throw new T1DBuddyException(ErrorCode.NO_PENDING_REQUEST_FOUND, HttpStatus.BAD_REQUEST);
        }

        if(Status.ACCEPTED.equals(connectionInput.getStatus())){
            Connection fromConnection = new Connection();
            fromConnection.setConnectedAt(DateUtils.getCurrentZonedDateTime());
            fromConnection.setCreatedAt(DateUtils.getCurrentZonedDateTime());
            fromConnection.setUpdatedAt(DateUtils.getCurrentZonedDateTime());
            fromConnection.setTargetUser(fromUser);
            toUser.getConnections().add(fromConnection);

            userRepository.save(toUser);

        }
    }
}
