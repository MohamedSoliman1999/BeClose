package com.example.beclose.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class DataField :Parcelable {
    var fieldName:String?=null
    var fieldData:String?=null
    var icon: Int?=null

    constructor(parcel: Parcel) : this() {
        fieldName = parcel.readString()
        fieldData = parcel.readString()
        icon = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    constructor(fieldName: String?, fieldData: String?, icon: Int?) {
        this.fieldName = fieldName
        this.fieldData = fieldData
        this.icon = icon
    }
    constructor(fieldName: String?,  icon: Int?) {
        this.fieldName = fieldName
        this.icon = icon
    }
    constructor()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fieldName)
        parcel.writeString(fieldData)
        parcel.writeValue(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataField> {
        override fun createFromParcel(parcel: Parcel): DataField {
            return DataField(parcel)
        }

        override fun newArray(size: Int): Array<DataField?> {
            return arrayOfNulls(size)
        }
    }
}