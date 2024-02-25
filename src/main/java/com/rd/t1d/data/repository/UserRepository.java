package com.rd.t1d.data.repository;

import com.rd.t1d.data.entity.node.StoredFile;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.AccessoryResult;
import com.rd.t1d.data.entity.projection.BgReadingResult;
import com.rd.t1d.data.entity.projection.ExerciseResult;
import com.rd.t1d.data.entity.projection.InsulinDoseResult;
import com.rd.t1d.enums.BgReadingSource;
import com.rd.t1d.enums.InsulinDoseType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    User findByEmail(@Param("email") String email);

    @Query("MATCH (m:User) where m.email = $email RETURN m")
    User findUserObjectByEmail(String email);

    @Query("MATCH (m:User) where m.avatar_name = $avatarName and m.email <> $email RETURN m")
    User findUserObjectByAvatarName(String email, String avatarName);

    @Query("MATCH (m:User) where m.api_secret = $apiSecret RETURN m")
    User findUserObjectByApiSecret(String apiSecret);

    @Query("MATCH (m:User) where m.sha1_secret = $sha1Secret RETURN m")
    User findUserObjectBySha1Secret(String sha1Secret);

    @Query("MATCH (m:User) where m.email = $email set m.api_secret = $apiSecret, m.sha1_secret = $sha1Secret, m.updated_at = $updatedAt")
    void updateApiSecret(String email, String apiSecret, String sha1Secret, ZonedDateTime updatedAt);

    @Query("MATCH (m:User) where m.fcm_token = $fcmToken RETURN m")
    User findUserObjectByFcmToken(String fcmToken);

    @Query("MATCH (m:User) where m.email = $email set m.fcm_token = $fcmToken, m.updated_at = $updatedAt")
    void updateFcmToken(String email, String fcmToken, ZonedDateTime updatedAt);

    @Query("MATCH (m:User) where m.email = $email remove m.fcm_token set m.updated_at = $updatedAt")
    void removeFcmToken(String email, ZonedDateTime updatedAt);

    @Query("MATCH (m:User)-[r:BG_READING]->(n:Bg_Level) where m.email = $email and r.measured_at = datetime($logTime) and ID(r) = $logId return count(r)")
    int findBgReadingCount(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:BG_READING]->(n:Bg_Level) where m.email = $email and r.measured_at = datetime($logTime) and ID(r) = $logId DELETE r")
    void deleteBgReading(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:INSULIN_TAKEN]->(n:Insulin_Dose) where m.email = $email and r.taken_at = datetime($logTime) and ID(r) = $logId return count(r)")
    int findInsulinDoseCount(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:INSULIN_TAKEN]->(n:Insulin_Dose) where m.email = $email and r.taken_at = datetime($logTime) and ID(r) = $logId DELETE r")
    void deleteInsulinDose(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:EXERCISE_DONE]->(n:Exercise_Type) where m.email = $email and r.done_at = datetime($logTime) and ID(r) = $logId return count(r)")
    int findExerciseLogCount(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:EXERCISE_DONE]->(n:Exercise_Type) where m.email = $email and r.done_at = datetime($logTime) and ID(r) = $logId DELETE r")
    void deleteExerciseDone(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:ACCESSORY_CHANGED]->(n:Accessory_Type) where m.email = $email and r.changed_at = datetime($logTime) and ID(r) = $logId return count(r)")
    int findAccessoryLogCount(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (m:User)-[r:ACCESSORY_CHANGED]->(n:Accessory_Type) where m.email = $email and r.changed_at = datetime($logTime) and ID(r) = $logId DELETE r")
    void deleteAccessoryChange(String email, Long logId, ZonedDateTime logTime);

    @Query("MATCH (a:User), (b:Stored_File) WHERE ID(a) = $userId AND ID(b) = $storedFileId CREATE (a)-[r:PROFILE_PHOTO]->(b)")
    void createProfilePhoto(Long userId, Long storedFileId);

    @Query("MATCH (a:User)-[r:PROFILE_PHOTO]->(b:Stored_File) WHERE ID(a) = $userId AND ID(b) = $storedFileId DELETE r")
    void deleteProfilePhoto(Long userId, Long storedFileId);

    @Query("MATCH (a:User), (b:Tag) WHERE ID(a) = $userId AND ID(b) = $tagId CREATE (a)-[r:TAG_CREATED]->(b)")
    void createTag(Long userId, Long tagId);

    @Query("MATCH (a:User), (b:Bg_Level) WHERE ID(a) = $userId AND ID(b) = $bgLevelId CREATE (a)-[r:BG_READING {measured_at: $measuredAt, bg_reading_source: $readingSource}]->(b)")
    void createBgReading(Long userId, Long bgLevelId, ZonedDateTime measuredAt, BgReadingSource readingSource);

    @Query("MATCH (a:User), (b:Bg_Level) WHERE ID(a) = $userId AND ID(b) = $bgLevelId CREATE (a)-[r:BG_READING {measured_at: $measuredAt, bg_reading_source: $readingSource, raw_bg: $rawBg}]->(b)")
    void createBgReading(Long userId, Long bgLevelId, ZonedDateTime measuredAt, BgReadingSource readingSource, Double rawBg);

    @Query("MATCH (a:User), (b:Accessory_Type) WHERE ID(a) = $userId AND ID(b) = $accessoryTypeId CREATE (a)-[r:ACCESSORY_CHANGED {changed_at: $changedAt, reminder_time: $reminderTime}]->(b)")
    void createAccessoryChanged(Long userId, Long accessoryTypeId, ZonedDateTime changedAt, ZonedDateTime reminderTime);

    @Query("MATCH (a:User), (b:Exercise_Type) WHERE ID(a) = $userId AND ID(b) = $exerciseTypeId CREATE (a)-[r:EXERCISE_DONE {done_at: $doneAt, duration_in_minutes: $durationInMinutes, more_details: $moreDetails}]->(b)")
    void createExerciseDone(Long userId, Long exerciseTypeId, ZonedDateTime doneAt, Long durationInMinutes, String moreDetails);

    @Query("MATCH (a:User), (b:Insulin_Dose) WHERE ID(a) = $userId AND ID(b) = $insulinDoseId CREATE (a)-[r:INSULIN_TAKEN {taken_at: $takenAt, insulin_dose_type: $insulinDoseType}]->(b)")
    void createInsulinDoseTaken(Long userId, Long insulinDoseId, ZonedDateTime takenAt, InsulinDoseType insulinDoseType);

    @Query("MATCH (a:User {email: $email}) set a.avatar_name = $avatarName, a.about_me = $aboutMe, a.pin_code = $pinCode, a.city = $city, a.state = $state, a.country = $country, " +
                    "a.injection_type = $injectionType, a.insulin_type = $insulinType, a.pump_type = $pumpType, a.glucometer_type = $glucometerType, " +
                    "a.bg_unit = $bgUnit, a.normal_bg_min = $minNormalBg, a.normal_bg_max = $maxNormalBg, a.updated_at = $updatedAt return a")
    void updateUserProfile(String email, String avatarName, String aboutMe, String pinCode, String city, String state, String country, String injectionType, String insulinType, String pumpType, String glucometerType,
                           String bgUnit, int minNormalBg, int maxNormalBg, Boolean onCgm, ZonedDateTime updatedAt);

    @Query("MATCH (m:User)-[r:FOLLOW]->(n:User) where ID(m) = $fromUserId and ID(n) = $toUserId return count(r) > 0")
    boolean followExists(Long fromUserId, Long toUserId);

    @Query("MATCH (m:User), (n:User) where ID(m) = $fromUserId and ID(n) = $toUserId CREATE (m)-[r:FOLLOW]->(n) return ID(r)")
    Long follow(Long fromUserId, Long toUserId);

    @Query("MATCH (m:User)-[r:FOLLOW]->(n:User) where ID(m) = $fromUserId and ID(n) = $toUserId delete r")
    void unfollow(Long fromUserId, Long toUserId);

}
