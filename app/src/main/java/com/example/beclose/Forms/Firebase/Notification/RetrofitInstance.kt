package com.example.beclose.Forms.Firebase.Notification

import com.example.beclose.Forms.Firebase.Notification.FCMContacts.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(ApplicationComponent::class)
class RetrofitInstance {

    companion object {
        @Provides
        @Singleton
        public fun getRetrofitInstance():NotificationAPI{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NotificationAPI::class.java)
        }
//
//        private val retrofit by lazy {
//            Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//        public val api by lazy {
//            retrofit.create(NotificationAPI::class.java)
//        }


    }
}