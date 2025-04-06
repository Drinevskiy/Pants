package com.example.pants.data.remote.repository

import android.util.Log
import com.example.pants.domain.model.ColorModel
import com.example.pants.domain.repository.ColorRepository
import com.example.pants.data.utils.generateRandomColor
import com.example.pants.domain.mapper.toColorModel
import com.example.pants.data.remote.service.ColorApiService
import kotlinx.collections.immutable.toImmutableSet
import java.util.Locale

class ColorRepositoryImpl(
    private val apiService: ColorApiService,
) : ColorRepository {

    override suspend fun getDistinctRandomColors(count: Int): Result<Set<ColorModel>> = runCatching {
        val requiredColors = mutableSetOf<ColorModel>()
        var attempts = 0
        val maxAttempts = 3

        while (requiredColors.size < count && attempts < maxAttempts) {
            val apiResult = apiService.getColor(
                hsl = generateRandomColor(),
                count = (count - requiredColors.size) * 2,
                mode = "quad"
            ).colors

            val processedColors = apiResult
                .asSequence()
                .distinctBy { it.name.value }
                .filterNot { color -> color.name.value.lowercase() in COMMON_USE_NAMES }
                .map { it.toColorModel() }
                .filter { colorModel -> colorModel.saturation > 0.3 && colorModel.value > 0.4 }
                .toList()

            processedColors.take(count - requiredColors.size).forEach { color ->
                requiredColors.add(color)
            }

            attempts++
        }

        if (requiredColors.size < count) {
            throw IllegalStateException("Could not fetch enough unique colors after $maxAttempts attempts")
        }

        requiredColors.toSet()
    }

    private companion object {
        val COMMON_USE_NAMES = setOf(
            "beige",
            "black",
            "blue violet",
            "blue",
            "brown",
            "crimson",
            "cyan",
            "gold",
            "gray",
            "green",
            "indigo",
            "khaki",
            "lavender",
            "lime green",
            "magenta",
            "maroon",
            "navy blue",
            "olive",
            "orange",
            "pink",
            "plum",
            "purple",
            "red",
            "salmon",
            "silver",
            "sky blue",
            "teal",
            "violet",
            "white",
            "yellow",
        )
    }
}
