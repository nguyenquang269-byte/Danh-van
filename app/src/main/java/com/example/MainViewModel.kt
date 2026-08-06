package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserEntity
import com.example.data.model.GameType
import com.example.data.model.Lesson
import com.example.data.model.QuizQuestion
import com.example.data.model.Sticker3D
import com.example.data.repository.VietnameseSpellingRepository
import com.example.speech.VietnameseSpeechEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = VietnameseSpellingRepository(db.appDao())
    val speechEngine = VietnameseSpeechEngine(application)

    val allProfiles: StateFlow<List<UserEntity>> = repository.allProfiles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _activeProfile = MutableStateFlow<UserEntity?>(null)
    val activeProfile: StateFlow<UserEntity?> = _activeProfile

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons

    val allStickers: List<Sticker3D> = repository.getAllStickers3D()

    private val _unlockedStickerIds = MutableStateFlow<List<String>>(emptyList())
    val unlockedStickerIds: StateFlow<List<String>> = _unlockedStickerIds

    init {
        viewModelScope.launch {
            repository.ensureDefaultUser()
            _lessons.value = repository.getLessons()
            allProfiles.collect { list ->
                if (_activeProfile.value == null && list.isNotEmpty()) {
                    val p = list.first()
                    _activeProfile.value = p
                    observeUnlockedStickers(p.id)
                }
            }
        }
    }

    private fun observeUnlockedStickers(profileId: Long) {
        viewModelScope.launch {
            repository.getUnlockedStickersForProfile(profileId).collect { ids ->
                _unlockedStickerIds.value = ids
            }
        }
    }

    fun selectProfile(profile: UserEntity) {
        _activeProfile.value = profile
        observeUnlockedStickers(profile.id)
    }

    fun createProfile(name: String, age: Int) {
        viewModelScope.launch {
            val newId = repository.createNewProfile(name, 0, age)
            val profile = db.appDao().getProfileById(newId)
            if (profile != null) {
                _activeProfile.value = profile
                observeUnlockedStickers(newId)
            }
        }
    }

    fun redeemSticker(sticker: Sticker3D, onResult: (Boolean, String) -> Unit) {
        val current = _activeProfile.value ?: return
        viewModelScope.launch {
            val success = repository.redeemSticker(current.id, sticker.id, sticker.costStars)
            if (success) {
                val updated = db.appDao().getProfileById(current.id)
                if (updated != null) _activeProfile.value = updated
                onResult(true, "Bé đã đổi thành công ${sticker.name}!")
            } else {
                val needed = sticker.costStars - current.starsCount
                onResult(false, "Bé cần tích lũy thêm $needed ngôi sao nữa nhé!")
            }
        }
    }

    fun recordLessonStars(lessonId: String, stars: Int) {
        val current = _activeProfile.value ?: return
        viewModelScope.launch {
            repository.saveProgress(current.id, lessonId, stars)
            // Refresh profile
            val updated = db.appDao().getProfileById(current.id)
            if (updated != null) {
                _activeProfile.value = updated
            }
        }
    }

    fun getQuizQuestionsForGame(gameType: GameType): List<QuizQuestion> {
        return repository.getQuizQuestions(gameType)
    }

    fun speakText(text: String) {
        speechEngine.speakText(text)
    }

    override fun onCleared() {
        super.onCleared()
        speechEngine.shutdown()
    }
}
