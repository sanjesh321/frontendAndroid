package com.sanjesh.motomart.Repository

import android.content.Context
import com.example.Motomart.entity.Product
import com.sanjesh.motomart.API.MyApiRequest
import com.sanjesh.motomart.API.Productapi
import com.sanjesh.motomart.API.Servicebuilder
import com.sanjesh.motomart.DB.UserDB

import com.sanjesh.motomart.Response.ProductResponse

class ProductRepo: MyApiRequest() {
    val MyAPI = Servicebuilder.buildServices(Productapi::class.java)

    suspend fun retrieveProducts():ProductResponse{
        return apiRequest {
            MyAPI.retrieveProducts()
        }
    }
    suspend fun insertBulkProduct(context: Context, products: List<Product>){
        //Delete all students
        UserDB.getInstance(context).getProductDAO().deleteAll()
        //Insert all data into DB
        UserDB.getInstance(context).getProductDAO().addProduct(products as MutableList<Product>)
    }
    suspend fun getAllProductFromRoom(context: Context):MutableList<Product>{
        return UserDB.getInstance(context).getProductDAO().getAllProduct()
    }



}