package com.example.pants.presentation.ui.picker.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pants.domain.model.ColorModel
import com.example.pants.presentation.utils.animation.animatedGradientTransition
import com.example.pants.presentation.utils.extension.other.hue

@Composable
internal fun PickerContent(
    selectedColor: Color,
    onHueChange: (Float) -> Unit,
    colors: List<ColorModel>,
) {
    val (animatedColor, animatedGradient) = animatedGradientTransition(selectedColor)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(48.dp),
        modifier = Modifier.width(IntrinsicSize.Min),
    ) {
        Previews(
            colors = colors,
            animatedColor = animatedColor,
            animatedGradient = animatedGradient,
            modifier = Modifier.fillMaxWidth(),
        )
        HuePicker(hue = { selectedColor.hue }, onHueChange = onHueChange)
    }
}

@Preview
@Composable
fun PickerContentPreview() {
    val model = ColorModel(
        name = "Color of your pants on fire on saturday morning",
        realHue = 227.0f,
        saturation = 1.0f,
        value = 1.0f,
        guessHue = null,
    )

    PickerContent(
        selectedColor = Color.Yellow,
        onHueChange = { _ -> },
        colors = List(5) { model }
    )
}
