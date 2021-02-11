package com.example.beclose.DaggerModule

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.beclose.Repository.Repository
import com.example.beclose.RoomDB.FriendsDao
import com.example.beclose.RoomDB.FriendsRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
public object DatabaseModule {
    @Provides
    @Singleton
    public fun provideDB(
        @ApplicationContext context: Context,
        roomCallback: RoomDatabase.Callback
    ): FriendsRoomDB {
        return Room.databaseBuilder(context, FriendsRoomDB::class.java, "frinedsdatabase")
            .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
            .addCallback(roomCallback)
            .build()
    }

    @Provides
    public fun provideRoomDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                //Initialize Database if no database attached to the App
                super.onCreate(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                //Re-open Database if it has database attached to the App
                super.onOpen(db)
            }
        }
    }

    @Provides
    @Singleton
    public fun provideDao(db: FriendsRoomDB): FriendsDao {
        return db.friendsDao()
    }

//    @Provides
//    @Singleton
//    fun provideRepo():Repository{
//        return Repository()
//    }
}