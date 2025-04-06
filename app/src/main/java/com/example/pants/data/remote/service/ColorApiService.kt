package com.example.pants.data.remote.service

import com.example.pants.data.dto.ColorResponse
import com.example.pants.data.dto.ColorsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ColorApiService {
    @GET("scheme")
    suspend fun getColor(
        @Query("hsl") hsl: String,
        @Query("count") count: Int,
        @Query("mode") mode: String
    ): ColorsListResponse
    @GET("id")
    suspend fun getColorById(
        @Query("hsl") hsl: String,
    ): ColorResponse
}
