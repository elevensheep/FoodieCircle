package com.foodicircle.data.model

// Friend (Mock)
data class Friend(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val starCount: Int,
    val recentActivity: String
)

// Group (Circle)
data class CircleResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val memberCount: Int,
    val restaurantCount: Int,
    val leaderName: String
)

// Restaurant Search
data class RestaurantSearchResponse(
    val id: Long,
    val name: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val reviewCount: Int
)
