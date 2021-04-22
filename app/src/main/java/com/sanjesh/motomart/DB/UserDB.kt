package com.sanjesh.motomart.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.Motomart.entity.Product
import com.sanjesh.motomart.DAO.Productdao
import com.sanjesh.motomart.DAO.Userdao
import com.sanjesh.motomart.Entity.User

@Database(
    entities = [(User::class),(Product::class)],
    version = 3,
    exportSchema = false
)

abstract class UserDB : RoomDatabase() {
    abstract fun getUserDAO(): Userdao
    abstract fun getProductDAO():Productdao
    companion object{
        @Volatile
        private var instance: UserDB? = null
        fun getInstance(context: Context): UserDB {
            if (instance == null){
                kotlin.synchronized(UserDB::class){
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }
        private fun buildDatabase(context: Context): UserDB {
            return Room.databaseBuilder(
                context.applicationContext,
                UserDB::class.java,
                "UsersDB"
            ).fallbackToDestructiveMigration().build()
        }
    }

}