package com.example.beclose.Distenation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.beclose.R
import com.example.beclose.VVM.VM.SettingsFragmentVM
import com.example.beclose.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    lateinit var vm:SettingsFragmentVM
    lateinit var binding:FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm=ViewModelProvider(this).get(SettingsFragmentVM::class.java)
        vm.getFirebase().setCurrentFragment()
        userHandler()
    }
    fun userHandler(){
        binding.qrCodeBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_from_home_to_qr_managment)
        }
        binding.fAccountMtbtnProfile.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_from_home_to_my_profile)
        }
        binding.fAccountMtbtnLogout.setOnClickListener {
            vm.getFirebase().signOut()
        }
        binding.fAccountMtbtnShare.setOnClickListener {
            activity?.let { it1 ->
                ShareCompat.IntentBuilder.from(it1)
                    .setType("text/plain")
                    .setChooserTitle("Share With")
                    .setText("http://play.google.com/store/apps/details?id=" + (activity)!!.getPackageName())
                    .startChooser()
            };
        }
        binding.fAccountMtbtnRate.setOnClickListener {
            try{
                var uri: Uri =Uri.parse("market://details?id="+requireActivity().packageName)
                var intent: Intent =Intent(Intent.ACTION_VIEW,uri)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }catch(e:Exception){
                var uri:Uri=Uri.parse("http://play.google.com/store/apps/details?id="+requireActivity().packageName)
                var intent:Intent=Intent(Intent.ACTION_VIEW,uri)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}