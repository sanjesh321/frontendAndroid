package com.sanjesh.motomart.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.Motomart.entity.Product
import com.sanjesh.motomart.Adapter.ProductAdapter
import com.sanjesh.motomart.R
import com.sanjesh.motomart.Repository.ProductRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var homeCategory: RecyclerView
    private lateinit var saleGridView: RecyclerView
    //    private lateinit var productView:ConstraintLayout
    val imageList = ArrayList<SlideModel>() // Create image list

    private var lstProduct : MutableList<Product> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title


        val view = inflater.inflate(R.layout.fragment_home, container, false)


        //reclycler view for categories
        homeCategory = view.findViewById(R.id.homeCategory)
        saleGridView = view.findViewById(R.id.saleGridView)
        loadSaleProduct()
        return view
    }

    fun loadSaleProduct(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = ProductRepo()
                val response = repo.retrieveProducts()
                lstProduct = response.data!!
                println(lstProduct)
                withContext(Dispatchers.Main) {
                    saleGridView.layoutManager =
                        LinearLayoutManager(context)
                    saleGridView.adapter = ProductAdapter(context!!, lstProduct)
                }
            } catch (ex: Exception) {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}