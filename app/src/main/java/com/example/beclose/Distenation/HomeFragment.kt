package com.example.beclose.Distenation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.beclose.Adapter.ViewPagerAdapter
import com.example.beclose.Forms.BottomSheet
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.VVM.Observers.IOnBackPressed
import com.example.beclose.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment(),IOnBackPressed {
    lateinit var binding:FragmentHomeBinding
    lateinit var viewPagerAdapter:ViewPagerAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        val view=binding.root
//        showButtomsheet()
        Toast.makeText(activity, "FFDSDDfdfs", Toast.LENGTH_SHORT).show()
        return view
    }
    private fun showButtomsheet() {
        val myBottomSheetDialog = BottomSheet()
        activity?.let { myBottomSheetDialog.show(it.supportFragmentManager, "") }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initTabAdapter() {
        binding.homeTabLayout.setupWithViewPager(binding.homeViewPager)
        viewPagerAdapter  = ViewPagerAdapter(childFragmentManager, 0)
        val favouriteFragment = FavouriteFragment()
        val connections = ConnectionsFragment()
        val settingFragment = SettingsFragment()
        val notification = NotificationFragment()
        viewPagerAdapter.resetAdapter()
        viewPagerAdapter.insertFragment(connections, "")
        viewPagerAdapter.insertFragment(favouriteFragment, "")
        viewPagerAdapter.insertFragment(settingFragment, "")
        viewPagerAdapter.insertFragment(notification, "")
        binding.homeViewPager.adapter = viewPagerAdapter
        binding.homeTabLayout.getTabAt(0)!!.icon = requireActivity().getDrawable(R.drawable.selector_network)
        binding.homeTabLayout.getTabAt(1)!!.icon = requireActivity().getDrawable(R.drawable.selector_favouite)
        binding.homeTabLayout.getTabAt(2)!!.icon = requireActivity().getDrawable(R.drawable.selector_settings)
        binding.homeTabLayout.getTabAt(3)!!.icon = requireActivity().getDrawable(R.drawable.selector_notification)
        var badgeDrawable= binding.homeTabLayout.getTabAt(3)!!.orCreateBadge
        badgeDrawable.setVisible(true)
        badgeDrawable.number=1
        binding.homeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 3) {
                    badgeDrawable.setVisible(false)
                }//test
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        setTabItemMargin()
    }
    private fun setTabItemMargin() {
        for (i in 0 until binding.homeTabLayout.tabCount) {
            val tab = (binding.homeTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(20, 0, 20, 0)
            tab.requestLayout()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
    override fun onBackPressed() {
        Log.e("###","Home")
        if (binding.homeTabLayout.selectedTabPosition !=0) {
            binding.homeViewPager.currentItem=0
        }else{
            (activity as MainActivity).setOnBackPressedListener(null)
            requireActivity().onBackPressed()
        }
    }
    fun userHandler(){
        (activity as MainActivity).setOnBackPressedListener(this)

        initTabAdapter()
        binding.profileIV.setOnClickListener {
            binding.homeViewPager.currentItem=0

        }
    }
    override fun onDestroyView() {
        (activity as MainActivity).setOnBackPressedListener(null)
        Log.e("##","Destroy")
        super.onDestroyView()
    }

    override fun onPause() {
        Log.e("##","Pause")
        super.onPause()
    }

    override fun onStop() {
        Log.e("##","Stop")
        super.onStop()
    }
}