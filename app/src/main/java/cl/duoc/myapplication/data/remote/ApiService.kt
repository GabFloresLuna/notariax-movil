package cl.duoc.myapplication.data.remote

import cl.duoc.myapplication.model.AuthResponse
import cl.duoc.myapplication.model.LoginRequest
import cl.duoc.myapplication.model.SignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse
}
