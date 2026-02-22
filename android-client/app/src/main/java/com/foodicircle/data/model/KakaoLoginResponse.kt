package com.foodicircle.data.model

data class KakaoLoginResponse(
    val status: Int,
    val data: LoginData?,
    val message: String?
)

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
    val nickname: String,
    val newMember: Boolean
)
