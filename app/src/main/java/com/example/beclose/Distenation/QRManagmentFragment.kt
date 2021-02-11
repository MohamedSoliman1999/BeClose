package com.example.beclose.Distenation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.beclose.Adapter.ViewPagerAdapter
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.VVM.Observers.IOnBackPressed
import com.example.beclose.databinding.FragmentQRManagmentBinding
import com.google.android.material.tabs.TabLayout


class QRManagmentFragment : Fragment(),IOnBackPressed {
    lateinit var binding:FragmentQRManagmentBinding
    lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentQRManagmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initTabAdapter() {
        binding.QrTabLayout.setupWithViewPager(binding.QRViewPager)
        viewPagerAdapter  = ViewPagerAdapter(childFragmentManager, 0)
        val qrCodeFragment = QRCodeFragment()
        val scanQRFragment = ScanQRFragment()
        viewPagerAdapter.resetAdapter()
        viewPagerAdapter.insertFragment(qrCodeFragment, "MY CODE")
        viewPagerAdapter.insertFragment(scanQRFragment, "SCAN CODE")
        binding.QRViewPager.adapter = viewPagerAdapter
//        binding.QrTabLayout.getTabAt(0)!!.icon = activity!!.getDrawable(R.drawable.selector_network)
//        binding.QrTabLayout.getTabAt(1)!!.icon = activity!!.getDrawable(R.drawable.selector_favouite)
        binding.QrTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 1) {
                    binding.QRManagmentLayout.setBackgroundColor(activity!!.getColor(R.color.black))
                    binding.fragmentQRCodeManagementTvTitle.setTextColor(activity!!.getColor(R.color.white))
                    binding.QrTabLayout.setBackgroundColor(activity!!.getColor(R.color.black))
                    binding.QrTabLayout.setTabTextColors(activity!!.getColor(R.color.colorPlaceholderLightGrey), activity!!.getColor(R.color.white))
                } else {
                    binding.QRManagmentLayout.setBackgroundColor(activity!!.getColor(R.color.white))
                    binding.fragmentQRCodeManagementTvTitle.setTextColor(activity!!.getColor(R.color.black))
                    binding.QrTabLayout.setBackgroundColor(activity!!.getColor(R.color.white))
                    binding.QrTabLayout.setTabTextColors(activity!!.getColor(R.color.colorPlaceholderLightGrey), activity!!.getColor(R.color.black))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        setTabItemMargin()
    }
    private fun setTabItemMargin() {
        for (i in 0 until binding.QrTabLayout.tabCount) {
            val tab = (binding.QrTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(20, 0, 20, 0)
            tab.requestLayout()
        }
    }
    override fun onBackPressed() {
        Log.e("###", "Home")
        if (binding.QrTabLayout.selectedTabPosition !=0) {
            binding.QRViewPager.currentItem=0
        }else{
            (activity as MainActivity).setOnBackPressedListener(null)
            activity!!.onBackPressed()
        }
    }
    fun userHandler(){
        (activity as MainActivity).setOnBackPressedListener(this)
        (activity as MainActivity).setStatusBarVisiablie(false)
        initTabAdapter()
        binding.profileIV.setOnClickListener {
            binding.QRViewPager.currentItem=0

        }
    }

    fun mergeBitmaps(overlay: Bitmap, bitmap: Bitmap): Bitmap? {
        val height = bitmap.height
        val width = bitmap.width
        val combined = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(combined)
        val canvasWidth: Int = canvas.getWidth()
        val canvasHeight: Int = canvas.getHeight()
        canvas.drawBitmap(bitmap, Matrix(), null)
        val centreX = (canvasWidth - overlay.width) / 2
        val centreY = (canvasHeight - overlay.height) / 2
        //canvas.drawBitmap(overlay, centreX, centreY, null)
        return combined
    }
}