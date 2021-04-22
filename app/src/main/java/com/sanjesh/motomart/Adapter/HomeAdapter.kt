package com.sanjesh.motomart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sanjesh.motomart.Entity.Category
import com.sanjesh.motomart.R

class HomeAdapter(private val lstCategory: ArrayList<Category>,
                  val context: Context
): RecyclerView.Adapter<HomeAdapter.CategoriesViewHolder>() {
    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivCat: ImageView
        val catName: TextView
        init {
            ivCat = view.findViewById(R.id.ivCat)
            catName=view.findViewById(R.id.catName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_category,parent,false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val Categories = lstCategory[position]
        holder.catName.text = Categories.catName
        Glide.with(context).load(Categories.catImg).into(holder.ivCat)
    }

    override fun getItemCount(): Int {
        return lstCategory.size
    }

}