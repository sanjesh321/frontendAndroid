package com.sanjesh.motomart

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.sanjesh.motomart.API.Servicebuilder
import com.sanjesh.motomart.DB.UserDB
import com.sanjesh.motomart.Notification.NotificationSender
import com.sanjesh.motomart.Repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LogInAct : AppCompatActivity() {
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    private lateinit var etUid: EditText
    private lateinit var etPass: EditText
    private lateinit var cbRemember: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        etUid=findViewById(R.id.etUid)
        etPass=findViewById(R.id.etPass)
        cbRemember=findViewById(R.id.cbRemember)
        checkRunTimePermission()
    }
    private fun saveSharedPref() {
        val sharedPref = getSharedPreferences("credentials", MODE_PRIVATE)
        val editor= sharedPref.edit()
        editor.putString("username",etUid.text.toString())
        editor.putString("password", etPass.text.toString())
        editor.apply()
        Toast.makeText(this@LogInAct, "Username and Password saved", Toast.LENGTH_SHORT).show()

    }
    private fun checkRunTimePermission() {
        if (!hasPermission()) {
            requestPermission()
        }
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@LogInAct, permissions, 1)
    }
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
                break
            }
        }
        return hasPermission
    }
    private fun validate() :Boolean{
        var flag = true
        if(TextUtils.isEmpty(etUid.text)){
            etUid.error = "Enter username"
            etUid.requestFocus()
            flag = false
        }else if(TextUtils.isEmpty(etPass.text)){
            etPass.error = "Enter password"
            etPass.requestFocus()
            flag = false
        }
        return flag
    }

    fun login(view: View) {
        if(validate()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepo()
                    val response = repo.checkUser(etUid.text.toString(),etPass.text.toString())
                    if(response.success == true)
                    {
                        NotificationSender(this@LogInAct,"login successfull!!","${etUid.text.toString()}").createHighPriority()
                        var instance = UserDB.getInstance(this@LogInAct).getUserDAO()
                        instance.registerUser(response.data!!)
                        withContext(Main)
                        {
                            Servicebuilder.token = "Bearer "+response.token
                            if(cbRemember.isChecked){
                                saveSharedPref()
                            }
                            val intent = Intent(this@LogInAct,MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@LogInAct, "Logged in", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        withContext(Main)
                        {

                            Toast.makeText(this@LogInAct, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                catch (ex: Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Main)
                    {
                        Toast.makeText(this@LogInAct, "${ex.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
    fun registerRoute(view: View) {
        val intent = Intent(this,SignUp::class.java)
        startActivity(intent)
    }}
