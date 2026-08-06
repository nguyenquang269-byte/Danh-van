package com.example.data.repository

import com.example.data.local.AppDao
import com.example.data.local.LessonProgressEntity
import com.example.data.local.StudyLogEntity
import com.example.data.local.UnlockedStickerEntity
import com.example.data.local.UserEntity
import com.example.data.model.AlphabetCategory
import com.example.data.model.AlphabetItem
import com.example.data.model.GameType
import com.example.data.model.Lesson
import com.example.data.model.QuizQuestion
import com.example.data.model.Sticker3D
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VietnameseSpellingRepository(private val appDao: AppDao) {

    // Default built-in child profiles
    suspend fun ensureDefaultUser() {
        val defaultProfile = UserEntity(
            id = 1,
            name = "Bé An",
            avatarIndex = 0,
            age = 5,
            starsCount = 125
        )
        appDao.insertProfile(defaultProfile)
        ensureDefaultStickers(1)
    }

    val allProfiles: Flow<List<UserEntity>> = appDao.getAllProfiles()

    fun getProgressForProfile(profileId: Long): Flow<List<LessonProgressEntity>> {
        return appDao.getProgressForProfile(profileId)
    }

    fun getStudyLogsForProfile(profileId: Long): Flow<List<StudyLogEntity>> {
        return appDao.getStudyLogsForProfile(profileId)
    }

    fun getUnlockedStickersForProfile(profileId: Long): Flow<List<String>> {
        return appDao.getUnlockedStickersForProfile(profileId)
    }

    suspend fun redeemSticker(profileId: Long, stickerId: String, costStars: Int): Boolean {
        val profile = appDao.getProfileById(profileId) ?: return false
        if (profile.starsCount < costStars) return false

        appDao.addStarsToProfile(profileId, -costStars)
        appDao.unlockSticker(UnlockedStickerEntity(profileId, stickerId))
        return true
    }

    private suspend fun ensureDefaultStickers(profileId: Long) {
        appDao.unlockSticker(UnlockedStickerEntity(profileId, "st_cat"))
        appDao.unlockSticker(UnlockedStickerEntity(profileId, "st_apple"))
    }

    suspend fun saveProgress(profileId: Long, lessonId: String, stars: Int) {
        val existing = appDao.getLessonProgress(profileId, lessonId)
        val attempts = (existing?.totalAttempts ?: 0) + 1
        val maxStars = maxOf(existing?.starsEarned ?: 0, stars)
        appDao.saveLessonProgress(
            LessonProgressEntity(
                profileId = profileId,
                lessonId = lessonId,
                isCompleted = true,
                starsEarned = maxStars,
                totalAttempts = attempts,
                lastStudiedAt = System.currentTimeMillis()
            )
        )
        appDao.addStarsToProfile(profileId, stars)

        // Log study session
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateStr = sdf.format(Date())
        appDao.insertStudyLog(
            StudyLogEntity(
                profileId = profileId,
                dateString = dateStr,
                durationMinutes = 10,
                wordsLearned = 5,
                quizCorrectCount = stars,
                quizTotalCount = 3,
                weakItemsListJson = "tr, ch"
            )
        )
    }

    suspend fun createNewProfile(name: String, avatarIndex: Int, age: Int): Long {
        val id = appDao.insertProfile(
            UserEntity(
                name = name,
                avatarIndex = avatarIndex,
                age = age,
                starsCount = 50
            )
        )
        ensureDefaultStickers(id)
        return id
    }

    fun getAllStickers3D(): List<Sticker3D> {
        return listOf(
            Sticker3D("st_cat", "Mèo Xinh 3D", "Động Vật", 5, "cat", "Meo meo! Bé đã mở Sticker Con Mèo Xinh xắn!", "Meo meo! Con mèo kêu meo meo!", 0xFFFF9800),
            Sticker3D("st_dog", "Chó Cún Dễ Thương", "Động Vật", 10, "dog", "Gâu gâu! Sticker Chó Cún thông minh và trung thành!", "Gâu gâu! Chó cún sủa gâu gâu!", 0xFF8D6E63),
            Sticker3D("st_duck", "Vịt Vàng Bơi Lội", "Động Vật", 10, "duck", "Cạc cạc! Vịt Vàng đang tung tăng bơi lội dưới nước!", "Cạc cạc! Vịt vàng kêu cạc cạc!", 0xFFFBC02D),
            Sticker3D("st_car", "Xe Ô Tô Đua 3D", "Phương Tiện", 15, "car", "Bíp bíp! Xe ô tô thể thao chạy cực nhanh trên đường đua!", "Bíp bíp! Xe ô tô bấm còi bíp bíp!", 0xFF1E88E5),
            Sticker3D("st_dino", "Khủng Long T-Rex", "Thần Thoại", 20, "dinosaur", "Gầm gừ! Khủng long Dũng Cảm dậm chân cực khỏe!", "Gầm gừ! Khủng long gầm thật to!", 0xFF4CAF50),
            Sticker3D("st_dragon", "Con Rồng Phun Lửa", "Thần Thoại", 25, "dragon", "Vút vút! Con Rồng Thần Kỳ bay lượn trên mây xanh!", "Vút vút! Rồng thần phun lửa rực rỡ!", 0xFFE53935),
            Sticker3D("st_lion", "Sư Tử Chúa Tể", "Động Vật", 25, "lion", "Gầm gừ! Sư Tử dũng mãnh là chúa tể rừng xanh!", "Gầm gừ! Sư tử xua đuổi muông thú!", 0xFFFFB300),
            Sticker3D("st_bee", "Con Ong Chăm Chỉ", "Động Vật", 10, "bee", "Vo ve vo ve! Con Ong ngoan chăm chỉ hút mật hoa!", "Vo ve! Ong vàng đi tìm mật ngọt!", 0xFFFFD740),
            Sticker3D("st_rabbit", "Thỏ Ngọc Siêu Quậy", "Động Vật", 15, "rabbit", "Hẳn hoi! Thỏ Ngọc thích ăn cà rốt giòn ngọt!", "Nhún nhảy! Thỏ ngọc nhảy tung tăng!", 0xFFEC407A),
            Sticker3D("st_tiger", "Hổ Con Mạnh Mẽ", "Động Vật", 20, "tiger", "Gầm gừ! Hổ Con oai phong lẫm liệt!", "Gầm gừ! Hổ con siêu dũng cảm!", 0xFFFF7043),
            Sticker3D("st_apple", "Trái Táo Đỏ Mọng", "Trái Cây", 5, "apple", "Xoạt xoạt! Trái Táo Đỏ tươi ngon ngọt lịm!", "Ngoạm ngoạm! Trái táo đỏ mọng!", 0xFFE53935),
            Sticker3D("st_fish", "Cá Cảnh Bẩy Màu", "Động Vật", 10, "fish", "Túm túp! Cá Cảnh xúng xính bơi lội tung tăng!", "Bọt nước! Cá cảnh bơi lội xúng xính!", 0xFF00ACC1)
        )
    }

    // Curriculum Data Factory
    fun getLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "lesson_l1",
                title = "Bài 1: Bảng Chữ Cái Đơn (29 Chữ)",
                subtitle = "Học thuộc 29 chữ cái cơ bản chuẩn tiếng Việt từ A đến Y",
                category = AlphabetCategory.SINGLE_LETTER,
                levelNumber = 1,
                colorHex = 0xFFFF6F00,
                iconName = "alphabet",
                items = getSingleLetters()
            ),
            Lesson(
                id = "lesson_l2",
                title = "Bài 2: Ghép Âm Đơn & Tiếng Đơn",
                subtitle = "Luyện ghép phụ âm + nguyên âm: ba, ca, đi, me, gà, bò...",
                category = AlphabetCategory.SINGLE_LETTER,
                levelNumber = 2,
                colorHex = 0xFF00BFA5,
                iconName = "blend",
                items = getSimpleSyllables()
            ),
            Lesson(
                id = "lesson_l3",
                title = "Bài 3: Ghép 5 Dấu Thanh Chuẩn",
                subtitle = "Sắc (´), Huyền (`), Hỏi (?), Ngã (~), Nặng (.)",
                category = AlphabetCategory.TONE_MARKS,
                levelNumber = 3,
                colorHex = 0xFFAB47BC,
                iconName = "tones",
                items = getToneMarkExamples()
            ),
            Lesson(
                id = "lesson_l4",
                title = "Bài 4: Phụ Âm Ghép Đôi & Ba",
                subtitle = "Thành thạo ch, nh, ph, th, tr, ng, ngh, qu, gi, kh...",
                category = AlphabetCategory.COMPOUND_CONSONANT,
                levelNumber = 4,
                colorHex = 0xFF0288D1,
                iconName = "compound",
                items = getCompoundConsonants()
            ),
            Lesson(
                id = "lesson_l5",
                title = "Bài 5: Vần Kép & Thẻ Từ 3D",
                subtitle = "Đánh vần từ đầy đủ có hình minh họa 3D tương tác sinh động",
                category = AlphabetCategory.VOWEL_RHYME,
                levelNumber = 5,
                colorHex = 0xFFFF4081,
                iconName = "cards3d",
                items = get3DWordCards()
            )
        )
    }

    private fun getSingleLetters(): List<AlphabetItem> {
        return listOf(
            AlphabetItem("a", "A", "a", "Âm A", "a", "Cá", "Con Cá", AlphabetCategory.SINGLE_LETTER, "fish", 0xFFFF7043),
            AlphabetItem("ă", "Ă", "ă", "Âm Á", "á", "Khăn", "Cái Khăn", AlphabetCategory.SINGLE_LETTER, "towel", 0xFFFFB300),
            AlphabetItem("â", "Â", "â", "Âm Ớ", "ớ", "Nấm", "Cây Nấm", AlphabetCategory.SINGLE_LETTER, "mushroom", 0xFF26A69A),
            AlphabetItem("b", "B", "b", "Âm Bờ", "bờ", "Bò", "Con Bò", AlphabetCategory.SINGLE_LETTER, "cow", 0xFF42A5F5),
            AlphabetItem("c", "C", "c", "Âm Cờ", "cờ", "Cá", "Con Cá", AlphabetCategory.SINGLE_LETTER, "fish", 0xFFEC407A),
            AlphabetItem("d", "D", "d", "Âm Dờ", "dờ", "Dù", "Cái Dù", AlphabetCategory.SINGLE_LETTER, "umbrella", 0xFFAB47BC),
            AlphabetItem("đ", "Đ", "đ", "Âm Đờ", "đờ", "Đèn", "Cái Đèn", AlphabetCategory.SINGLE_LETTER, "lamp", 0xFF78909C),
            AlphabetItem("e", "E", "e", "Âm E", "e", "Xe", "Xe Hơi", AlphabetCategory.SINGLE_LETTER, "car", 0xFFFF7043),
            AlphabetItem("ê", "Ê", "ê", "Âm Ê", "ê", "Bế", "Bé Bế", AlphabetCategory.SINGLE_LETTER, "baby", 0xFFFFA726),
            AlphabetItem("g", "G", "g", "Âm Gờ", "gờ", "Gà", "Con Gà", AlphabetCategory.SINGLE_LETTER, "chicken", 0xFF66BB6A),
            AlphabetItem("h", "H", "h", "Âm Hờ", "hờ", "Hổ", "Con Hổ", AlphabetCategory.SINGLE_LETTER, "tiger", 0xFF26C6DA),
            AlphabetItem("i", "I", "i", "Âm I", "i", "Bi", "Viên Bi", AlphabetCategory.SINGLE_LETTER, "ball", 0xFF5C6BC0),
            AlphabetItem("k", "K", "k", "Âm Ca", "ca", "Kéo", "Cái Kéo", AlphabetCategory.SINGLE_LETTER, "scissors", 0xFF8D6E63),
            AlphabetItem("l", "L", "l", "Âm Lờ", "lờ", "Lá", "Chiếc Lá", AlphabetCategory.SINGLE_LETTER, "leaf", 0xFF9CCC65),
            AlphabetItem("m", "M", "m", "Âm Mờ", "mờ", "Mèo", "Con Mèo", AlphabetCategory.SINGLE_LETTER, "cat", 0xFFEF5350),
            AlphabetItem("n", "N", "n", "Âm Nờ", "nờ", "Nón", "Cái Nón", AlphabetCategory.SINGLE_LETTER, "hat", 0xFFFFA726),
            AlphabetItem("o", "O", "o", "Âm O", "o", "Ong", "Con Ong", AlphabetCategory.SINGLE_LETTER, "bee", 0xFFFFCA28),
            AlphabetItem("ô", "Ô", "ô", "Âm Ô", "ô", "Ô tô", "Xe Ô Tô", AlphabetCategory.SINGLE_LETTER, "car", 0xFF42A5F5),
            AlphabetItem("ơ", "Ơ", "ơ", "Âm Ơ", "ơ", "Cờ", "Lá Cờ", AlphabetCategory.SINGLE_LETTER, "flag", 0xFFAB47BC),
            AlphabetItem("p", "P", "p", "Âm Pờ", "pờ", "Pin", "Cục Pin", AlphabetCategory.SINGLE_LETTER, "battery", 0xFF26A69A),
            AlphabetItem("q", "Q", "q", "Âm Quy", "quy", "Quạt", "Cái Quạt", AlphabetCategory.SINGLE_LETTER, "fan", 0xFFFF7043),
            AlphabetItem("r", "R", "r", "Âm Rờ", "rờ", "Rùa", "Con Rùa", AlphabetCategory.SINGLE_LETTER, "turtle", 0xFF66BB6A),
            AlphabetItem("s", "S", "s", "Âm Sờ", "sờ", "Sóc", "Con Sóc", AlphabetCategory.SINGLE_LETTER, "squirrel", 0xFFFFA726),
            AlphabetItem("t", "T", "t", "Âm Tờ", "tờ", "Táo", "Trái Táo", AlphabetCategory.SINGLE_LETTER, "apple", 0xFFEF5350),
            AlphabetItem("u", "U", "u", "Âm U", "u", "Mũ", "Cái Mũ", AlphabetCategory.SINGLE_LETTER, "hat", 0xFF26C6DA),
            AlphabetItem("ư", "Ư", "ư", "Âm Ư", "ư", "Sư tử", "Con Sư Tử", AlphabetCategory.SINGLE_LETTER, "lion", 0xFFFFB300),
            AlphabetItem("v", "V", "v", "Âm Vờ", "vờ", "Vịt", "Con Vịt", AlphabetCategory.SINGLE_LETTER, "duck", 0xFF42A5F5),
            AlphabetItem("x", "X", "x", "Âm Xờ", "xờ", "Xe", "Xe Máy", AlphabetCategory.SINGLE_LETTER, "car", 0xFF8D6E63),
            AlphabetItem("y", "Y", "y", "Âm Y", "y", "Y tế", "Hộp Y Tế", AlphabetCategory.SINGLE_LETTER, "medical", 0xFFAB47BC)
        )
    }

    private fun getSimpleSyllables(): List<AlphabetItem> {
        return listOf(
            AlphabetItem("s_ba", "BA", "ba", "bờ - a - ba", "bờ - a - ba", "Ba", "Ba Yêu", AlphabetCategory.SINGLE_LETTER, "father", 0xFFFF7043),
            AlphabetItem("s_ca", "CA", "ca", "cờ - a - ca", "cờ - a - ca", "Ca", "Cái Ca", AlphabetCategory.SINGLE_LETTER, "cup", 0xFF26A69A),
            AlphabetItem("s_di", "ĐI", "đi", "đờ - i - đi", "đờ - i - đi", "Đi", "Bé Đi Học", AlphabetCategory.SINGLE_LETTER, "walk", 0xFF42A5F5),
            AlphabetItem("s_me", "ME", "me", "mờ - e - me", "mờ - e - me", "Me", "Quả Me", AlphabetCategory.SINGLE_LETTER, "tamarind", 0xFFFFA726),
            AlphabetItem("s_ga", "GÀ", "gà", "gờ - a - ga - huyền - gà", "gờ - a - ga - huyền - gà", "Gà", "Con Gà", AlphabetCategory.SINGLE_LETTER, "chicken", 0xFF66BB6A),
            AlphabetItem("s_bo", "BÒ", "bò", "bờ - o - bo - huyền - bò", "bờ - o - bo - huyền - bò", "Bò", "Con Bò", AlphabetCategory.SINGLE_LETTER, "cow", 0xFFAB47BC),
            AlphabetItem("s_co", "CỜ", "cờ", "cờ - ơ - cơ - huyền - cờ", "cờ - ơ - cơ - huyền - cờ", "Cờ", "Lá Cờ", AlphabetCategory.SINGLE_LETTER, "flag", 0xFFEF5350),
            AlphabetItem("s_la", "LÁ", "lá", "lờ - a - la - sắc - lá", "lờ - a - la - sắc - lá", "Lá", "Chiếc Lá", AlphabetCategory.SINGLE_LETTER, "leaf", 0xFF8BC34A),
            AlphabetItem("s_tu", "TỦ", "tủ", "tờ - u - tu - hỏi - tủ", "tờ - u - tu - hỏi - tủ", "Tủ", "Cái Tủ", AlphabetCategory.SINGLE_LETTER, "house", 0xFFFF9800),
            AlphabetItem("s_xo", "XÔ", "xô", "xờ - ô - xô", "xờ - ô - xô", "Xô", "Cái Xô", AlphabetCategory.SINGLE_LETTER, "cup", 0xFF2196F3),
            AlphabetItem("s_bi", "BI", "bi", "bờ - i - bi", "bờ - i - bi", "Bi", "Viên Bi", AlphabetCategory.SINGLE_LETTER, "ball", 0xFF9C27B0),
            AlphabetItem("s_xe", "XE", "xe", "xờ - e - xe", "xờ - e - xe", "Xe", "Xe Hơi", AlphabetCategory.SINGLE_LETTER, "car", 0xFFFF5722)
        )
    }

    private fun getToneMarkExamples(): List<AlphabetItem> {
        return listOf(
            AlphabetItem("t_ngang", "CA", "ca", "Thanh Ngang", "cờ - a - ca", "Ca", "Cái Ca (Thanh Ngang)", AlphabetCategory.TONE_MARKS, "cup", 0xFFFF7043),
            AlphabetItem("t_sac", "CÁ", "cá", "Dấu Sắc (´)", "cờ - a - ca - sắc - cá", "Cá", "Con Cá (Dấu Sắc)", AlphabetCategory.TONE_MARKS, "fish", 0xFFEF5350),
            AlphabetItem("t_huyen", "CÀ", "cà", "Dấu Huyền (`)", "cờ - a - ca - huyền - cà", "Cà", "Quả Cà (Dấu Huyền)", AlphabetCategory.TONE_MARKS, "apple", 0xFF26A69A),
            AlphabetItem("t_hoi", "CẢ", "cả", "Dấu Hỏi (?)", "cờ - a - ca - hỏi - cả", "Cả", "Tất Cả (Dấu Hỏi)", AlphabetCategory.TONE_MARKS, "question", 0xFFFFA726),
            AlphabetItem("t_nga", "CÃ", "cã", "Dấu Ngã (~)", "cờ - a - ca - ngã - cã", "Cã", "Cã Giã (Dấu Ngã)", AlphabetCategory.TONE_MARKS, "wave", 0xFFAB47BC),
            AlphabetItem("t_nang", "CẠ", "cạ", "Dấu Nặng (.)", "cờ - a - ca - nặng - cạ", "Cạ", "Cạ Mặt (Dấu Nặng)", AlphabetCategory.TONE_MARKS, "dot", 0xFF78909C),
            AlphabetItem("t_ba_sac", "BÁ", "bá", "Dấu Sắc (´)", "bờ - a - ba - sắc - bá", "Bá", "Quả Bá", AlphabetCategory.TONE_MARKS, "star", 0xFFFF9800),
            AlphabetItem("t_ba_huyen", "BÀ", "bà", "Dấu Huyền (`)", "bờ - a - ba - huyền - bà", "Bà", "Bà Nội", AlphabetCategory.TONE_MARKS, "grandmother", 0xFF4CAF50),
            AlphabetItem("t_ba_hoi", "BẢ", "bả", "Dấu Hỏi (?)", "bờ - a - ba - hỏi - bả", "Bả", "Bảng Học", AlphabetCategory.TONE_MARKS, "question", 0xFF00BCD4)
        )
    }

    private fun getCompoundConsonants(): List<AlphabetItem> {
        return listOf(
            AlphabetItem("c_ch", "CH", "ch", "Phụ âm Chờ", "chờ - a - cha", "Cha", "Ba Cha", AlphabetCategory.COMPOUND_CONSONANT, "father", 0xFFFF7043),
            AlphabetItem("c_nh", "NH", "nh", "Phụ âm Nhờ", "nhờ - a - nha - huyền - nhà", "Nhà", "Ngôi Nhà", AlphabetCategory.COMPOUND_CONSONANT, "house", 0xFF26A69A),
            AlphabetItem("c_ph", "PH", "ph", "Phụ âm Phờ", "phờ - ơ - phơ - hỏi - phở", "Phở", "Bát Phở", AlphabetCategory.COMPOUND_CONSONANT, "bowl", 0xFF42A5F5),
            AlphabetItem("c_th", "TH", "th", "Phụ âm Thờ", "thờ - o - tho - hỏi - thỏ", "Thỏ", "Con Thỏ", AlphabetCategory.COMPOUND_CONSONANT, "rabbit", 0xFFFFA726),
            AlphabetItem("c_tr", "TR", "tr", "Phụ âm Trờ", "trờ - a - tra - i - trai", "Trai", "Con Trai", AlphabetCategory.COMPOUND_CONSONANT, "boy", 0xFFAB47BC),
            AlphabetItem("c_ng", "NG", "ng", "Phụ âm Ngờ", "ngờ - u - ngu - huyền - ngủ", "Ngủ", "Bé Ngủ", AlphabetCategory.COMPOUND_CONSONANT, "sleep", 0xFF66BB6A),
            AlphabetItem("c_ngh", "NGH", "ngh", "Phụ âm Ngờ kép", "ngờ - e - nghe", "Nghe", "Nghe Nhạc", AlphabetCategory.COMPOUND_CONSONANT, "music", 0xFF26C6DA),
            AlphabetItem("c_qu", "QU", "qu", "Phụ âm Quơ", "quơ - a - qua - hỏi - quả", "Quả", "Quả Táo", AlphabetCategory.COMPOUND_CONSONANT, "apple", 0xFFEF5350),
            AlphabetItem("c_gi", "GI", "gi", "Phụ âm Giơ", "giơ - i - gi - huyền - gì", "Gì", "Cái Gì", AlphabetCategory.COMPOUND_CONSONANT, "question", 0xFF8BC34A),
            AlphabetItem("c_kh", "KH", "kh", "Phụ âm Khờ", "khờ - a - kha - n - khăn", "Khăn", "Cái Khăn", AlphabetCategory.COMPOUND_CONSONANT, "towel", 0xFFFF9800)
        )
    }

    private fun get3DWordCards(): List<AlphabetItem> {
        return listOf(
            AlphabetItem("w_ca", "CÁ", "cá", "Con Cá 3D", "cờ - a - ca - sắc - cá", "Cá", "Con Cá Biển 3D", AlphabetCategory.VOWEL_RHYME, "fish", 0xFFFF7043),
            AlphabetItem("w_nha", "NHÀ", "nhà", "Ngôi Nhà 3D", "nhờ - a - nha - huyền - nhà", "Nhà", "Ngôi Nhà Đẹp 3D", AlphabetCategory.VOWEL_RHYME, "house", 0xFF26A69A),
            AlphabetItem("w_meo", "MÈO", "mèo", "Con Mèo 3D", "mờ - eo - meo - huyền - mèo", "Mèo", "Con Mèo Xinh 3D", AlphabetCategory.VOWEL_RHYME, "cat", 0xFFEF5350),
            AlphabetItem("w_tao", "TÁO", "táo", "Trái Táo 3D", "tờ - ao - tao - sắc - táo", "Táo", "Trái Táo Đỏ 3D", AlphabetCategory.VOWEL_RHYME, "apple", 0xFFFFA726),
            AlphabetItem("w_oto", "Ô TÔ", "ô tô", "Xe Ô Tô 3D", "ô - tờ - ô - tô", "Ô tô", "Xe Ô Tô Đỏ 3D", AlphabetCategory.VOWEL_RHYME, "car", 0xFF42A5F5),
            AlphabetItem("w_cho", "CHÓ", "chó", "Con Chó 3D", "chờ - o - cho - sắc - chó", "Chó", "Con Chó Cún 3D", AlphabetCategory.VOWEL_RHYME, "dog", 0xFFAB47BC),
            AlphabetItem("w_bong", "BÓNG", "bóng", "Quả Bóng 3D", "bờ - ong - bong - sắc - bóng", "Bóng", "Quả Bóng Tròn 3D", AlphabetCategory.VOWEL_RHYME, "ball", 0xFF26C6DA),
            AlphabetItem("w_vit", "VỊT", "vịt", "Con Vịt 3D", "vờ - it - vit - nặng - vịt", "Vịt", "Con Vịt Vàng 3D", AlphabetCategory.VOWEL_RHYME, "duck", 0xFFFFCA28),
            AlphabetItem("w_dino", "KHỦNG LONG", "khủng long", "Khủng Long 3D", "khờ - ung - khung - hỏi - khủng - lờ - ong - long", "Khủng long", "Khủng Long T-Rex 3D", AlphabetCategory.VOWEL_RHYME, "dinosaur", 0xFF4CAF50),
            AlphabetItem("w_dragon", "CON RỒNG", "con rồng", "Con Rồng 3D", "cờ - on - con - rờ - ong - rong - huyền - rồng", "Rồng", "Con Rồng Phun Lửa 3D", AlphabetCategory.VOWEL_RHYME, "dragon", 0xFFE53935),
            AlphabetItem("w_lion", "SƯ TỬ", "sư tử", "Con Sư Tử 3D", "sờ - ư - sư - tờ - ư - tư - hỏi - tử", "Sư tử", "Sư Tử Chúa Tể 3D", AlphabetCategory.VOWEL_RHYME, "lion", 0xFFFFB300),
            AlphabetItem("w_bee", "CON ONG", "con ong", "Con Ong 3D", "cờ - on - con - o - ngờ - ong", "Ong", "Con Ong Chăm Chỉ 3D", AlphabetCategory.VOWEL_RHYME, "bee", 0xFFFFD740)
        )
    }

    // Generate Quiz Questions for Kids Interactive Games
    fun getQuizQuestions(gameType: GameType): List<QuizQuestion> {
        val allItems = getSingleLetters() + getCompoundConsonants() + get3DWordCards()
        val shuffled = allItems.shuffled()

        return shuffled.take(6).mapIndexed { index, target ->
            val distractorList = (allItems - target).shuffled().take(3)
            val options = (distractorList + target).shuffled()

            val prompt = when (gameType) {
                GameType.LISTEN_AND_FIND -> "Bé hãy bấm nghe và chọn chữ âm đúng nhé!"
                GameType.DRAG_AND_SPELL -> "Bé hãy tìm chữ cái ghép thành từ '${target.sampleWord}'"
                GameType.MEMORY_MATCH -> "Tìm thẻ hình tương ứng với chữ '${target.displaySymbol}'"
            }

            QuizQuestion(
                id = "quiz_$index",
                gameType = gameType,
                promptText = prompt,
                speechPrompt = when (gameType) {
                    GameType.LISTEN_AND_FIND -> "Bé hãy tìm chữ ${target.phoneticName}"
                    GameType.DRAG_AND_SPELL -> "Bé hãy chọn chữ để đánh vần từ ${target.sampleWord}"
                    GameType.MEMORY_MATCH -> "Thẻ hình nào là ${target.sampleWordFull}?"
                },
                targetItem = target,
                options = options
            )
        }
    }
}
