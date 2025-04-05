package com.example.pants.presentation.utils.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
@OptIn(InternalAnimationApi::class)
fun animatedGradientTransition(color: Color): Pair<Color, Brush> {

    val transition = updateTransition(color, label = "color state")
    val animatedColor by transition.animateColor(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = "color preview",
    ) { it }

    val blendPosition by remember { derivedStateOf { 1 - transition.playTimeNanos.toFloat() / transition.totalDurationNanos.toFloat() } }

    val colors = arrayOf(
        0.0f to transition.currentState,
        blendPosition to animatedColor,
        1f to transition.targetState
    )

    val animatedGradient = Brush.linearGradient(
        colorStops = colors,
        start = Offset(Float.POSITIVE_INFINITY, 0f),
        end = Offset(0f, Float.POSITIVE_INFINITY),
    )
    return Pair(animatedColor, animatedGradient)
}
