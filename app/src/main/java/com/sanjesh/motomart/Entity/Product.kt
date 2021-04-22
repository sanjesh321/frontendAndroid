
package com.example.Motomart.entity
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Product(
    @PrimaryKey
    val _id:String="",
    val pname:String?=null,
    val pdesc:String?=null,
    val pprice:Int=0,
    val pimage:String?=null,
    val pcategory:String?=null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(pname)
        parcel.writeString(pdesc)
        parcel.writeInt(pprice)
        parcel.writeString(pimage)
        parcel.writeString(pcategory)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }


}
