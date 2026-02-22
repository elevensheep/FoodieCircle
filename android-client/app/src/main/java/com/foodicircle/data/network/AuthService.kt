package com.foodicircle.data.network

import com.foodicircle.data.model.KakaoLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/user/auth/login/kakao")
    suspend fun kakaoLogin(@Body request: KakaoLoginRequest): Response<KakaoLoginResponse>
}

data class KakaoLoginRequest(
    val authCode: String
)
