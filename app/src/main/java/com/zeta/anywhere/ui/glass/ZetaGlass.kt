package com.zeta.anywhere.ui.glass

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.ui.theme.ZetaIndigo
import com.zeta.anywhere.ui.theme.ZetaPurple
import com.zeta.anywhere.ui.theme.ZetaSurfaceDark
import com.zeta.anywhere.ui.theme.ZetaViolet

enum class ZetaGlassLevel {
    Ambient,
    Interactive,
    Prominent,
    Core
}

@Composable
fun ZetaGlassProvider(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ZetaViolet.copy(alpha = 0.55f),
                        ZetaIndigo.copy(alpha = 0.35f),
                        ZetaSurfaceDark
                    )
                )
            )
            .padding(16.dp)
    ) {
        content()
    }
}

fun Modifier.zetaGlass(
    level: ZetaGlassLevel,
    pressed: Boolean = false,
    cornerRadius: Dp = 24.dp
): Modifier = composed {
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = 420f, dampingRatio = 0.78f),
        label = "zetaPress"
    )

    val alpha = when (level) {
        ZetaGlassLevel.Ambient -> 0.22f
        ZetaGlassLevel.Interactive -> 0.28f
        ZetaGlassLevel.Prominent -> 0.34f
        ZetaGlassLevel.Core -> 0.40f
    }

    val blurRadius = when (level) {
        ZetaGlassLevel.Ambient -> 10f
        ZetaGlassLevel.Interactive -> 14f
        ZetaGlassLevel.Prominent -> 18f
        ZetaGlassLevel.Core -> 22f
    }

    val shape = RoundedCornerShape(cornerRadius)

    Modifier
        .graphicsLayer {
            scaleX = pressScale
            scaleY = pressScale
            compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
            if (Build.VERSION.SDK_INT >= 33) {
                renderEffect = createLiquidShaderEffect(blurRadius, alpha)?.asComposeRenderEffect()
            }
        }
        .clip(shape)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    ZetaPurple.copy(alpha = alpha + 0.10f),
                    ZetaIndigo.copy(alpha = alpha),
                    Color.White.copy(alpha = alpha / 2)
                )
            )
        )
        .shadow(12.dp, shape, clip = false)
        .drawWithContent {
            drawContent()
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.35f),
                        Color.Transparent,
                        ZetaViolet.copy(alpha = 0.25f)
                    )
                ),
                style = Stroke(width = 2f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
            )
        }
}

private fun createLiquidShaderEffect(blurRadius: Float, intensity: Float): RenderEffect? {
    if (Build.VERSION.SDK_INT < 33) return null

    val shader = RuntimeShader(
        """
            uniform shader composable;
            uniform float2 resolution;
            uniform float intensity;
            half4 main(float2 fragCoord) {
                float2 uv = fragCoord / resolution;
                float2 centered = uv - 0.5;
                float lens = smoothstep(0.7, 0.0, length(centered));
                float2 refract = centered * (0.015 * intensity) * lens;
                float3 col;
                col.r = composable.eval(fragCoord + refract * resolution + float2(1.0, 0.0)).r;
                col.g = composable.eval(fragCoord + refract * resolution).g;
                col.b = composable.eval(fragCoord + refract * resolution - float2(1.0, 0.0)).b;
                float highlight = smoothstep(0.5, 0.0, abs(centered.y + centered.x * 0.2)) * 0.18;
                return half4(col + highlight, 1.0);
            }
        """.trimIndent()
    )

    shader.setFloatUniform("resolution", 1080f, 2400f)
    shader.setFloatUniform("intensity", intensity)

    val liquid = RenderEffect.createRuntimeShaderEffect(shader, "composable")
    return RenderEffect.createBlurEffect(
        blurRadius,
        blurRadius,
        liquid,
        Shader.TileMode.CLAMP
    )
}
