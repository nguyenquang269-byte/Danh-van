package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Kid3DCardContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFFFAED),
    borderColor: Color = Color(0xFFFFB74D),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow( elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false)
            .border(width = 3.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(backgroundColor, Color.White)
                )
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Custom 3D Illustration Graphic Renderer for Vietnamese Vocabulary Words
 */
@Composable
fun KidIllustrationGraphic(
    type: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (type.lowercase()) {
                "fish" -> {
                    // Fish Body 3D Gradient
                    val fishPath = Path().apply {
                        moveTo(w * 0.2f, h * 0.5f)
                        quadraticTo(w * 0.4f, h * 0.15f, w * 0.75f, h * 0.5f)
                        quadraticTo(w * 0.4f, h * 0.85f, w * 0.2f, h * 0.5f)
                    }
                    drawPath(
                        path = fishPath,
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFF7043), Color(0xFFD84315)),
                            center = Offset(w * 0.5f, h * 0.4f)
                        )
                    )
                    // Fish Tail
                    val tailPath = Path().apply {
                        moveTo(w * 0.75f, h * 0.5f)
                        lineTo(w * 0.95f, h * 0.25f)
                        lineTo(w * 0.9f, h * 0.5f)
                        lineTo(w * 0.95f, h * 0.75f)
                        close()
                    }
                    drawPath(tailPath, Color(0xFFFFAB40))
                    // Eye
                    drawCircle(Color.White, radius = w * 0.08f, center = Offset(w * 0.35f, h * 0.45f))
                    drawCircle(Color.Black, radius = w * 0.04f, center = Offset(w * 0.35f, h * 0.45f))
                    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.33f, h * 0.43f))
                }
                "house" -> {
                    // House Body
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFB74D), Color(0xFFF57C00))
                        ),
                        topLeft = Offset(w * 0.25f, h * 0.45f),
                        size = Size(w * 0.5f, h * 0.45f)
                    )
                    // Roof
                    val roofPath = Path().apply {
                        moveTo(w * 0.15f, h * 0.45f)
                        lineTo(w * 0.5f, h * 0.15f)
                        lineTo(w * 0.85f, h * 0.45f)
                        close()
                    }
                    drawPath(roofPath, Color(0xFFE53935))
                    // Door & Window
                    drawRect(Color(0xFF8D6E63), topLeft = Offset(w * 0.42f, h * 0.62f), size = Size(w * 0.16f, h * 0.28f))
                    drawCircle(Color(0xFF81D4FA), radius = w * 0.08f, center = Offset(w * 0.32f, h * 0.58f))
                }
                "cat" -> {
                    // Cat Head 3D
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFB74D), Color(0xFFE65100)),
                            center = Offset(w * 0.5f, h * 0.45f)
                        ),
                        radius = w * 0.35f,
                        center = Offset(w * 0.5f, h * 0.5f)
                    )
                    // Ears
                    val earLeft = Path().apply {
                        moveTo(w * 0.25f, h * 0.28f)
                        lineTo(w * 0.18f, h * 0.08f)
                        lineTo(w * 0.4f, h * 0.22f)
                        close()
                    }
                    val earRight = Path().apply {
                        moveTo(w * 0.75f, h * 0.28f)
                        lineTo(w * 0.82f, h * 0.08f)
                        lineTo(w * 0.6f, h * 0.22f)
                        close()
                    }
                    drawPath(earLeft, Color(0xFFE65100))
                    drawPath(earRight, Color(0xFFE65100))
                    // Eyes
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.36f, h * 0.45f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.36f, h * 0.45f))
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.64f, h * 0.45f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.64f, h * 0.45f))
                    // Pink nose
                    drawCircle(Color(0xFFFF80AB), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.56f))
                }
                "apple" -> {
                    // Apple Body
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFF5252), Color(0xFFC62828)),
                            center = Offset(w * 0.4f, h * 0.4f)
                        ),
                        radius = w * 0.38f,
                        center = Offset(w * 0.5f, h * 0.52f)
                    )
                    // Stem & Leaf
                    drawRect(Color(0xFF5D4037), topLeft = Offset(w * 0.48f, h * 0.12f), size = Size(w * 0.04f, h * 0.12f))
                    val leafPath = Path().apply {
                        moveTo(w * 0.52f, h * 0.16f)
                        quadraticTo(w * 0.7f, h * 0.12f, w * 0.65f, h * 0.24f)
                        quadraticTo(w * 0.55f, h * 0.22f, w * 0.52f, h * 0.16f)
                    }
                    drawPath(leafPath, Color(0xFF4CAF50))
                }
                "car" -> {
                    // Car Roof
                    val carTop = Path().apply {
                        moveTo(w * 0.25f, h * 0.45f)
                        lineTo(w * 0.4f, h * 0.25f)
                        lineTo(w * 0.7f, h * 0.25f)
                        lineTo(w * 0.85f, h * 0.45f)
                        close()
                    }
                    drawPath(carTop, Color(0xFF64B5F6))
                    // Car Body
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF29B6F6), Color(0xFF0277BD))
                        ),
                        topLeft = Offset(w * 0.1f, h * 0.45f),
                        size = Size(w * 0.8f, h * 0.28f)
                    )
                    // Wheels
                    drawCircle(Color.DarkGray, radius = w * 0.1f, center = Offset(w * 0.3f, h * 0.73f))
                    drawCircle(Color.LightGray, radius = w * 0.05f, center = Offset(w * 0.3f, h * 0.73f))
                    drawCircle(Color.DarkGray, radius = w * 0.1f, center = Offset(w * 0.7f, h * 0.73f))
                    drawCircle(Color.LightGray, radius = w * 0.05f, center = Offset(w * 0.7f, h * 0.73f))
                }
                "sun", "star" -> {
                    // Sun 3D
                    val sunCenter = Offset(w * 0.5f, h * 0.5f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFF176), Color(0xFFF57F17)),
                            center = sunCenter
                        ),
                        radius = w * 0.28f,
                        center = sunCenter
                    )
                    // Sun rays
                    for (i in 0 until 8) {
                        val angle = (i * 45) * Math.PI / 180
                        val start = Offset((sunCenter.x + Math.cos(angle) * w * 0.32).toFloat(), (sunCenter.y + Math.sin(angle) * w * 0.32).toFloat())
                        val end = Offset((sunCenter.x + Math.cos(angle) * w * 0.44).toFloat(), (sunCenter.y + Math.sin(angle) * w * 0.44).toFloat())
                        drawLine(Color(0xFFFFB300), start = start, end = end, strokeWidth = 8f)
                    }
                }
                "duck" -> {
                    // Yellow Duck Head & Body 3D
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFEE58), Color(0xFFFBC02D)),
                            center = Offset(w * 0.5f, h * 0.45f)
                        ),
                        radius = w * 0.32f,
                        center = Offset(w * 0.5f, h * 0.52f)
                    )
                    // Duck Head
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFF59D), Color(0xFFFBC02D)),
                            center = Offset(w * 0.4f, h * 0.32f)
                        ),
                        radius = w * 0.22f,
                        center = Offset(w * 0.45f, h * 0.35f)
                    )
                    // Duck Beak (Orange)
                    val beakPath = Path().apply {
                        moveTo(w * 0.25f, h * 0.38f)
                        lineTo(w * 0.08f, h * 0.42f)
                        lineTo(w * 0.25f, h * 0.48f)
                        close()
                    }
                    drawPath(beakPath, Color(0xFFFF6F00))
                    // Duck Eye
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.4f, h * 0.32f))
                    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.38f, h * 0.3f))
                }
                "dog" -> {
                    // Puppy Head 3D
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFB74D), Color(0xFF8D6E63)),
                            center = Offset(w * 0.5f, h * 0.45f)
                        ),
                        radius = w * 0.35f,
                        center = Offset(w * 0.5f, h * 0.5f)
                    )
                    // Flappy Ears
                    drawCircle(Color(0xFF5D4037), radius = w * 0.12f, center = Offset(w * 0.18f, h * 0.4f))
                    drawCircle(Color(0xFF5D4037), radius = w * 0.12f, center = Offset(w * 0.82f, h * 0.4f))
                    // Eyes
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.36f, h * 0.45f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.36f, h * 0.45f))
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.64f, h * 0.45f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.64f, h * 0.45f))
                    // Black nose & Tongue
                    drawCircle(Color.Black, radius = w * 0.05f, center = Offset(w * 0.5f, h * 0.58f))
                    drawCircle(Color(0xFFFF80AB), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.68f))
                }
                "dinosaur" -> {
                    // Green Dinosaur Head
                    val dinoPath = Path().apply {
                        moveTo(w * 0.25f, h * 0.7f)
                        lineTo(w * 0.25f, h * 0.3f)
                        quadraticTo(w * 0.5f, h * 0.1f, w * 0.8f, h * 0.35f)
                        quadraticTo(w * 0.85f, h * 0.5f, w * 0.65f, h * 0.6f)
                        lineTo(w * 0.65f, h * 0.75f)
                        close()
                    }
                    drawPath(
                        path = dinoPath,
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
                            center = Offset(w * 0.5f, h * 0.4f)
                        )
                    )
                    // Cute eye
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.6f, h * 0.35f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.6f, h * 0.35f))
                    // Spikes
                    for (i in 0..3) {
                        val spike = Path().apply {
                            moveTo(w * (0.25f - i * 0.02f), h * (0.3f + i * 0.1f))
                            lineTo(w * (0.12f - i * 0.02f), h * (0.35f + i * 0.1f))
                            lineTo(w * (0.25f - i * 0.02f), h * (0.4f + i * 0.1f))
                            close()
                        }
                        drawPath(spike, Color(0xFFFFB74D))
                    }
                }
                "dragon" -> {
                    // Red Dragon Head & Wings
                    val dragonHead = Path().apply {
                        moveTo(w * 0.3f, h * 0.65f)
                        quadraticTo(w * 0.2f, h * 0.35f, w * 0.5f, h * 0.2f)
                        quadraticTo(w * 0.85f, h * 0.3f, w * 0.75f, h * 0.65f)
                        close()
                    }
                    drawPath(
                        path = dragonHead,
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFF5252), Color(0xFFB71C1C)),
                            center = Offset(w * 0.5f, h * 0.4f)
                        )
                    )
                    // Golden Horns
                    val hornL = Path().apply {
                        moveTo(w * 0.38f, h * 0.25f)
                        lineTo(w * 0.25f, h * 0.08f)
                        lineTo(w * 0.45f, h * 0.22f)
                        close()
                    }
                    val hornR = Path().apply {
                        moveTo(w * 0.62f, h * 0.25f)
                        lineTo(w * 0.75f, h * 0.08f)
                        lineTo(w * 0.55f, h * 0.22f)
                        close()
                    }
                    drawPath(hornL, Color(0xFFFFD54F))
                    drawPath(hornR, Color(0xFFFFD54F))
                    // Fiery Eyes
                    drawCircle(Color(0xFFFFEB3B), radius = w * 0.07f, center = Offset(w * 0.42f, h * 0.42f))
                    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.42f, h * 0.42f))
                    drawCircle(Color(0xFFFFEB3B), radius = w * 0.07f, center = Offset(w * 0.58f, h * 0.42f))
                    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.58f, h * 0.42f))
                }
                "lion" -> {
                    // Lion Mane (Orange Sunburst)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFF9800), Color(0xFFE65100)),
                            center = Offset(w * 0.5f, h * 0.5f)
                        ),
                        radius = w * 0.42f,
                        center = Offset(w * 0.5f, h * 0.5f)
                    )
                    // Lion Face (Gold Yellow)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFE082), Color(0xFFFFB300)),
                            center = Offset(w * 0.5f, h * 0.5f)
                        ),
                        radius = w * 0.28f,
                        center = Offset(w * 0.5f, h * 0.52f)
                    )
                    // Eyes & Nose
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.48f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.48f))
                    drawCircle(Color(0xFF5D4037), radius = w * 0.045f, center = Offset(w * 0.5f, h * 0.58f))
                }
                "bee" -> {
                    // Yellow/Black Striped Bee
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFEB3B), Color(0xFFFBC02D)),
                            center = Offset(w * 0.5f, h * 0.5f)
                        ),
                        radius = w * 0.32f,
                        center = Offset(w * 0.5f, h * 0.52f)
                    )
                    // Stripes
                    drawRect(Color.Black, topLeft = Offset(w * 0.25f, h * 0.44f), size = Size(w * 0.5f, h * 0.06f))
                    drawRect(Color.Black, topLeft = Offset(w * 0.28f, h * 0.58f), size = Size(w * 0.44f, h * 0.06f))
                    // Wings
                    drawCircle(Color(0x80E0F7FA), radius = w * 0.16f, center = Offset(w * 0.32f, h * 0.28f))
                    drawCircle(Color(0x80E0F7FA), radius = w * 0.16f, center = Offset(w * 0.68f, h * 0.28f))
                    // Cute Eyes
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.38f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.38f))
                }
                "rabbit" -> {
                    // Bunny Face 3D
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White, Color(0xFFF5F5F5)),
                            center = Offset(w * 0.5f, h * 0.5f)
                        ),
                        radius = w * 0.32f,
                        center = Offset(w * 0.5f, h * 0.58f)
                    )
                    // Long Ears
                    drawRect(Color.White, topLeft = Offset(w * 0.34f, h * 0.1f), size = Size(w * 0.1f, h * 0.36f))
                    drawRect(Color.White, topLeft = Offset(w * 0.56f, h * 0.1f), size = Size(w * 0.1f, h * 0.36f))
                    drawRect(Color(0xFFFF80AB), topLeft = Offset(w * 0.36f, h * 0.14f), size = Size(w * 0.06f, h * 0.28f))
                    drawRect(Color(0xFFFF80AB), topLeft = Offset(w * 0.58f, h * 0.14f), size = Size(w * 0.06f, h * 0.28f))
                    // Eyes & Pink nose
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.4f, h * 0.54f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.6f, h * 0.54f))
                    drawCircle(Color(0xFFFF80AB), radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.62f))
                }
                "tiger" -> {
                    // Tiger Face
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFF9800), Color(0xFFE65100)),
                            center = Offset(w * 0.5f, h * 0.5f)
                        ),
                        radius = w * 0.35f,
                        center = Offset(w * 0.5f, h * 0.5f)
                    )
                    // Tiger Stripes
                    drawRect(Color.Black, topLeft = Offset(w * 0.45f, h * 0.2f), size = Size(w * 0.1f, h * 0.12f))
                    drawRect(Color.Black, topLeft = Offset(w * 0.2f, h * 0.45f), size = Size(w * 0.12f, h * 0.06f))
                    drawRect(Color.Black, topLeft = Offset(w * 0.68f, h * 0.45f), size = Size(w * 0.12f, h * 0.06f))
                    // Eyes & Snout
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.36f, h * 0.48f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.36f, h * 0.48f))
                    drawCircle(Color.White, radius = w * 0.07f, center = Offset(w * 0.64f, h * 0.48f))
                    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.64f, h * 0.48f))
                    drawCircle(Color.Black, radius = w * 0.045f, center = Offset(w * 0.5f, h * 0.6f))
                }
                else -> {
                    // Default Friendly Star Badge
                    val starCenter = Offset(w * 0.5f, h * 0.5f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFD54F), Color(0xFFFF8F00)),
                            center = starCenter
                        ),
                        radius = w * 0.36f,
                        center = starCenter
                    )
                    drawCircle(Color.White, radius = w * 0.06f, center = Offset(w * 0.4f, h * 0.42f))
                    drawCircle(Color.White, radius = w * 0.06f, center = Offset(w * 0.6f, h * 0.42f))
                    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.4f, h * 0.42f))
                    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.6f, h * 0.42f))
                    // Smile
                    drawArc(
                        color = Color(0xFF5D4037),
                        startAngle = 20f,
                        sweepAngle = 140f,
                        useCenter = false,
                        topLeft = Offset(w * 0.38f, h * 0.52f),
                        size = Size(w * 0.24f, h * 0.2f),
                        style = Stroke(width = 6f)
                    )
                }
            }
        }
    }
}
