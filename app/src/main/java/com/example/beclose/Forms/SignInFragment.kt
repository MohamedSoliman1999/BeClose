package com.example.beclose.Forms

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.beclose.MainActivity
import com.example.beclose.VVM.VM.SignInFragmentVM
import com.example.beclose.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    lateinit var vm:SignInFragmentVM
    lateinit var binding: FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentSignInBinding.inflate(inflater,container,false)
        var view=binding.root
        animation()
        return view
    }
    private fun animation(){
        binding.fAuthTilEmailAddress.translationX=800f
        binding.fAuthTilPassword.translationX=-800f
        binding.fAuthTvForgotPassword.translationX=800f
        binding.fAuthBtnSignIn.translationX=-800f

        binding.fAuthTilEmailAddress.alpha=0f
        binding.fAuthTilPassword.alpha=0f
        binding.fAuthTvForgotPassword.alpha=0f
        binding.fAuthBtnSignIn.alpha=0f

        binding.fAuthTilEmailAddress.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(300).start()
        binding.fAuthTilPassword.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        binding.fAuthTvForgotPassword.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        binding.fAuthBtnSignIn.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()
    }
    private fun handleUserInteractions(){
        binding.fAuthBtnSignIn.setOnClickListener {
            var email=binding.fAuthTietEmailAddress.text.toString()
            var password=binding.fAuthTietPassword.text.toString()
            if(!email.isEmpty()&&!password.isEmpty()){
                vm.getFirebase().signInEmailForClick(email,password)
            }else{
                Toast.makeText(activity, "Please Write Username and Password", Toast.LENGTH_SHORT).show()
            }
        }
        binding.fAuthTvForgotPassword.setOnClickListener {
            (activity as MainActivity).showMyDialog(0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm=ViewModelProvider(this).get(SignInFragmentVM::class.java)
        vm.getFirebase().setCurrentFragment()
        handleUserInteractions()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        (activity as MainActivity).firebaseAuth.onActivityResult(requestCode, resultCode, data)
    }
}