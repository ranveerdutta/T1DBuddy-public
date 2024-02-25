package com.rd.t1d.service;

import com.rd.t1d.utils.DateUtils;
import com.rd.t1d.data.entity.node.Food;
import com.rd.t1d.data.entity.node.FoodContent;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.relations.FoodIncludes;
import com.rd.t1d.data.repository.FoodContentRepository;
import com.rd.t1d.data.repository.UserRepository;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.ZonedDateTime;

@Service
public class FoodService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodContentRepository foodContentRepository;

    @Transactional
    public void addFood(String email, @RequestBody Food foodTaken){
        User user = userRepository.findUserObjectByEmail(email);
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();

        if(null == user){
            throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
        }

        if(null == foodTaken.getEatenAt()){
            foodTaken.setEatenAt(currentDateTime);
        }

        for(FoodIncludes foodIncludes : foodTaken.getIncludedContents()){
            String content = StringUtils.convertToUnderscoreString(foodIncludes.getFoodContent().getContent());
            FoodContent foodContent = foodContentRepository.findByContent(content);
            if(foodContent != null){
                foodIncludes.setFoodContent(foodContent);
            }else{
                foodContent  = foodIncludes.getFoodContent();
                foodContent.setCreatedAt(currentDateTime);
                foodContent.setContent(content);
            }
        }



        user.getFoodList().add(foodTaken);

        userRepository.save(user);
    }
}
