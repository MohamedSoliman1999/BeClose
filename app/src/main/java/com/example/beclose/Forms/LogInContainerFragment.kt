package com.example.beclose.Forms

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclose.Adapter.ViewPagerAdapter
import com.example.beclose.MainActivity
import com.example.beclose.VVM.VM.LoginContainerFragmentVM
import com.example.beclose.databinding.FragmentLogInContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInContainerFragment : Fragment() {
    lateinit var vm:LoginContainerFragmentVM
    lateinit var binding: FragmentLogInContainerBinding
    lateinit var viewm:View
    /*private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123
    public lateinit var user:FirebaseUser*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //authenticationRequest()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm=ViewModelProvider(this).get(LoginContainerFragmentVM::class.java)
        vm.getFirebase().setCurrentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLogInContainerBinding.inflate(inflater, container, false)
        viewm=binding.root
        (activity as MainActivity?)!!.hideFabVisibility(true)
        initTabAdapter()
        animation()
        handleUserInteractions()
        return viewm
    }
    private fun initTabAdapter() {
         val singUpForm = SignUpFragment()
        val singInForm = SignInFragment()
         binding.formTabLayout.setupWithViewPager(binding.formViewPager)
         val viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, 0)
         viewPagerAdapter.resetAdapter()
         viewPagerAdapter.insertFragment(singInForm, "Sign In")
         viewPagerAdapter.insertFragment(singUpForm, "Sign Up")
         binding.formViewPager.adapter = viewPagerAdapter
         setTabItemMargin()
    }
    private fun setTabItemMargin() {
        for (i in 0 until binding.formTabLayout.tabCount) {
            val tab = (binding.formTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(50, 0, 50, 0)
            tab.requestLayout()
        }
    }
    private fun animation(){
        binding.containerItemLoginContainer.translationY= 1200f
        binding.facbookFab.translationY=300f
        binding.googleFab.translationY=300f
        binding.twitterFab.translationY=300f
        binding.formTabLayout.translationY=300f

        binding.facbookFab.alpha=0f
        binding.googleFab.alpha=0f
        binding.twitterFab.alpha=0f
        binding.formTabLayout.alpha=0f

        binding.containerItemLoginContainer.animate().translationY(0f).setDuration(1000).setStartDelay(400).start()
        binding.facbookFab.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(1400).start()
        binding.googleFab.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(1400).start()
        binding.twitterFab.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(1400).start()
        binding.formTabLayout.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(1400).start()
    }
    private fun handleUserInteractions(){
        binding.googleFab.setOnClickListener { vm.getFirebase().signInGoogleForClick() }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        Log.e("###","fghdsfaFDGD")
//        firebaseAuth.onActivityResult(requestCode, resultCode, data)
    }

}