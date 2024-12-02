package com.example.projectadvocata.data.retrofit

import com.example.projectadvocata.data.response.LoginResponse
import com.example.projectadvocata.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("/v1/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        password1: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/v1/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse


}