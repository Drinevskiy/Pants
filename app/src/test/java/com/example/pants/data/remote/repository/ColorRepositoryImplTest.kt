package com.example.pants.data.remote.repository

import com.example.pants.data.dto.ColorResponse
import com.example.pants.data.dto.ColorsListResponse
import com.example.pants.data.dto.Hsv
import com.example.pants.data.dto.Name
import com.example.pants.data.remote.service.ColorApiService
import com.example.pants.domain.model.ColorModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random


class ColorRepositoryImplTest {

    private var mockApiService: ColorApiService = mockk<ColorApiService>()
    private var repository: ColorRepositoryImpl = ColorRepositoryImpl(mockApiService)

    @Test
    fun `should return correct number of colors`() = runTest {
        val mockColors = List(10) { i ->
            ColorResponse(
                name = Name("color $i"),
                hsv = Hsv(Hsv.Fraction(0.5f, 0.6f, 0.7f))
            )
        }
        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(mockColors)

        val count = 5
        val result = repository.getDistinctRandomColors(count).getOrThrow()

        assertEquals(count, result.size)
    }

    @Test
    fun `should throw error when not enough colors`() = runTest {
        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(emptyList())
        assertThrows<IllegalStateException> {
            repository.getDistinctRandomColors(5).getOrThrow()
        }
    }

    @Test
    fun `should return collection of correct data type`() = runTest {
        val mockColors = List(10) { i ->
            ColorResponse(
                name = Name("color $i"),
                hsv = Hsv(Hsv.Fraction(0.5f, 0.6f, 0.7f))
            )
        }
        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(mockColors)

        val count = 7
        val result = repository.getDistinctRandomColors(count).getOrThrow()

        assertEquals(count, result.size)

        assertInstanceOf(Set::class.java, result)

        result.forEach { element ->
            assertInstanceOf(ColorModel::class.java, element)
        }
    }

    @Test
    fun `should filter invalid saturation`() = runTest {
        val commonColor = ColorResponse(
            name = Name("color 1"),
            hsv = Hsv(Hsv.Fraction(0.5f, 0.6f, 0.7f))
        )

        val invalidSaturationColor = ColorResponse(
            name = Name("color 2"),
            hsv = Hsv(Hsv.Fraction(0.5f, 0.3f, 0.7f))
        )

        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(
            listOf(commonColor, invalidSaturationColor)
        )

        val result = repository.getDistinctRandomColors(2).getOrThrow()

        assertEquals(2, result.size)
        assertFalse(result.all { it.saturation > 0.3f })
    }

    @Test
    fun `should filter invalid brightness`() = runTest {
        val commonColor = ColorResponse(
            name = Name("color 1"),
            hsv = Hsv(Hsv.Fraction(0.5f, 0.6f, 0.7f))
        )

        val invalidBrightnessColor = ColorResponse(
            name = Name("color 2"),
            hsv = Hsv(Hsv.Fraction(0.98f, 0.76f, 0.4f))
        )

        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(
            listOf(commonColor, invalidBrightnessColor)
        )

        val result = repository.getDistinctRandomColors(2).getOrThrow()

        assertEquals(2, result.size)
        assertFalse(result.all { it.value > 0.4f })
    }

    @Test
    fun `should contain unique colors name`() = runTest {
        val mockColors = MutableList(10) { i ->
            ColorResponse(
                name = Name("color $i"),
                hsv = Hsv(Hsv.Fraction(0.5f, 0.6f, 0.7f))
            )
        }

        mockColors.add(
            ColorResponse(
                name = Name("color 2"),
                hsv = Hsv(Hsv.Fraction(0.98f, 0.76f, 0.4f))
            )
        )

        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(mockColors)

        val count = 11
        val result = repository.getDistinctRandomColors(count).getOrThrow()

        assertEquals(count, result.size)
        assertFalse(result.map { it.name }.distinct().size == result.size)
    }

    @Test
    fun `should not contain common use names`() = runTest {
        val mockColors = MutableList(10) {
            ColorResponse(
                name = Name(COMMON_USE_NAMES.elementAt(Random.nextInt(0, COMMON_USE_NAMES.size))),
                hsv = Hsv(Hsv.Fraction(0.5f, 0.6f, 0.7f))
            )
        }
        coEvery { mockApiService.getColor(any(), any(), any()) } returns ColorsListResponse(mockColors)

        assertThrows<IllegalStateException> {
            repository.getDistinctRandomColors(5).getOrThrow()
        }
    }

    private companion object{
        val COMMON_USE_NAMES = setOf(
            "beige", "black", "blue violet", "blue", "brown", "crimson", "cyan", "gold",
            "gray", "green", "indigo", "khaki", "lavender", "lime green", "magenta", "maroon",
            "navy blue", "olive", "orange", "pink", "plum", "purple", "red", "salmon", "silver",
            "sky blue", "teal", "violet", "white", "yellow"
        )
    }
}