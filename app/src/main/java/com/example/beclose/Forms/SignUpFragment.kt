package com.example.beclose.Forms

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.beclose.VVM.VM.SignUpFragmentVM
import com.example.beclose.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var vm:SignUpFragmentVM
    lateinit var binding:FragmentSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignUpBinding.inflate(inflater,container,false)
        var view=binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm=ViewModelProvider(this).get(SignUpFragmentVM::class.java)
        vm.getFirebase().setCurrentFragment()
        handleUserInteractions()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        (activity as MainActivity).firebaseAuth.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleUserInteractions(){
        binding.fRegistrationBtnSignUp.setOnClickListener {
            var email=binding.fRegistrationTietEmailAddress.text.toString()
            var password=binding.fRegistrationTietPassword.text.toString()
            var fullName=binding.fRegistrationTietFullName.text.toString()
            if(!email.isEmpty()&&!password.isEmpty()&&!fullName.isEmpty()){
                vm.getFirebase().signUpEmailForClick( email,password, fullName)
            }else{
                Toast.makeText(activity, "Please Write Username and Password and Name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}