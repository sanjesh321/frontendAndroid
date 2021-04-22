package com.sanjesh.motomart.Response

import com.sanjesh.motomart.Entity.Cart

data class CartResponse(
    val success:Boolean?=null,
    val message:String?=null,
    val data:MutableList<Cart>?=null)
