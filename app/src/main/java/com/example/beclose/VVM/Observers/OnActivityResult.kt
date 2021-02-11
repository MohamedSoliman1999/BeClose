package com.example.beclose.VVM.Observers

import android.content.Intent

interface OnActivityResult {
     fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}