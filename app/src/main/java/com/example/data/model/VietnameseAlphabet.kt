package com.example.data.model

enum class AlphabetCategory(val titleVi: String, val descriptionVi: String) {
    SINGLE_LETTER("Chữ Cái Đơn", "Bảng 29 chữ cái cơ bản: a, b, c, d, đ, e..."),
    COMPOUND_CONSONANT("Phụ Âm Ghép", "Các phụ âm đôi, ba: ch, gh, gi, kh, nh, ng, ngh, ph, qu, th, tr"),
    VOWEL_RHYME("Vần & Nguyên Âm Kép", "Các vần quen thuộc: ai, ao, au, ay, am, an, ang, inh, ui, oi..."),
    TONE_MARKS("5 Dấu Thanh", "Sắc (´), Huyền (`), Hỏi (?), Ngã (~), Nặng (.)"),
    WORD_EXPLORER("Từ Mẫu 3D", "Luyện ghép từ với hình ảnh 3D sinh động")
}

data class AlphabetItem(
    val id: String,
    val displaySymbol: String,
    val lowercaseSymbol: String,
    val phoneticName: String, // e.g. "Âm Bờ"
    val spellingGuide: String, // e.g. "bờ - a - ba - sắc - bá"
    val sampleWord: String, // e.g. "Cá"
    val sampleWordFull: String, // e.g. "Con Cá"
    val category: AlphabetCategory,
    val illustrationType: String, // "fish", "ball", "cat", "house", "sun", "apple", "car", "duck", "cow", "umbrella", "baby"
    val colorHex: Long = 0xFFFF6F00
)

data class Lesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: AlphabetCategory,
    val items: List<AlphabetItem>,
    val iconName: String,
    val levelNumber: Int,
    val colorHex: Long
)

enum class GameType(val titleVi: String) {
    LISTEN_AND_FIND("Tìm Âm Đúng"),
    DRAG_AND_SPELL("Ghép Từ Đánh Vần"),
    MEMORY_MATCH("Lật Thẻ Trí Nhớ")
}

data class QuizQuestion(
    val id: String,
    val gameType: GameType,
    val promptText: String,
    val speechPrompt: String,
    val targetItem: AlphabetItem,
    val options: List<AlphabetItem>
)

data class Sticker3D(
    val id: String,
    val name: String,
    val category: String,
    val costStars: Int,
    val illustrationType: String,
    val soundVoicePrompt: String,
    val soundEffectSimulation: String,
    val badgeColorHex: Long = 0xFFFF9800
)
