package com.sanjesh.motomart.API

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

abstract class MyApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()//call garni kam garxa
        if (response.isSuccessful) {//body ko response
            return response.body()!!
        } else {
            //   val error: String = response.errorBody().toString()
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            println(error)
            message.append("Error code : ${response.code()}")
            throw IOException(message.toString())
        }
    }
}