package com.example.beclose.Distenation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.beclose.Adapter.WalkThroughAdapter
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.Utils.SharedDB
import com.example.beclose.databinding.FragmentWalkThroughBinding
import com.example.beclose.model.WalkThroughModel
import com.google.firebase.iid.FirebaseInstanceId

class WalkThroughFragment : Fragment() {
    lateinit var binding:FragmentWalkThroughBinding
    lateinit var adapter:WalkThroughAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentWalkThroughBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun userHandler(){
        setViewPagerAdapter()
        setupIndecator()
        currentIndecator(0)
        animate()
        binding.getStartBtn.setOnClickListener {
//            Navigate to home
            var localUser= SharedDB.getUser()
            if(localUser.fields!!.size>0){
                (activity as MainActivity).showMyDialog(1)
                Navigation.findNavController(requireView()).navigate(R.id.action_from_walk_through_to_home)
            }else{
                (activity as MainActivity).showMyDialog(1)
                //goto add fields
                Navigation.findNavController(requireView()).navigate(R.id.action_from_walk_through_to_add_newField)
            }
        }
    }
    private fun setViewPagerAdapter(){
        var arr:ArrayList<WalkThroughModel> = ArrayList()
        arr.add((WalkThroughModel(R.drawable.doctor,"First","sgdfasdsdvfffsdvsdvfsdvsdvsdvaddfsdasdf")))
        arr.add((WalkThroughModel(R.drawable.doctor,"Second","sgdfassdvffffsdvsdvdsdcsdfaddfsdasdf")))
        arr.add((WalkThroughModel(R.drawable.doctor,"Last","yjhghfgdsdvsfffsdvdvsdvsdvsdvsddvdfsvsdfghjh")))
        adapter=WalkThroughAdapter(arr)
        binding.walkThroughViewPager.adapter=adapter
        binding.walkThroughViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentIndecator(position)
            }
        })
        (binding.walkThroughViewPager.getChildAt(0) as RecyclerView).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
        binding.nextBtn.setOnClickListener {
            if (binding.walkThroughViewPager.currentItem+1<adapter.itemCount){
                binding.walkThroughViewPager.currentItem+=1
            }else{
//                Navigate to home
                var localUser= SharedDB.getUser()
                if(localUser.fields!!.size>0){
                    (activity as MainActivity).showMyDialog(1)
                    Navigation.findNavController(requireView()).navigate(R.id.action_from_walk_through_to_home)
                }else if (localUser.description==null){
//                    goto add description
                    (activity as MainActivity).showMyDialog(1)
                }
                else{
                    //goto add fields
                    (activity as MainActivity).showMyDialog(1)
                    Navigation.findNavController(requireView()).navigate(R.id.action_from_walk_through_to_add_newField)
                }
            }
        }
    }
    private fun setupIndecator(){
        var indecators= arrayOfNulls<ImageView>(adapter.itemCount)
        Log.e("$$$$$",indecators.size.toString())
        var layoutParam:LinearLayout.LayoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParam.setMargins(8,0,8,0)
        for (i in 0 until indecators.size){
            indecators[i]= ImageView(this.context)
            indecators[i].let {
                if (it != null) {
                    it.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.indicator_inactive_background))
                    it.layoutParams=layoutParam
                    binding.indecatorContainer.addView(it)
                    Log.e("$$$$$",i.toString())
                }
            }
        }
    }
    private fun currentIndecator(position:Int){
        for (i in 0 until binding.indecatorContainer.childCount){
            var imageView=binding.indecatorContainer.getChildAt(i) as ImageView
            if (i==position){
                imageView.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.indicator_active_background))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.indicator_inactive_background))
            }
        }
    }
    private fun animate(){
        binding.walkThroughViewPager.translationX=-1200f
        binding.nextBtn.translationX=-1200f
        binding.buttonNextView.translationX=-1200f
        binding.indecatorContainer.translationX=-1200f
        binding.getStartBtn.translationY=800f

        binding.buttonNextView.alpha=0f
        binding.nextBtn.alpha=0f
        binding.getStartBtn.alpha=0f

        binding.walkThroughViewPager.animate().translationX(0f).setDuration(800).setStartDelay(600).start()
        binding.indecatorContainer.animate().translationX(0f).setDuration(800).setStartDelay(600).start()
        binding.buttonNextView.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(600).start()
        binding.nextBtn.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(600).start()
        binding.getStartBtn.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(1000).start()
    }
}