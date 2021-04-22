package com.sanjesh.motomart.API

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Servicebuilder {

    private const val  BASE_URL= "http://10.0.2.2:90/"

    var token:String?=null


    val okhttp = OkHttpClient.Builder()
    val retroFitBuilder = Retrofit.Builder().baseUrl(BASE_URL).
    addConverterFactory(GsonConverterFactory.create())
        .client(okhttp.build())




    val retrofit = retroFitBuilder.build()

    //generic functions
    fun <T> buildServices(serviceType:Class<T>):T{
        return retrofit.create(serviceType)
    }
    fun loadImagePath(): String {
        val arr = BASE_URL.split("/").toTypedArray()
        return arr[0] + "/" + arr[1] + arr[2] + "/"
    }
}