package com.sanjesh.motomart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.sanjesh.motomart.Entity.User
import com.sanjesh.motomart.Repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUp : AppCompatActivity() {

    private lateinit var etFn: EditText
    private lateinit var etln: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPass: EditText
    private lateinit var etConfPass: EditText
    private lateinit var already: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        etFn = findViewById(R.id.etFn)
        etln = findViewById(R.id.etln)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPass = findViewById(R.id.etPass)
        etConfPass = findViewById(R.id.etConfPass)
        already = findViewById(R.id.already)


        already.setOnClickListener{
            val intent = Intent(this@SignUp,LogInAct::class.java)
            startActivity(intent)

        }

    }

    fun registerRoute(view: View) {
        val fn = etFn.text.toString()
        val ln = etln.text.toString()
        val userName = etUsername.text.toString()
        val email = etEmail.text.toString()
        val Password = etPass.text.toString()
        val conPassword = etConfPass.text.toString()
        if(Password != conPassword){
            etPass.error = "Password don't match"
            etPass.requestFocus()
        }
        else{
            val user = User(Firstname=fn, Lastname=ln, Username =userName, Email =email, password =Password)
            try{


                CoroutineScope(Dispatchers.IO).launch {
                    val userRepo = UserRepo()
                    val response = userRepo.registerUser(user)
                    if(response.message==true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUp, "User Registered", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUp,LogInAct::class.java)
                            startActivity(intent)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUp,"Error", Toast.LENGTH_SHORT).show()

                        }
                    }

                }

            }
            catch(ex:Exception)
            {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@SignUp, "User already exists", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }


    }
}