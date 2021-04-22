package com.sanjesh.motomart.API

import com.sanjesh.motomart.Response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface Productapi {
    @GET("product/showall")
    suspend fun retrieveProducts(): Response<ProductResponse>
}
