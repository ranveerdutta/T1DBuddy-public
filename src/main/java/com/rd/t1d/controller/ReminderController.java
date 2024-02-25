package com.rd.t1d.controller;

import com.rd.t1d.data.entity.node.ReminderType;
import com.rd.t1d.data.entity.relations.Reminder;
import com.rd.t1d.service.NotificationService;
import com.rd.t1d.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/reminder-type")
    public List<ReminderType> getReminderTypeList(){
        return reminderService.getReminderTypeList();
    }

    @PostMapping("/user/{email}/reminder")
    public ResponseEntity createReminder(@PathVariable("email") String email, @RequestBody Reminder reminder){
        reminderService.createReminder(email, reminder);
        return new ResponseEntity<>("Reminder created successfully", HttpStatus.OK);
    }

    @PutMapping("/user/{email}/reminder/{id}/status/{status}")
    public ResponseEntity updateReminderStatus(@PathVariable("email") String email, @PathVariable("id") Long reminderId, @PathVariable("status") String status){
        reminderService.updateReminderStatus(email, reminderId, status);
        return new ResponseEntity<>("Reminder status updated successfully", HttpStatus.OK);
    }

    @GetMapping("/user/{email}/reminder/pending")
    public List<Reminder> getPendingReminders(@PathVariable("email") String email){
        return reminderService.getPendingReminders(email);
    }

    @PostMapping("/push-notification")
    public ResponseEntity sendNotifications(){
        notificationService.sendNotifications();
        return new ResponseEntity<>("Sent notifications successfully", HttpStatus.OK);
    }
}
