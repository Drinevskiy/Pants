package com.example.pants.presentation.ui.picker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.pants.domain.model.ColorModel

@Composable
internal fun ColorBoardPreview(
    colors: List<ColorModel>,
    modifier: Modifier = Modifier,
) {
    Column (
        modifier = modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        colors.forEach {
            BorderedBox(it)
        }
    }
}

@Composable
private fun BorderedBox(color: ColorModel) {
    fun darkenColor(color: Color) =
        Color(ColorUtils.blendARGB(color.toArgb(), Color.Black.toArgb(), 0.5f))
    fun ColorModel.asComposeColor() =
        guessHue?.let { hue -> Color.hsv(hue, 1f, 1f) } ?: Color.Gray

    val infillColor = color.asComposeColor()
    val outlineColor = darkenColor(color.asComposeColor())
    val colors = listOf(outlineColor, infillColor)
    Box(contentAlignment = Alignment.Center) {
        colors.forEach { colorToDraw ->
            val size = when(colorToDraw) {
                outlineColor -> 38.dp
                infillColor -> 32.dp
                else -> 32.dp
            }
            Surface (
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(12.dp)),
                color = colorToDraw,
            ) { }
        }
    }
}
