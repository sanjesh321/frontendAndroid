package com.sanjesh.motomart

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sanjesh.motomart.API.Servicebuilder
import com.sanjesh.motomart.DB.UserDB
import com.sanjesh.motomart.Repository.UserRepo
import kotlinx.coroutines.*
import java.lang.Exception

class Slashscreen : AppCompatActivity() {
    var username: String? = null
    var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slashscreen)
        if (!checkInternetConnection()) {
            Toast.makeText(
                this,
                "No Internet connection , Check whether Wi-Fi or Mobile Data is ON",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                getSharedPref()
                if (username != "") {
                    login()
                } else {
                    loadLoginPage()
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            startActivity(
                Intent(
                    this@Slashscreen,
                    LogInAct::class.java
                )
            )
        }
    }
    private fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserRepo()
                val response = repository.checkUser(username!!, password!!)
                if (response.success == true) {
                    // Save token
                    var instance = UserDB.getInstance(this@Slashscreen).getUserDAO()
                    instance.registerUser(response.data!!)
                    Servicebuilder.token = "Bearer ${response.token}"
                    startActivity(
                        Intent(
                            this@Slashscreen,
                            MainActivity::class.java
                        )
                    )
                    finish()
                } else {
                    withContext(Dispatchers.Main) {
                        loadLoginPage()
                    }
                }
            } catch (ex: Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Slashscreen,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
    private fun checkInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("UsernamePasswordPref", MODE_PRIVATE)
        username = sharedPref.getString("username", "")
        password = sharedPref.getString("password", "")
    }
    private fun loadLoginPage() {
        startActivity(
            Intent(
                this@Slashscreen,
                LogInAct::class.java
            )
        )
        finish()
    }
}