package com.sanjesh.motomart.Response

import com.sanjesh.motomart.Entity.User

data class UserResponse(
    val message:Boolean?=null,
    val data: User?=null
)