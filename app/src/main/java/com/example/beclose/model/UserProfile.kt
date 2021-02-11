package com.example.beclose.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FriendsUserTable")
class UserProfile :Parcelable {
    @PrimaryKey(autoGenerate = true)
    public var roomID:Long?=null
    public var uId: String? =null  //user id for everey user with defferent devices
    public var token: String? =null //token for one device ----every device have different toke
    public var name: String? =null
    public var description: String? ="Have No Description Or Job Title"
    public var password:String?=null
    public var image: String? = null
    public var fields: ArrayList<DataField>? =ArrayList()
    public var friendsTokn: ArrayList<String> = ArrayList<String>()
    public var public:Int?=null
    public var waitingListYouSent:ArrayList<String> = ArrayList()
    public var waitingListYouWait:ArrayList<String> = ArrayList()
    public var isFavourite:Boolean=false
    constructor()
    constructor(parcel: Parcel) : this() {
        roomID = parcel.readValue(Long::class.java.classLoader) as? Long
        uId = parcel.readString()
        token = parcel.readString()
        name = parcel.readString()
        description = parcel.readString()
        password = parcel.readString()
        image = parcel.readString()
        public = parcel.readValue(Int::class.java.classLoader) as? Int
        isFavourite = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(roomID)
        parcel.writeString(uId)
        parcel.writeString(token)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(password)
        parcel.writeString(image)
        parcel.writeValue(public)
        parcel.writeByte(if (isFavourite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }


}