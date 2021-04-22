package com.sanjesh.motomart.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.Motomart.entity.Product

@Entity
data class Cart(
    @PrimaryKey
    val _id:String = "",
    val p_id: Product?=null,
    val u_id:String?=null,
    var pprice:Int=0,
    var pquantity:Int=0
)
