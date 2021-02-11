package com.example.beclose.Distenation

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.beclose.MainActivity
import com.example.beclose.Utils.SharedDB
import com.example.beclose.databinding.FragmentQRCodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder


class QRCodeFragment : Fragment() {
    lateinit var binding: FragmentQRCodeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideFabVisibility(true)
        (activity as MainActivity).setStatusBarVisiablie(true)
        userHandler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentQRCodeBinding.inflate(inflater, container, false)
        val view=binding.root
        SharedDB.getUser().uId?.let { generateQRCode(it) }
        return view
    }
    private fun generateQRCode(QRString: String){
        var multiFormatWriter =MultiFormatWriter()
        var bitmatrix:BitMatrix=multiFormatWriter.encode(QRString, BarcodeFormat.QR_CODE, 350, 350)
        var barcode=BarcodeEncoder()
        var bitmap:Bitmap= barcode.createBitmap(bitmatrix)
        binding.qrCodeIV.setImageBitmap(bitmap)
       /*Hide Keyboard
        var manager:InputMethodManager= activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(binding.editText.getApplicationWindowToken(),0)*/
    }
    private fun userHandler(){
        binding.profileImage.setOnClickListener {
            var d=binding.profileImage.drawable
            val bitmap = (d as BitmapDrawable).bitmap
          //  (activity as MainActivity).removeFragment()
            val action=QRManagmentFragmentDirections.actionFromQrManagmentToFullScreen(bitmap)
            Navigation.findNavController(it).navigate(action)

        }

    }

}