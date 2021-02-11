package com.example.beclose.Distenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beclose.Adapter.ConnectionAdapter
import com.example.beclose.Adapter.SearchItemAdapter
import com.example.beclose.databinding.FragmentSearchBinding
import com.example.beclose.model.UserProfile
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout

class SearchFragment : Fragment() {
    lateinit var binding:FragmentSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
    }
    fun userHandler(){
        setRecycleView()
        binding.searchLayoutFragment.searchLayoutBackBtnIV.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    private fun setRecycleView() {
        lateinit var adapter:SearchItemAdapter
        var users=ArrayList<UserProfile>()
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())


        adapter = SearchItemAdapter(users, requireActivity().application.applicationContext)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(requireActivity().application.applicationContext)//ZoomRecyclerLayout(requireActivity().application.applicationContext)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.searchRV.layoutManager = linearLayoutManager
        binding.searchRV.isNestedScrollingEnabled=false
        binding.searchRV.adapter = adapter

//        modelView = ViewModelProviders.of(this).get(ScanViewModel::class.java)
//        modelView.liveArray.observe(this, Observer {
//            adapter!!.setList(it)
//        })
    }
}