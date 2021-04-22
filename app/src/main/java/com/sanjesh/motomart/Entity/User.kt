package com.sanjesh.motomart.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    var _id:String="",
    var profilepic: String? = null,
    var Firstname: String? = null,
    var Lastname: String? = null,
    var Username: String? = null,
    var Email: String? = null,
    var password: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0
}