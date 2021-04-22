package com.sanjesh.motomart

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sanjesh.motomart.API.Servicebuilder
import com.sanjesh.motomart.DB.UserDB
import com.sanjesh.motomart.Repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Update : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var etFn: EditText
    private lateinit var etln: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var btnImageUpload: Button
    private lateinit var ivPp: ImageView
    var CAMERA_REQUEST_CODE = 1
    var GALLERY_REQUEST_CODE = 0
    var image_url:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        etFn = findViewById(R.id.etFn)
        etln = findViewById(R.id.etln)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        ivPp = findViewById(R.id.ivPp)
        btnImageUpload = findViewById(R.id.btnUpdateImage)
        ivPp.setOnClickListener{
            showPopUp()
        }
        btnImageUpload.setOnClickListener{
            uploadImage()
        }

        initialize()
    }
    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            var instance = UserDB.getInstance(this@Update).getUserDAO()
            var user = instance.checkUser()
            withContext(Dispatchers.Main)
            {
                if(user.profilepic != null && user.profilepic!="no-img.jpg")
                {
                    var imgPath = Servicebuilder.loadImagePath()+user.profilepic!!.replace("\\","/")
                    Glide.with(this@Update).load(imgPath).into(ivPp)
                }
                else
                {
                    ivPp.setImageResource(R.drawable.img1)
                }

                etFn.setText(user.Firstname)
                etln.setText(user.Firstname)
                etEmail.setText(user.Email)
                etUsername.setText(user.Username)
            }
        }
    }

    private fun uploadImage() {
        if (image_url != "") {
            val file = File(image_url)
            val extension = MimeTypeMap.getFileExtensionFromUrl(image_url)
            val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val req_file = RequestBody.create(MediaType.parse(mimetype), file)
            val body = MultipartBody.Part.createFormData("profilepic", file.name, req_file)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepo()
                    val response = repo.uploadImage(body)
                    if (response.success == true) {
                        var instance = UserDB.getInstance(this@Update).getUserDAO()
                        instance.delete()
                        instance.registerUser(response.data!!)
                        withContext(Dispatchers.Main)
                        {
                            var intent = Intent(this@Update, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(
                                ivPp,
                                "${response.message}",
                                Snackbar.LENGTH_INDEFINITE
                            )
                            snk.setAction("OK", View.OnClickListener {
                                snk.dismiss()
                            })
                            snk.show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main)
                    {
                        println(ex.printStackTrace())
                        val snk = Snackbar.make(
                            ivPp,
                            "Sorry! We are having some problem:(",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snk.setAction("OK", View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
            }
        } else {
            val snk = Snackbar.make(ivPp, "Please Select image", Snackbar.LENGTH_LONG)
            snk.setAction("OK", View.OnClickListener {
                snk.dismiss()
            })
            snk.show()
        }

    }
    private fun showPopUp()
    {
        var popUp = PopupMenu(this,ivPp)
        popUp.menuInflater.inflate(R.menu.gallery_camera,popUp.menu)
        popUp.setOnMenuItemClickListener(this)
        popUp.show()
    }
    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                //overall location of selected image
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                //locator and identifier
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()//moveTONext // movetolast
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                image_url = cursor.getString(columnIndex)
                //image preview
                ivPp.setImageBitmap(BitmapFactory.decodeFile(image_url))
                cursor.close()
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                image_url = file!!.absolutePath
                ivPp.setImageBitmap(BitmapFactory.decodeFile(image_url))
            }
        }
    }

    fun UpdateRoute(view: View) {
        if(validation())
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepo()
                    val response = repo.editUser(etFn.text.toString(),etln.text.toString(),etEmail.text.toString(),etUsername.text.toString())
                    if(response.success == true)
                    {
                        var instance = UserDB.getInstance(this@Update).getUserDAO()
                        instance.delete()
                        instance.registerUser(response.data!!)
                        withContext(Dispatchers.Main)
                        {
                            var intent = Intent(this@Update,MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(etUsername,"${response.message}", Snackbar.LENGTH_LONG)
                            snk.setAction("OK", View.OnClickListener {
                                snk.dismiss()
                            })
                            snk.show()
                        }
                    }
                }
                catch (ex:Exception)
                {
                    println(ex.printStackTrace())
                    withContext(Dispatchers.Main)
                    {
                        val snk = Snackbar.make(etUsername,"${ex.toString()}", Snackbar.LENGTH_LONG)
                        snk.setAction("OK", View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
            }
        }
    }

    private fun validation(): Boolean {
        if(TextUtils.isEmpty(etFn.text)){
            etFn.error = "Insert Firstname"
            etFn.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etEmail.text))
        {
            etEmail.error = "Insert Email"
            etEmail.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etUsername.text))
        {
            etUsername.error = "Insert Username"
            etUsername.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etln.text))
        {
            etln.error = "Insert Lastname"
            etln.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            R.id.menuCamera ->{
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent,CAMERA_REQUEST_CODE)
            }
            R.id.menuGallery ->{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,GALLERY_REQUEST_CODE)
            }
        }
        return true
    }
}