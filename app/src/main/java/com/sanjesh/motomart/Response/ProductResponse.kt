package com.sanjesh.motomart.Response

import com.example.Motomart.entity.Product


data class ProductResponse(
    val success:Boolean?=null,

    val data:MutableList<Product>?=null
)