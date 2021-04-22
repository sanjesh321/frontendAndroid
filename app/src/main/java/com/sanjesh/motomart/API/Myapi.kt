package com.sanjesh.motomart.API

import com.example.Motomart.entity.Product
import com.sanjesh.motomart.Entity.User
import com.sanjesh.motomart.Response.LoginResponse
import com.sanjesh.motomart.Response.ProductResponse
import com.sanjesh.motomart.Response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface Myapi {
    @POST("/register/user")
    suspend fun registerUser(@Body user: User): Response<UserResponse>

    @FormUrlEncoded

    @POST("/login/User")
    suspend fun checkUser(
        @Field("Email") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("regProduct")
    suspend fun registerProduct(@Body product: Product): Response<ProductResponse>


    @Multipart
    @POST("/update/profPicture")
    suspend fun uploadImage(
        @Header("Authorization") token:String,
        @Part file: MultipartBody.Part
    ): Response<LoginResponse>

    @FormUrlEncoded
    @PUT("Updateuser")
    suspend fun editDetails(
        @Header("Authorization") token:String,
        @Field("fn") fn:String,
        @Field("ln") ln:String,
        @Field("un") un:String,
        @Field("em") em:String
    ): Response<LoginResponse>
}
