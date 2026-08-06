package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlphabetItem
import com.example.data.model.GameType
import com.example.data.model.QuizQuestion
import com.example.ui.components.KidIllustrationGraphic
import com.example.ui.components.RewardStarDialog

@Composable
fun GamesScreen(
    gameType: GameType,
    questions: List<QuizQuestion>,
    onBack: () -> Unit,
    onSpeakPrompt: (String) -> Unit,
    onFinishGame: (starsEarned: Int) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var scoreStars by remember { mutableIntStateOf(0) }
    var showRewardDialog by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentIndex)

    // Speech trigger on question load
    LaunchedEffect(currentIndex) {
        currentQuestion?.let {
            onSpeakPrompt(it.speechPrompt)
        }
    }

    if (showRewardDialog) {
        RewardStarDialog(
            starsCount = maxOf(1, scoreStars),
            onContinue = {
                onFinishGame(maxOf(1, scoreStars))
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF5))
    ) {
        // Top Header
        Surface(
            color = when (gameType) {
                GameType.LISTEN_AND_FIND -> Color(0xFF0288D1)
                GameType.DRAG_AND_SPELL -> Color(0xFFE91E63)
                GameType.MEMORY_MATCH -> Color(0xFF4CAF50)
            },
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("game_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Thoát trò chơi",
                        tint = Color.White
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gameType.titleVi,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Câu ${currentIndex + 1} / ${questions.size}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Sao",
                        tint = Color(0xFFFFD54F)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$scoreStars",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        currentQuestion?.let { q ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Audio Prompt Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSpeakPrompt(q.speechPrompt) }
                        .testTag("game_prompt_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFFFFF3E0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.VolumeUp,
                                contentDescription = "Phát âm thanh câu hỏi",
                                tint = Color(0xFFFF6F00),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Bấm để nghe đọc:",
                                fontSize = 12.sp,
                                color = Color(0xFF78909C)
                            )
                            Text(
                                text = q.promptText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C3E50)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Specific Game Play Mechanics
                when (gameType) {
                    GameType.LISTEN_AND_FIND -> {
                        // 4 Option Cards
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(q.options) { option ->
                                OptionCard(
                                    item = option,
                                    onSelect = {
                                        if (option.id == q.targetItem.id) {
                                            scoreStars += 1
                                            onSpeakPrompt("Đúng rồi! Bé giỏi quá!")
                                        } else {
                                            onSpeakPrompt("Chưa chính xác. Bé thử lại nhé!")
                                        }

                                        if (currentIndex + 1 < questions.size) {
                                            currentIndex += 1
                                        } else {
                                            showRewardDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }

                    GameType.DRAG_AND_SPELL -> {
                        // Picture Preview
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(Color.White, RoundedCornerShape(20.dp))
                                .border(2.dp, Color(0xFFFFB74D), RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            KidIllustrationGraphic(type = q.targetItem.illustrationType, modifier = Modifier.size(90.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Từ mẫu: ${q.targetItem.sampleWord}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFF6F00)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Letter Options
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(q.options) { option ->
                                OptionCard(
                                    item = option,
                                    onSelect = {
                                        if (option.id == q.targetItem.id) {
                                            scoreStars += 1
                                            onSpeakPrompt("Bé đã ghép đúng từ ${q.targetItem.sampleWord}!")
                                        } else {
                                            onSpeakPrompt("Thử lại nhé bé yêu!")
                                        }

                                        if (currentIndex + 1 < questions.size) {
                                            currentIndex += 1
                                        } else {
                                            showRewardDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }

                    GameType.MEMORY_MATCH -> {
                        // Memory Flip Cards
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(q.options) { option ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clickable {
                                            if (option.id == q.targetItem.id) {
                                                scoreStars += 1
                                                onSpeakPrompt("Tuyệt vời! Ghép đúng rồi!")
                                            } else {
                                                onSpeakPrompt("Bé lật chưa đúng thẻ, thử lại nhé!")
                                            }

                                            if (currentIndex + 1 < questions.size) {
                                                currentIndex += 1
                                            } else {
                                                showRewardDialog = true
                                            }
                                        },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        KidIllustrationGraphic(type = option.illustrationType, modifier = Modifier.size(54.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = option.sampleWord,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(option.colorHex)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionCard(
    item: AlphabetItem,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onSelect() }
            .testTag("option_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(item.colorHex).copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.displaySymbol,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(item.colorHex)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.phoneticName,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF37474F)
            )
        }
    }
}
