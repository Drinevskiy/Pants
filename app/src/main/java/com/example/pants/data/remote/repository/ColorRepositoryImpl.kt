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
        apiService.getColor(generateRandomColor(), count * 2, "quad").colors
            .asSequence()
            .distinctBy { it.name.value }
            .filterNot { it.name.value.lowercase() in COMMON_USE_NAMES }
            .map { it.toColorModel() }
            .take(count)
            .toSet()
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
