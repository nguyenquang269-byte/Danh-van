package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.GameType
import com.example.data.model.Lesson
import com.example.ui.components.ParentGateDialog
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.StickerShopScreen
import com.example.ui.theme.DanhVanKidTheme

enum class ScreenRoute {
    HOME,
    LESSON_DETAIL,
    GAMES,
    STICKER_SHOP,
    PARENT_PORTAL
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DanhVanKidTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen(mainViewModel: MainViewModel = viewModel()) {
    val profiles by mainViewModel.allProfiles.collectAsStateWithLifecycle()
    val activeProfile by mainViewModel.activeProfile.collectAsStateWithLifecycle()
    val lessons by mainViewModel.lessons.collectAsStateWithLifecycle()

    var currentRoute by remember { mutableStateOf(ScreenRoute.HOME) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var selectedGameType by remember { mutableStateOf(GameType.LISTEN_AND_FIND) }
    var showParentGate by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isTabletOrWide = configuration.screenWidthDp >= 600

    if (showParentGate) {
        ParentGateDialog(
            onDismiss = { showParentGate = false },
            onSuccess = {
                showParentGate = false
                currentRoute = ScreenRoute.PARENT_PORTAL
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        bottomBar = {
            if (!isTabletOrWide && (currentRoute == ScreenRoute.HOME || currentRoute == ScreenRoute.GAMES || currentRoute == ScreenRoute.STICKER_SHOP || currentRoute == ScreenRoute.PARENT_PORTAL)) {
                KidBottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route == ScreenRoute.PARENT_PORTAL) {
                            showParentGate = true
                        } else {
                            currentRoute = route
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Adaptive Navigation Rail for Tablets / Wide screens
            if (isTabletOrWide && (currentRoute == ScreenRoute.HOME || currentRoute == ScreenRoute.GAMES || currentRoute == ScreenRoute.STICKER_SHOP || currentRoute == ScreenRoute.PARENT_PORTAL)) {
                NavigationRail(
                    containerColor = Color.White
                ) {
                    NavigationRailItem(
                        selected = currentRoute == ScreenRoute.HOME,
                        onClick = { currentRoute = ScreenRoute.HOME },
                        icon = { Icon(Icons.Rounded.Home, contentDescription = "Trang chủ") },
                        label = { Text("Trang Chủ", fontWeight = FontWeight.Bold) }
                    )
                    NavigationRailItem(
                        selected = currentRoute == ScreenRoute.GAMES,
                        onClick = { currentRoute = ScreenRoute.GAMES },
                        icon = { Icon(Icons.Rounded.Extension, contentDescription = "Trò chơi") },
                        label = { Text("Trò Chơi", fontWeight = FontWeight.Bold) }
                    )
                    NavigationRailItem(
                        selected = currentRoute == ScreenRoute.STICKER_SHOP,
                        onClick = { currentRoute = ScreenRoute.STICKER_SHOP },
                        icon = { Icon(Icons.Rounded.Stars, contentDescription = "Đổi quà") },
                        label = { Text("Đổi Quà 3D", fontWeight = FontWeight.Bold) }
                    )
                    NavigationRailItem(
                        selected = currentRoute == ScreenRoute.PARENT_PORTAL,
                        onClick = { showParentGate = true },
                        icon = { Icon(Icons.Rounded.FamilyRestroom, contentDescription = "Phụ huynh") },
                        label = { Text("Phụ Huynh", fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                when (currentRoute) {
                    ScreenRoute.HOME -> {
                        HomeScreen(
                            currentProfile = activeProfile,
                            lessons = lessons,
                            onSelectLesson = { lesson ->
                                selectedLesson = lesson
                                currentRoute = ScreenRoute.LESSON_DETAIL
                            },
                            onLaunchGame = { gameType ->
                                selectedGameType = gameType
                                currentRoute = ScreenRoute.GAMES
                            },
                            onOpenStickerShop = {
                                currentRoute = ScreenRoute.STICKER_SHOP
                            },
                            onOpenParentPortal = {
                                showParentGate = true
                            },
                            onSpeakPrompt = { text ->
                                mainViewModel.speakText(text)
                            }
                        )
                    }

                    ScreenRoute.LESSON_DETAIL -> {
                        selectedLesson?.let { lesson ->
                            LessonDetailScreen(
                                lesson = lesson,
                                onBack = { currentRoute = ScreenRoute.HOME },
                                onSpeakItem = { item ->
                                    mainViewModel.speechEngine.speakSpellingGuide(item.spellingGuide, item.sampleWord)
                                },
                                onCompleteLesson = {
                                    mainViewModel.recordLessonStars(lesson.id, 3)
                                    mainViewModel.speakText("Bé giỏi quá! Bé đã hoàn thành bài học.")
                                    currentRoute = ScreenRoute.HOME
                                }
                            )
                        } ?: run {
                            currentRoute = ScreenRoute.HOME
                        }
                    }

                    ScreenRoute.GAMES -> {
                        val questions = remember(selectedGameType) {
                            mainViewModel.getQuizQuestionsForGame(selectedGameType)
                        }
                        GamesScreen(
                            gameType = selectedGameType,
                            questions = questions,
                            onBack = { currentRoute = ScreenRoute.HOME },
                            onSpeakPrompt = { text ->
                                mainViewModel.speakText(text)
                            },
                            onFinishGame = { stars ->
                                mainViewModel.recordLessonStars("game_${selectedGameType.name}", stars)
                                currentRoute = ScreenRoute.HOME
                            }
                        )
                    }

                    ScreenRoute.STICKER_SHOP -> {
                        StickerShopScreen(
                            viewModel = mainViewModel,
                            onNavigateBack = { currentRoute = ScreenRoute.HOME }
                        )
                    }

                    ScreenRoute.PARENT_PORTAL -> {
                        ParentDashboardScreen(
                            profiles = profiles,
                            activeProfile = activeProfile,
                            onSelectProfile = { mainViewModel.selectProfile(it) },
                            onCreateProfile = { name, age -> mainViewModel.createProfile(name, age) },
                            onBack = { currentRoute = ScreenRoute.HOME }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KidBottomNavigationBar(
    currentRoute: ScreenRoute,
    onNavigate: (ScreenRoute) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == ScreenRoute.HOME,
            onClick = { onNavigate(ScreenRoute.HOME) },
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Trang chủ", modifier = Modifier.testTag("nav_home")) },
            label = { Text("TRANG CHỦ", fontWeight = FontWeight.Black, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E88E5),
                selectedTextColor = Color(0xFF1E88E5),
                indicatorColor = Color(0xFFE3F2FD),
                unselectedIconColor = Color(0xFF8B5E3C).copy(alpha = 0.6f),
                unselectedTextColor = Color(0xFF8B5E3C).copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == ScreenRoute.GAMES,
            onClick = { onNavigate(ScreenRoute.GAMES) },
            icon = { Icon(Icons.Rounded.Extension, contentDescription = "Trò chơi", modifier = Modifier.testTag("nav_games")) },
            label = { Text("TRÒ CHƠI", fontWeight = FontWeight.Black, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFAB47BC),
                selectedTextColor = Color(0xFFAB47BC),
                indicatorColor = Color(0xFFF3E5F5),
                unselectedIconColor = Color(0xFF8B5E3C).copy(alpha = 0.6f),
                unselectedTextColor = Color(0xFF8B5E3C).copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == ScreenRoute.STICKER_SHOP,
            onClick = { onNavigate(ScreenRoute.STICKER_SHOP) },
            icon = { Icon(Icons.Rounded.Stars, contentDescription = "Đổi quà 3D", modifier = Modifier.testTag("nav_stickers")) },
            label = { Text("ĐỔI QUÀ 3D", fontWeight = FontWeight.Black, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFFF9800),
                selectedTextColor = Color(0xFFFF9800),
                indicatorColor = Color(0xFFFFF3E0),
                unselectedIconColor = Color(0xFF8B5E3C).copy(alpha = 0.6f),
                unselectedTextColor = Color(0xFF8B5E3C).copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == ScreenRoute.PARENT_PORTAL,
            onClick = { onNavigate(ScreenRoute.PARENT_PORTAL) },
            icon = { Icon(Icons.Rounded.FamilyRestroom, contentDescription = "Phụ huynh", modifier = Modifier.testTag("nav_parent")) },
            label = { Text("PHỤ HUYNH", fontWeight = FontWeight.Black, fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF4E342E),
                selectedTextColor = Color(0xFF4E342E),
                indicatorColor = Color(0xFFFFF3E0),
                unselectedIconColor = Color(0xFF8B5E3C).copy(alpha = 0.6f),
                unselectedTextColor = Color(0xFF8B5E3C).copy(alpha = 0.6f)
            )
        )
    }
}
