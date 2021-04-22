package com.sanjesh.motomart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SharedPreference : AppCompatActivity() {
    val username = "Bishesh"
    val password = "123"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preference)
        saveSharedPref()
        getSharedPref()



    }

    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val username = sharedPref.getString("username","")
        val password = sharedPref.getString("password","")
        Toast.makeText(this, "Username: $username and password : $password", Toast.LENGTH_SHORT).show()

    }

    private fun saveSharedPref() {
        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor= sharedPref.edit()
        editor.putString("username",username)
        editor.putString("password", password)
        editor.apply()
        Toast.makeText(this@SharedPreference, "Username and Password saved", Toast.LENGTH_SHORT).show()

    }
}