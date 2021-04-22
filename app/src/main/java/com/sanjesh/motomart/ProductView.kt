package com.sanjesh.motomart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.Motomart.entity.Product
import com.google.android.material.snackbar.Snackbar
import com.sanjesh.motomart.API.Servicebuilder

import com.sanjesh.motomart.Repository.CartRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductView : AppCompatActivity(), View.OnClickListener {
    private var product: Product? =null
    private lateinit var tvPrice: TextView

    private lateinit var tvProduct: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnAdd: Button
    private lateinit var ivPart: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)
        tvPrice = findViewById(R.id.tvPrice)

        tvDescription = findViewById(R.id.tvDescription)
        tvProduct = findViewById(R.id.tvProduct)
        btnAdd = findViewById(R.id.btnAdd)
        ivPart = findViewById(R.id.ivPart)
        product = intent.getParcelableExtra("product")
        initialize()
        btnAdd.setOnClickListener(this)
    }
    private fun initialize()
    {
        tvPrice.text = "$"+product!!.pprice
        tvDescription.text = product!!.pdesc
        tvProduct.text = product!!.pname

        var imgPath = Servicebuilder.loadImagePath()+product!!.pimage!!.replace("\\","/")
        Glide.with(this@ProductView).load(imgPath).into(ivPart)

    }
    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnAdd->{
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val cartRepository = CartRepo()
                        val response = cartRepository.addToCart(product!!._id,"1")
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                val snk = Snackbar.make(tvProduct,"Product Added to Cart", Snackbar.LENGTH_LONG)
                                snk.setAction("Go to Cart", View.OnClickListener {
                                    snk.dismiss()
                                })
                                snk.show()
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {

                                val snk = Snackbar.make(tvProduct,"${response.message}", Snackbar.LENGTH_LONG)
                                snk.setAction("OK", View.OnClickListener {
                                    snk.dismiss()
                                })
                                snk.show()


                            }
                        }

                    }
                    catch (ex:Exception)
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(tvProduct,"${ex.toString()}", Snackbar.LENGTH_LONG)
                            snk.setAction("OK", View.OnClickListener {
                                snk.dismiss()
                            })
                            snk.show()
                            println(ex.printStackTrace())
                        }

                    }
                }

            }
        }
    }
}