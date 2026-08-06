package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val avatarIndex: Int = 0, // 0: Bear, 1: Cat, 2: Bunny, 3: Dog, 4: Tiger
    val age: Int = 5,
    val starsCount: Int = 0,
    val isSyncEnabled: Boolean = true,
    val lastSyncTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "lesson_progress", primaryKeys = ["profileId", "lessonId"])
data class LessonProgressEntity(
    val profileId: Long,
    val lessonId: String,
    val isCompleted: Boolean = false,
    val starsEarned: Int = 0,
    val totalAttempts: Int = 0,
    val lastStudiedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_logs")
data class StudyLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long,
    val dateString: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int = 0,
    val wordsLearned: Int = 0,
    val quizCorrectCount: Int = 0,
    val quizTotalCount: Int = 0,
    val weakItemsListJson: String = "" // e.g. "tr,ch,dấu ngã"
)

@Entity(tableName = "unlocked_stickers", primaryKeys = ["profileId", "stickerId"])
data class UnlockedStickerEntity(
    val profileId: Long,
    val stickerId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)
