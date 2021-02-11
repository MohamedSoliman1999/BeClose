package com.example.beclose

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
public class BeCloserApp: Application() {
    companion object {
        private lateinit var  INSTANCE:BeCloserApp
        public fun gettINSTANCE():BeCloserApp{
            if(INSTANCE==null){
                INSTANCE=BeCloserApp()
            }
            return INSTANCE
        }
    }
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Fresco.initialize(this)
    }
}