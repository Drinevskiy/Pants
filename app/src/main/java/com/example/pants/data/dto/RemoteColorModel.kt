package com.example.pants.data.dto

data class ColorsListResponse(
    val colors: List<ColorResponse>
)

data class ColorResponse(
    val name: Name,
    val hsv: Hsv
)

data class Name(val value: String)

data class Hsv(val fraction: Fraction) {
    data class Fraction(val h: Float, val s: Float, val v: Float)
}
