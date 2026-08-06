package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Stars
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Sticker3D

@Composable
fun StickerShowcaseDialog(
    sticker: Sticker3D,
    onDismiss: () -> Unit,
    onPlaySound: (String) -> Unit
) {
    // Play animal / vehicle sound effect immediately upon opening
    LaunchedEffect(sticker) {
        onPlaySound("${sticker.soundEffectSimulation} ... ${sticker.soundVoicePrompt}")
    }

    // Bounce & 3D Wobble Animation
    val infiniteTransition = rememberInfiniteTransition(label = "sticker_anim")
    
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = -7f,
        targetValue = 7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotate"
    )

    val bgPulseAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bgPulse"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f))
                .testTag("sticker_showcase_dialog"),
            contentAlignment = Alignment.Center
        ) {
            // Full-Screen Animated Particle Rays Canvas (Hiệu ứng nền toàn màn hình)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val center = Offset(w / 2f, h / 2f)

                // Background Radial Magic Glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(sticker.badgeColorHex).copy(alpha = 0.5f),
                            Color(0xFFFFD740).copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = w * 0.7f
                    ),
                    radius = w * 0.7f,
                    center = center
                )

                // Rotating Star Rays
                for (i in 0 until 12) {
                    val angle = (i * 30 + bgPulseAnim).toDouble() * Math.PI / 180.0
                    val endX = (center.x + Math.cos(angle) * w * 0.8f).toFloat()
                    val endY = (center.y + Math.sin(angle) * w * 0.8f).toFloat()
                    drawLine(
                        color = Color.White.copy(alpha = 0.15f),
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 14f
                    )
                }

                // Floating Sparkle Dots
                val sparkPositions = listOf(
                    Offset(w * 0.2f, h * 0.25f),
                    Offset(w * 0.8f, h * 0.2f),
                    Offset(w * 0.15f, h * 0.75f),
                    Offset(w * 0.85f, h * 0.7f),
                    Offset(w * 0.3f, h * 0.85f),
                    Offset(w * 0.7f, h * 0.15f)
                )
                sparkPositions.forEachIndexed { idx, pos ->
                    val sparkSize = (12 + (idx * 4)).dp.toPx()
                    drawCircle(
                        color = if (idx % 2 == 0) Color(0xFFFFD740) else Color.White,
                        radius = sparkSize,
                        center = pos
                    )
                }
            }

            // Close button top right
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape)
                        .testTag("close_showcase_btn")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Đóng",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Main 3D Showcase Card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Celebration Badge Header
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFFFD740),
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(3.dp, Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Stars,
                            contentDescription = "Thành tích",
                            tint = Color(0xFF4E342E),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "STICKER 3D SỞ HỮU",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4E342E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ENLARGED 3D STICKER CONTAINER (Hiệu ứng di chuyển & nẩy)
                val currentDensity = LocalDensity.current
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .scale(scaleAnim)
                        .rotate(rotateAnim)
                        .graphicsLayer(
                            cameraDistance = 12f * currentDensity.density,
                            rotationY = rotateAnim * 0.5f
                        )
                        .shadow(24.dp, RoundedCornerShape(48.dp), spotColor = Color(sticker.badgeColorHex))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.White, Color(0xFFFFFDE7))
                            ),
                            shape = RoundedCornerShape(48.dp)
                        )
                        .border(6.dp, Color(sticker.badgeColorHex), RoundedCornerShape(48.dp))
                        .clickable {
                            onPlaySound("${sticker.soundEffectSimulation} ... ${sticker.soundVoicePrompt}")
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KidIllustrationGraphic(
                        type = sticker.illustrationType,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Title & Sound Prompt Box
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(12.dp),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = sticker.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4E342E),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Sound Effect Simulation Banner
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(sticker.badgeColorHex).copy(alpha = 0.15f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "🔊 Âm thanh: \"${sticker.soundEffectSimulation}\"",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4E342E),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = sticker.soundVoicePrompt,
                            fontSize = 14.sp,
                            color = Color(0xFF8B5E3C),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Replay Audio Button
                        Button(
                            onClick = {
                                onPlaySound("${sticker.soundEffectSimulation} ... ${sticker.soundVoicePrompt}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(sticker.badgeColorHex)),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.VolumeUp,
                                contentDescription = "Phát âm thanh",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "NGHE LẠI ÂM THANH",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD740)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "BÉ RẤT THÍCH ❤️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4E342E)
                    )
                }
            }
        }
    }
}
