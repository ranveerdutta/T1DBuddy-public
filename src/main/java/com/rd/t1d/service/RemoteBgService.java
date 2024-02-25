package com.rd.t1d.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rd.t1d.data.entity.node.BgLevel;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.relations.BgReading;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.dto.RemoteBgInput;
import com.rd.t1d.enums.BgReadingSource;
import com.rd.t1d.enums.BgUnit;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RemoteBgService {

    private final static DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BgReadingService bgReadingService;

    @Transactional
    public void addEntries(Object inputJson, String apiSecret){
        User user = userRepository.findUserObjectBySha1Secret(apiSecret);
        if(null == user){
            log.error("Received entries for the wrong api secret: " + apiSecret);
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }
        log.info("Received entries for the user: " + user.getEmail());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String inputStr = gson.toJson(inputJson);
        log.info("entries: " + inputStr);

        TypeToken<List<RemoteBgInput>> token = new TypeToken<List<RemoteBgInput>>() {};
        List<RemoteBgInput> remoteBgInputList = deduplicateRemoteBgInput(gson.fromJson(inputStr, token.getType()));
        persistBgEntries(user, remoteBgInputList);
    }

    private List<RemoteBgInput> deduplicateRemoteBgInput(List<RemoteBgInput> remoteBgInputList){
        Map<Long, RemoteBgInput> output = new HashMap<>();
        remoteBgInputList.forEach(n -> {
            if(DateUtils.dayDiffFromToday(n.getDate()) <= 7){
                output.put(n.getDate(), n);
            }
        });

        return new ArrayList<>(output.values());
    }

    private void persistBgEntries(User user, List<RemoteBgInput> remoteBgInputList){
        remoteBgInputList.forEach(n -> {
            BgReading bgReading = new BgReading();
            bgReading.setReadingSource(BgReadingSource.CGM);
            bgReading.setMeasuredAt(ZonedDateTime.parse(n.getDateString(), formatter));
            bgReading.setRawBg(n.getRawbg());
            bgReading.setBgLevel(new BgLevel(n.getSgv(), BgUnit.MGDL));
            bgReadingService.addBgReading(user, bgReading);
        });
    }
}
