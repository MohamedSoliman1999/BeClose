package com.example.beclose.Forms

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSplashBinding.inflate(inflater,container,false)
        var view=binding.root
        return view
       // return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handler = Handler()
        handler.postDelayed({
            try { // if We press back during delay it will crashed
//                val action: NavDirections = SplashFragmentDirections.actionFromSplashToCompleteProfile().setUser(user)
//                Navigation.findNavController(view!!).navigate(action)
                // Navigation.findNavController(view!!).navigate(R.id.action_from_splash_to_home)
                (activity as MainActivity).firebaseAuth.setCurrentFragment()
                (activity as MainActivity).firebaseAuth.onAppStart()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 1500)

    }

}