package com.sanjesh.motomart.Repository

import com.sanjesh.motomart.API.Cartapi
import com.sanjesh.motomart.API.MyApiRequest
import com.sanjesh.motomart.API.Servicebuilder
import com.sanjesh.motomart.Response.CartResponse

class CartRepo (): MyApiRequest() {
    val MyAPI = Servicebuilder.buildServices(Cartapi::class.java)
    suspend fun addToCart(id:String, quantity:String):CartResponse{
        return apiRequest {
            MyAPI.addToCart(Servicebuilder.token!!,id,quantity)
        }
    }
    suspend fun retrieveCart(): CartResponse {
        return apiRequest {
            MyAPI.retrieveCart(Servicebuilder.token!!)
        }
    }
    suspend fun updateCart(id: String, qty: Int): CartResponse {
        return apiRequest {
            MyAPI.updateCart(Servicebuilder.token!!, id, qty)
        }
    }
    suspend fun deleteCart(id: String): CartResponse {
        return apiRequest {
            MyAPI.deleteCart(Servicebuilder.token!!, id)
        }
    }

}