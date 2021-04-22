package com.sanjesh.motomart.Repository

import com.sanjesh.motomart.API.MyApiRequest
import com.sanjesh.motomart.API.Myapi
import com.sanjesh.motomart.API.Servicebuilder
import com.sanjesh.motomart.Entity.User
import com.sanjesh.motomart.Response.LoginResponse
import com.sanjesh.motomart.Response.UserResponse
import okhttp3.MultipartBody

class UserRepo: MyApiRequest() {
    val MyAPI= Servicebuilder.buildServices(Myapi::class.java)

    suspend fun registerUser(user : User): UserResponse {
        return apiRequest {
            MyAPI.registerUser(user)
        }
    }
    suspend fun checkUser(username : String, password : String): LoginResponse {
        return apiRequest {
            MyAPI.checkUser(username, password)
        }
    }
    suspend fun uploadImage( body: MultipartBody.Part)
            : LoginResponse {
        return apiRequest {
            MyAPI.uploadImage(Servicebuilder.token!!, body)
        }
    }
    suspend fun editUser(fn:String,ln:String,em:String,un:String):LoginResponse {
        return apiRequest {
            MyAPI.editDetails(Servicebuilder.token!!, fn, ln, em, un)
        }
    }}
