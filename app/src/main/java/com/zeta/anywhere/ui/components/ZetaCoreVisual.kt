package com.zeta.anywhere.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.ui.theme.ZetaIndigo
import com.zeta.anywhere.ui.theme.ZetaPurple
import com.zeta.anywhere.ui.theme.ZetaViolet

@Composable
fun ZetaCoreVisual(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "zetaCore")
    val pulse = transition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.size(132.dp)) {
        val r = size.minDimension / 2f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    ZetaViolet.copy(alpha = 0.95f),
                    ZetaPurple.copy(alpha = 0.7f),
                    ZetaIndigo.copy(alpha = 0.45f),
                    Color.Transparent
                ),
                center = center,
                radius = r * pulse.value
            ),
            radius = r * pulse.value
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.18f),
            radius = r * 0.28f,
            center = Offset(center.x - r * 0.25f, center.y - r * 0.25f)
        )
    }
}
