package com.sanjesh.motomart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sanjesh.motomart.Entity.Productsale
import com.sanjesh.motomart.R

class SaleAdapter(private val lstSale: ArrayList<Productsale>,
                  val context: Context
): RecyclerView.Adapter<SaleAdapter.SaleViewHolder>() {
    class SaleViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivSale: ImageView
        val tvSale: TextView
        init {
            ivSale = view.findViewById(R.id.ivSale)
            tvSale=view.findViewById(R.id.tvSale)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.salecategory,parent,false)
        return SaleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val ProductSale = lstSale[position]
        holder.tvSale.text = ProductSale.pSale
        Glide.with(context).load(ProductSale.imgSale).into(holder.ivSale)
    }

    override fun getItemCount(): Int {
        return lstSale.size
    }

}