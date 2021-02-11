package com.example.beclose.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.beclose.model.UserProfile
import com.google.android.datatransport.runtime.dagger.Provides

@Database(entities = [UserProfile::class],version = 1,exportSchema = false)
@TypeConverters(RoomConverter::class)
public abstract class FriendsRoomDB: RoomDatabase() {
    abstract fun friendsDao(): FriendsDao

}