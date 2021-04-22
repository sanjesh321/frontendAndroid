package com.sanjesh.motomart.Response

import com.sanjesh.motomart.Entity.User

data class LoginResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val token:String?=null,
    val data: User?=null
)