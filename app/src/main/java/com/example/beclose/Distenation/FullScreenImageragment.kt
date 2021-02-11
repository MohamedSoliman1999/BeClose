package com.example.beclose.Distenation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.beclose.MainActivity
import com.example.beclose.VVM.Observers.IOnBackPressed
import com.example.beclose.databinding.FragmentFullScreenImageragmentBinding

class FullScreenImageragment : Fragment(),IOnBackPressed {
    lateinit var binding: FragmentFullScreenImageragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setStatusBarVisiablie(false)
   /*     if (offer == null) {
            updateUI(ManagementOption.ADD)
            isNew = true
        }*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentFullScreenImageragmentBinding.inflate(inflater,container,false)
        var view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments!=null){
            var bitmap = FullScreenImageragmentArgs.fromBundle(requireArguments()).image
            if(bitmap!=null){
                binding.fullScreenIV.setImageBitmap(bitmap)
            }
        }

        userHandelar()
    }
    fun userHandelar(){
        binding.fullScreenBackBtnIV.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        (activity as MainActivity).setOnBackPressedListener(null)
        super.onDestroyView()
    }

}