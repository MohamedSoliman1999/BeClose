package com.example.beclose.Forms

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beclose.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet() : BottomSheetDialogFragment() {
   lateinit var  fragmentView: View
   lateinit var bitmapImageProfile:Bitmap
   lateinit var showImageBtn:com.google.android.material.button.MaterialButton
   lateinit var selectImageBtn:com.google.android.material.button.MaterialButton
    constructor(image:Bitmap):this(){
        bitmapImageProfile=image
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.profile_picture_bottom_sheet, container, false)

//        increment = fragmentView.findViewById<ImageView>(R.id.increament)
//        decriment = fragmentView.findViewById<ImageView>(R.id.decrement)
//        numberTxt = fragmentView.findViewById<TextView>(R.id.productNumber)
//        Order = fragmentView.findViewById<TextView>(R.id.OrderBottomSheet)
//        AddToCard = fragmentView.findViewById<TextView>(R.id.addToCard)
//        PayoalConfig()
//        setOnClick()
        //var image=fragmentView.findViewById<com.zolad.zoominimageview.ZoomInImageView>(R.id.fullScreen_IV)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
    }
    private fun userHandler(){
        showImageBtn=fragmentView.findViewById<com.google.android.material.button.MaterialButton>(R.id.f_show_profile_image_btn)
        selectImageBtn= fragmentView.findViewById<com.google.android.material.button.MaterialButton>(R.id.f_account_change_profile_image_btn)
//        var tempImage=fragmentView.findViewById<ImageView>(R.id.tempimage)
//        tempImage.setImageBitmap(bitmapImageProfile)
//        tempImage.setOnClickListener {
////            var action= ProfileFragmentDirections.actionFromProfileToFullScreen(bitmapImageProfile)
////            Navigation.findNavController(view!!).navigate(action)
//        }
//        showImageBtn.setOnClickListener {

//            var action= ProfileFragmentDirections.actionFromProfileToFullScreen(bitmapImageProfile)
//            Navigation.findNavController(view!!).navigate(action)
        }
//        selectImageBtn.setOnClickListener {
//
//        }
//    }


}