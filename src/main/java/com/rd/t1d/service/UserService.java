package com.rd.t1d.service;

import com.rd.t1d.data.entity.node.DeviceInfo;
import com.rd.t1d.data.entity.node.Invitation;
import com.rd.t1d.data.entity.node.StoredFile;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.UserResult;
import com.rd.t1d.data.repository.*;
import com.rd.t1d.dto.UserProfileInput;
import com.rd.t1d.dto.UserPublicProfile;
import com.rd.t1d.enums.InjectionType;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private StoredFileRepository storedFileRepository;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private InsulinDoseRepository insulinDoseRepository;
    
    @Autowired
    private NotificationService notificationService;

    public User getUserInfoByEmail(String email) {
        return userRepository.findUserObjectByEmail(email);
    }

    public User getUserInfoByAvatarName(String email, String avatarName) {
        return userRepository.findUserObjectByAvatarName(email, avatarName);
    }

    @Transactional
    public void invite(String hostEmail, String guestEmail) {
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();
        User user = userRepository.findByEmail(hostEmail);

        Invitation existingInvitation = invitationRepository.findByEmail(guestEmail, user);

        if(existingInvitation != null) throw new T1DBuddyException(ErrorCode.ALREADY_INVITED, HttpStatus.BAD_REQUEST);

        Invitation invitation = new Invitation();
        invitation.setInvitedBy(user);
        invitation.setInvitationKey(UUID.randomUUID().toString());
        invitation.setEmail(guestEmail);
        invitation.setCreatedAt(currentDateTime);
        invitation.setUpdatedAt(currentDateTime);
        invitationRepository.save(invitation);
    }

    public Invitation validateInvitation(String invitationKey, String email) {
        Invitation invitation = invitationRepository.findByInvitationKeyAndEmail(invitationKey, email);

        if(null == invitation){
            throw new T1DBuddyException(ErrorCode.WRONG_INVITATION_KEY, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findUserObjectByEmail(email);
        if(user != null){
            throw new T1DBuddyException(ErrorCode.ALREADY_REGISTERED);
        }
        return invitation;
    }



    @Transactional
    public void registerUser(String invitationKey, User user, DeviceInfo deviceInfo){
        ZonedDateTime currentDateTime = DateUtils.getCurrentZonedDateTime();

        if(!"t1d-buddy".equalsIgnoreCase(invitationKey)){
            Invitation invitation = invitationRepository.findByInvitationKey(invitationKey);

            if(null == invitation){
                log.error("wrong invitation key: " + invitationKey);
                throw new T1DBuddyException(ErrorCode.WRONG_INVITATION_KEY, HttpStatus.BAD_REQUEST);
            }

            if(!invitation.getEmail().equals(user.getEmail())){
                throw new T1DBuddyException(ErrorCode.WRONG_EMAIL, HttpStatus.BAD_REQUEST);
            }
        }


        User existingUser = userRepository.findUserObjectByEmail(user.getEmail());
        if(existingUser != null){
            throw new T1DBuddyException(ErrorCode.ALREADY_REGISTERED, HttpStatus.BAD_REQUEST);
        }

        user.setPumpType(user.getPumpType() == null ? null: user.getPumpType().toUpperCase());
        user.setGlucometerType(user.getGlucometerType() == null ? null: user.getGlucometerType().toUpperCase());
        user.setCreatedAt(currentDateTime);
        user.setUpdatedAt(currentDateTime);

        user.setDeviceInfo(deviceInfo);
        userRepository.save(user);
    }

    public Boolean isRegisteredUser(String email) {
        User user = userRepository.findUserObjectByEmail(email);

        return user != null;
    }

    public UserPublicProfile getPublicProfile(String email){

        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        StoredFile profilePhoto = storedFileRepository.findProfilePhoto(user.getId());

        return UserPublicProfile.createPublicProfile(user, profilePhoto);
    }

    @Transactional
    public String uploadProfilePhoto(String email, File file, String contentType) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }
        StoredFile profilePhoto = storedFileRepository.findProfilePhoto(user.getId());
        //delete existing profile photo from DB
        if(profilePhoto != null){
            userRepository.deleteProfilePhoto(user.getId(), profilePhoto.getId());
        }

        StoredFile storedFile = fileStorageService.uploadFile(file, user.getEmail(), contentType, FileStorageService.IMAGE_FILE_TYPE);

        userRepository.createProfilePhoto(user.getId(), storedFile.getId());

        return storedFile.getFileId();
    }

    @Transactional
    public void updateUserProfile(String email, UserProfileInput userProfileInput) {
        User existingUser = userRepository.findUserObjectByEmail(userProfileInput.getEmail());
        if(null == existingUser){
            throw new T1DBuddyException(ErrorCode.INVALID_USER);
        }

        //if userProfileInput selects MDI then remove if any Basal Rate available
        if(InjectionType.MDI.equals(userProfileInput.getInjectionType())){
            int count = insulinDoseRepository.disableAllBasalRate(existingUser.getId(), ZonedDateTime.now());
            log.info(count + " basal rate deleted");
        }

        //process profile photo
        StoredFile profilePhoto = storedFileRepository.findProfilePhoto(existingUser.getId());
        if((profilePhoto != null && userProfileInput.getProfilePhotoId() != null && !profilePhoto.getFileId().equals(userProfileInput.getProfilePhotoId())
                || (profilePhoto == null && userProfileInput.getProfilePhotoId() != null))){

            if(profilePhoto != null){
                userRepository.deleteProfilePhoto(existingUser.getId(), profilePhoto.getId());
            }

            List<StoredFile> storedFile = storedFileRepository.findByFileId(userProfileInput.getProfilePhotoId());
            if(storedFile != null && storedFile.size() > 0){
                userRepository.createProfilePhoto(existingUser.getId(), storedFile.get(0).getId());
            }

        }




        userRepository.updateUserProfile(email, userProfileInput.getAvatarName(), userProfileInput.getAboutMe(), userProfileInput.getPinCode(), userProfileInput.getCity(), userProfileInput.getState(), userProfileInput.getCountry(),
                userProfileInput.getInjectionType().name(), userProfileInput.getInsulinType().name(), userProfileInput.getPumpType(), userProfileInput.getGlucometerType(), userProfileInput.getBgUnit().name(), userProfileInput.getNormalBgMin(),
                userProfileInput.getNormalBgMax(), userProfileInput.getOnCgm(), ZonedDateTime.now());
    }

    @Transactional
    public void follow(String fromEmail, String toEmail){

        User fromUser = userRepository.findUserObjectByEmail(fromEmail);
        if(null == fromUser){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        User toUser = userRepository.findUserObjectByEmail(toEmail);
        if(null == toUser){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        boolean doesFollowExists = userRepository.followExists(fromUser.getId(), toUser.getId());

        if(doesFollowExists){
            throw new T1DBuddyException(ErrorCode.ALREADY_FOLLOWING, HttpStatus.BAD_REQUEST);
        }

        Long followId = userRepository.follow(fromUser.getId(), toUser.getId());

        notificationService.createFollowerNotification(followId, toUser, fromUser.getFirstName());
    }

    @Transactional
    public void unfollow(String fromEmail, String toEmail){

        User fromUser = userRepository.findUserObjectByEmail(fromEmail);
        if(null == fromUser){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        User toUser = userRepository.findUserObjectByEmail(toEmail);
        if(null == toUser){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        boolean doesFollowExists = userRepository.followExists(fromUser.getId(), toUser.getId());

        if(!doesFollowExists){
            throw new T1DBuddyException(ErrorCode.NOT_FOLLOWING, HttpStatus.BAD_REQUEST);
        }

        userRepository.unfollow(fromUser.getId(), toUser.getId());

    }

    @Transactional
    public List<UserResult> getAllMembers(String email, ZonedDateTime createdAt, int batchSize, String filter){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        if(null == createdAt) createdAt = DateUtils.getCurrentZonedDateTime();
        if("following".equalsIgnoreCase(filter)){
            return customUserRepository.findFollowingUsers(email, createdAt, batchSize);
        }else if("follower".equalsIgnoreCase(filter)){
            return customUserRepository.findFollowerUsers(email, createdAt, batchSize);
        }else if("all".equalsIgnoreCase(filter)){
            return customUserRepository.findAllUsers(email, createdAt, batchSize);
        }else{
            throw new T1DBuddyException(ErrorCode.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public String generateApiSecret(String email){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isNotBlank(user.getApiSecret())){
            //log.info("api secret is already available for the user: " + email);
            return user.getApiSecret();
        }
        String apiSecret = UUID.randomUUID().toString();

        String sha1Secret = DigestUtils.sha1Hex(apiSecret);

        userRepository.updateApiSecret(email, apiSecret, sha1Secret, ZonedDateTime.now());

        return apiSecret;
    }

    @Transactional
    public void updateFcmToken(String email, String fcmToken){
        User user = userRepository.findUserObjectByEmail(email);
        if(null == user){
            throw new T1DBuddyException(ErrorCode.INVALID_USER, HttpStatus.BAD_REQUEST);
        }

        log.info("updating FCM token for the user:" + email + " with token: " + fcmToken);

        User otherUser = userRepository.findUserObjectByFcmToken(fcmToken);
        if(otherUser != null && otherUser.getEmail()!=email){
            userRepository.removeFcmToken(otherUser.getEmail(), ZonedDateTime.now());
        }
        userRepository.updateFcmToken(email, fcmToken, ZonedDateTime.now());
    }

    public User getUserByApiSecret(String apiSecret){
        return userRepository.findUserObjectByApiSecret(apiSecret);
    }

    public User getUserBySha1Secret(String apiSecret){
        return userRepository.findUserObjectBySha1Secret(apiSecret);
    }
}
