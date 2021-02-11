package com.example.beclose.VVM.VM

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.beclose.Forms.Firebase.BeCloseFirebaseAuth
import com.example.beclose.Repository.Repository

class LoginContainerFragmentVM @ViewModelInject constructor(var repository: Repository): ViewModel() {
    fun getFirebase(): BeCloseFirebaseAuth {
        return repository.getFireBaseInstance()
    }
}