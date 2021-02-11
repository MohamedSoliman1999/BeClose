package com.example.beclose.Adapter

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beclose.Distenation.FullScreenImageragmentArgs
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.VVM.Observers.IOnClickListenerBottomSheet
import com.example.beclose.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
lateinit var binding:FragmentBottomSheetBinding
lateinit var bitmapImageProfile:Bitmap
    private val GALLERY_REQUEST_CODE = 1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bitmap = FullScreenImageragmentArgs.fromBundle(requireArguments()).image
        if(bitmap!=null){
            bitmapImageProfile=bitmap
        }
        userHandler()
        BottomINSTANCE =this
    }
    fun userHandler(){
        binding.fShowProfileImageBtn.setOnClickListener {
            this.dismiss()
            if (onClick !=null){
                onClick.onClickListenerViewImage()
            }
        }
        binding.fAccountChangeProfileImageBtn.setOnClickListener {
            if (onClick !=null){
                this.dismiss()
                if ((activity as MainActivity).showInternetSnakeBar()){
                    onClick.onClickListenerSelectImage()
                }
            }
        }
    }

    companion object{
        lateinit var onClick:IOnClickListenerBottomSheet
        lateinit var BottomINSTANCE: BottomSheetFragment
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }
}