package com.sanjesh.motomart.DAO


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.Motomart.entity.Product
@Dao
interface Productdao { @Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun addProduct(product: MutableList<Product>)
    @Query("select * from Product")
    suspend fun viewProduct():List<Product>
    @Query("delete from Product")
    suspend fun deleteAll()
    @Query("SELECT * FROM Product")
    suspend fun getAllProduct() : MutableList<Product>
}
