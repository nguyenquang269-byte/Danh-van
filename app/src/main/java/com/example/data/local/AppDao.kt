package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // User Profiles
    @Query("SELECT * FROM user_profiles ORDER BY id ASC")
    fun getAllProfiles(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserEntity): Long

    @Update
    suspend fun updateProfile(profile: UserEntity)

    @Query("UPDATE user_profiles SET starsCount = starsCount + :addStars WHERE id = :profileId")
    suspend fun addStarsToProfile(profileId: Long, addStars: Int)

    // Lesson Progress
    @Query("SELECT * FROM lesson_progress WHERE profileId = :profileId")
    fun getProgressForProfile(profileId: Long): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE profileId = :profileId AND lessonId = :lessonId LIMIT 1")
    suspend fun getLessonProgress(profileId: Long, lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLessonProgress(progress: LessonProgressEntity)

    // Study Logs
    @Query("SELECT * FROM study_logs WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT 30")
    fun getStudyLogsForProfile(profileId: Long): Flow<List<StudyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyLog(log: StudyLogEntity)

    // Unlocked 3D Stickers
    @Query("SELECT stickerId FROM unlocked_stickers WHERE profileId = :profileId")
    fun getUnlockedStickersForProfile(profileId: Long): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun unlockSticker(sticker: UnlockedStickerEntity)
}
