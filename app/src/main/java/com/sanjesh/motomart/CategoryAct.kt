package com.sanjesh.motomart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanjesh.motomart.Adapter.ProductAdapter
import com.sanjesh.motomart.Repository.ProductRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CategoryAct : AppCompatActivity() {
    private lateinit var categoryProduct: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        categoryProduct = findViewById(R.id.categoryProduct)
        loadProduct()
    }
    private fun loadProduct(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val productRepository = ProductRepo()
                val response = productRepository.retrieveProducts()
                println(response)
                if (response.success == true){
                    //for room
                    productRepository.insertBulkProduct(this@CategoryAct,response.data!!)

                }
                var lstProduct = productRepository.getAllProductFromRoom(this@CategoryAct)

                withContext(Main){
                    categoryProduct.adapter= ProductAdapter(this@CategoryAct,lstProduct)
                    categoryProduct.layoutManager = LinearLayoutManager(this@CategoryAct)



                }
            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@CategoryAct,
                        "Error : ${ex.toString()}", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}