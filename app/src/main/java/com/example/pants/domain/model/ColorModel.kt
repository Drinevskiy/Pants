package com.example.pants.domain.model

class ColorModel(
    val name: String,
    val realHue: Float,
    val guessHue: Float?,
    val saturation: Float,
    val value: Float,
) {
    fun updateHue(hue: Float?) = ColorModel(
        name = name,
        realHue = realHue,
        guessHue = hue,
        saturation = saturation,
        value = value,
    )

    override fun toString(): String {
        return "ColorModel (name = $name, realHue = $realHue, guessHue = $guessHue, saturation = $saturation, value = $value)"
    }
}
