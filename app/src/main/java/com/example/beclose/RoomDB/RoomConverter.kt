package com.example.beclose.RoomDB

import androidx.room.TypeConverter
import com.example.beclose.model.DataField
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class RoomConverter {
    @TypeConverter
    public fun fromFieldsListToGson(fieldsList: ArrayList<DataField>):String{
        return Gson().toJson(fieldsList)
    }
    @TypeConverter
    public fun fromGsonToFields(gsonFields: String):ArrayList<DataField>{
        val listType: Type = object : TypeToken<ArrayList<DataField?>?>() {}.type
        return Gson().fromJson(gsonFields, listType)
    }
    @TypeConverter
    fun fromStringToArrayOfString(stringListString: String): ArrayList<String> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(stringListString, listType)
    }

    @TypeConverter
    fun convertToString(stringList: ArrayList<String>): String {
        return Gson().toJson(stringList)
    }
//    @TypeConverter
//    fun fromString(stringListString: String): List<String> {
//        return stringListString.split(",").map { it }
//    }
//
//    @TypeConverter
//    fun toString(stringList: List<String>): String {
//        return stringList.joinToString(separ    ator = ",")
//    }
}