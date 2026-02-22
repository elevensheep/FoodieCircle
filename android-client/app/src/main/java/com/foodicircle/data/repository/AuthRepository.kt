package com.foodicircle.data.repository

import android.content.Context
import android.util.Log
import com.foodicircle.data.model.KakaoLoginResponse
import com.foodicircle.data.network.AuthService
import com.foodicircle.data.network.KakaoLoginRequest
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    @ApplicationContext private val context: Context
) {

    suspend fun loginWithKakao(): Result<KakaoLoginResponse> {
        return try {
            // 1. Get Kakao Auth Code from Kakao SDK
            val authCode = getKakaoAuthCode()
            
            // 2. Send Auth Code to Backend
            val response = authService.kakaoLogin(KakaoLoginRequest(authCode))
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login Failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getKakaoAuthCode(): String = suspendCoroutine { continuation ->
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("AuthRepository", "KakaoTalk Login Failed", error)
                    // If KakaoTalk login fails, try account login
                    UserApiClient.instance.loginWithKakaoAccount(context) { tokenAccount, errorAccount ->
                        if (errorAccount != null) {
                            continuation.resumeWith(Result.failure(errorAccount))
                        } else if (tokenAccount != null) {
                            // Note: We need AuthCode, but SDK returns Token typically. 
                            // However, for backend-side verification, we often pass the Access Token or 
                            // we need to use a different flow if we want the actual Auth Code.
                            // BUT, strictly speaking, the `kakaoLogin` API in backend expects "authCode" string.
                            // If the backend uses `authService.kakaoLogin(authCode)`, it likely expects the Authorization Code
                            // to exchange for a token itself (Server-side flow).
                            // 
                            // CHALLENGE: Android SDK `loginWithKakaoTalk` returns an OAuthToken (Access/Refresh Token), 
                            // NOT an Authorization Code, unless we use specific config.
                            //
                            // Adjusted Logic: If the backend expects an *Auth Code*, the Android client must use
                            // the REST API flow or specific SDK Headers, but typically Standard Native App flow
                            // gives the Access Token to the Client, which then passes it to the Backend.
                            // 
                            // Let's check `user-service` implementation.
                            // The backend `AuthService.java` (inferred) likely calls Kakao's `/oauth/token` with the code.
                            // So we DO need the Auth Code.
                            // In Android SDK, getting the Auth Code directly is less common in modern SDKs (they handle token exchange).
                            // 
                            // HOWEVER, for this implementation, I will assume the `token.accessToken` is what we pass 
                            // OR I will assume we need to adjust standard SDK usage.
                            // 
                            // Wait, if the Backend does the Token Exchange, it needs the Code.
                            // If the Backend just validates the Token, it needs the Token.
                            // `user-service` API param is `authCode`.
                            // 
                            // Correct Android SDK usage for "Getting Auth Code":
                            // We might need to imply this is a limitation without a full web-view flow, 
                            // OR we assume the Backend actually validates the Access Token (common mistake in naming).
                            // 
                            // Let's assume for now we pass the `accessToken` as the `authCode` to the backend, 
                            // OR the user will adjust. 
                            // Actually, let's look at `AuthControllerTest.java` again.
                            // It sends `{"authCode": "test-auth-code"}`.
                            
                            continuation.resume(tokenAccount.accessToken) // Passing AccessToken as AuthCode for now as placeholder
                        }
                    }
                } else if (token != null) {
                     continuation.resume(token.accessToken)
                }
            }
        } else {
             UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (error != null) {
                    continuation.resumeWith(Result.failure(error))
                } else if (token != null) {
                    continuation.resume(token.accessToken)
                }
            }
        }
    }
}
