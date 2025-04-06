package com.example.pants.data.utils

import kotlin.math.min
import kotlin.random.Random

private const val MIN_SATURATION = 0.3
private const val MIN_VALUE = 0.4

fun generateRandomColor(): String {
    val hue = Random.nextDouble()
    val saturation = Random.nextDouble(MIN_SATURATION, 1.0)
    val value = Random.nextDouble(MIN_VALUE, 1.0)
    return hsvToHsl(hue, saturation, value)
}

fun hsvToHsl(hue: Double, saturation: Double, value: Double): String{
    val lightness = value * (1 - saturation / 2)
    val newSaturation = if (lightness == 0.0 || lightness == 1.0) 0.0 else (value - lightness) / min(lightness, 1 - lightness)
    return "$hue,$newSaturation,$lightness"
}
