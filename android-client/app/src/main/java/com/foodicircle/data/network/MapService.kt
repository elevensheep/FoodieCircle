package com.foodicircle.data.network

import com.foodicircle.data.model.CircleResponse
import com.foodicircle.data.model.RestaurantSearchResponse
import com.example.common.dto.ApiResponse // Assuming we copy this or create a matching generic
// detailed note: Android client doesn't share 'common' module directly easily without config. 
// I should probably define a generic wrapper in Android client or just use the raw response data classes if simplified.
// For now, I'll define a generic wrapper locally in Android to match backend.
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MapService {
    @GET("/api/map/groups")
    suspend fun getMyCircles(
        @Header("X-User-Id") userId: Long
    ): Response<ApiResponseWrapper<List<CircleResponse>>>

    @GET("/api/map/restaurants/search")
    suspend fun searchRestaurants(
        @Query("keyword") keyword: String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("radius") radius: Int
    ): Response<ApiResponseWrapper<List<RestaurantSearchResponse>>>
}

// Helper wrapper to match backend ApiResponse structure
data class ApiResponseWrapper<T>(
    val status: String,
    val message: String,
    val data: T
)
