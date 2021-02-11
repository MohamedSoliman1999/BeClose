package com.example.beclose.VVM.VM

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.beclose.Forms.Firebase.BeCloseFirebaseAuth
import com.example.beclose.Repository.Repository

class ScanFragmentVM @ViewModelInject constructor(private var repositry: Repository,app:Application) : AndroidViewModel(app) {
    fun getFirebaseInstance():BeCloseFirebaseAuth{
        return repositry.getFireBaseInstance()
    }
}