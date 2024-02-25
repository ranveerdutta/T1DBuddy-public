package com.rd.t1d.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rd.t1d.data.entity.node.DeviceInfo;
import com.rd.t1d.data.entity.node.Invitation;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.UserResult;
import com.rd.t1d.dto.InvitationOutput;
import com.rd.t1d.dto.UserProfileInput;
import com.rd.t1d.dto.UserPublicProfile;
import com.rd.t1d.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/email/{email}")
    public User getUserInfo(@PathVariable("email") String email){
        return userService.getUserInfoByEmail(email);
    }

    @GetMapping("/{email}/avatar/{avatar_name}")
    public ResponseEntity isAvatarNameAvailable(@PathVariable("email") String email, @PathVariable("avatar_name") String avatarName) throws JsonProcessingException {
        User user = userService.getUserInfoByAvatarName(email, avatarName);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        if(null == user){
            json = mapper.readTree("{\"is_available\": true}");
        }else{
            json = mapper.readTree("{\"is_available\": false}");
        }

        return ResponseEntity.ok(json);
    }

    @PutMapping("/{email}/fcm-token/{fcm-token}")
    public ResponseEntity updateFcmToken(@PathVariable("email") String email, @PathVariable("fcm-token") String fcmToken) {
        userService.updateFcmToken(email, fcmToken);
        return new ResponseEntity<>("FCM token updated", HttpStatus.OK);
    }

    @PostMapping("/invitation/{guestEmail}")
    public ResponseEntity invite(@RequestHeader("email") String hostEmail, @PathVariable("guestEmail") String guestEmail){
        userService.invite(hostEmail, guestEmail);
        return new ResponseEntity<>("Invitation sent successfully", HttpStatus.OK);
    }

    @GetMapping("/invitation/{invitation_key}/email/{email}")
    public InvitationOutput getActiveInvitation(@PathVariable("invitation_key") String invitationKey,
                                    @PathVariable("email") String email){
        Invitation invitation = userService.validateInvitation(invitationKey, email);

        InvitationOutput invitationOutput = new InvitationOutput();
        invitationOutput.setInvitationKey(invitation.getInvitationKey());
        invitationOutput.setEmail(invitation.getEmail());
        invitationOutput.setInvitedBy(invitation.getInvitedBy().getEmail());
        return invitationOutput;
    }

    @PostMapping("/register/{invitation_key}")
    public ResponseEntity register(@RequestHeader("os_version") String osVersion, @RequestHeader("brand") String brand, @RequestHeader("device") String device,
                                   @RequestHeader("model") String model, @RequestHeader("device_id") String deviceId,
                                   @PathVariable("invitation_key") String invitationKey, @RequestBody User user){
        DeviceInfo deviceInfo = new DeviceInfo(osVersion, brand, device, model, deviceId);
        deviceInfo.setDevice(device);
        userService.registerUser(invitationKey, user, deviceInfo);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping("/{email}/profile-update")
    public ResponseEntity updateProfile(@PathVariable("email") String email, @RequestBody UserProfileInput userProfileInput){
        userService.updateUserProfile(email, userProfileInput);
        return new ResponseEntity<>("User profile updated successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/{email}/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity validateLogin(@PathVariable("email") String email) {
        log.info("validating login for: " + email);
        Boolean isRegistered = userService.isRegisteredUser(email);
        Map<String, Boolean> resp = new HashMap<>();
        resp.put("is_registered", isRegistered);
        return ResponseEntity.ok(resp);

    }

    @GetMapping("/{email}/public-profile")
    public UserPublicProfile getUserPublicProfile(@PathVariable("email") String email){
        return userService.getPublicProfile(email);
    }

    @PostMapping("/{email}/profile-photo")
    public ResponseEntity uploadProfilePhoto(@PathVariable("email") String email, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + multipartFile.getOriginalFilename());
        convFile.deleteOnExit();
        multipartFile.transferTo(convFile);
        String fileId = userService.uploadProfilePhoto(email, convFile, multipartFile.getContentType());
        Map<String, String> resp = new HashMap<>();
        resp.put("file_id", fileId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{email}/follow/{to_email}")
    public ResponseEntity follow(@PathVariable("email") String fromEmail, @PathVariable("to_email") String toEmail) throws Exception {
        userService.follow(fromEmail, toEmail);
        return new ResponseEntity<>("Follow request processed successfully", HttpStatus.OK);
    }

    @PostMapping("/{email}/unfollow/{to_email}")
    public ResponseEntity unfollow(@PathVariable("email") String fromEmail, @PathVariable("to_email") String toEmail) throws Exception {
        userService.unfollow(fromEmail, toEmail);
        return new ResponseEntity<>("Unfollow request processed successfully", HttpStatus.OK);
    }

    @GetMapping("/{email}/member-list/{filter}")
    public List<UserResult> getPostHeaders(@RequestParam("created_at") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime createdAt,
                                           @RequestParam("batch_size") int batchSize, @PathVariable("email") String email, @PathVariable("filter") String filter){
        return userService.getAllMembers(email, createdAt, batchSize, filter);
    }

    @PostMapping("/{email}/api-secret")
    public ResponseEntity generateApiSecret(@PathVariable("email") String email){
        String apiSecret = userService.generateApiSecret(email);
        Map<String, String> resp = new HashMap<>();
        resp.put("api_secret", apiSecret);
        return ResponseEntity.ok(resp);
    }
}
