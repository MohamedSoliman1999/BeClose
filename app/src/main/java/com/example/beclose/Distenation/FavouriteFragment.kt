package com.example.beclose.Distenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.Adapter.ConnectionAdapter
import com.example.beclose.Adapter.FavouriteAdapter
import com.example.beclose.databinding.FragmentFavouriteBinding
import com.example.beclose.model.UserProfile
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout


class FavouriteFragment : Fragment() {
    lateinit var binding:FragmentFavouriteBinding
    lateinit var adapter:FavouriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentFavouriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
    }
    private fun setRecycleView() {
        var users=ArrayList<UserProfile>()
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())


        adapter = FavouriteAdapter(users, activity!!.application.applicationContext)
        var linearLayoutManager: LinearLayoutManager = ZoomRecyclerLayout(activity!!.application.applicationContext)
        linearLayoutManager.orientation= LinearLayoutManager.HORIZONTAL
        binding.favouriteConnectionsRV.layoutManager = linearLayoutManager
        binding.favouriteConnectionsRV.isNestedScrollingEnabled=false
        binding.favouriteConnectionsRV.adapter = adapter

//        modelView = ViewModelProviders.of(this).get(ScanViewModel::class.java)
//        modelView.liveArray.observe(this, Observer {
//            adapter!!.setList(it)
//        })
    }
    private fun userHandler(){
        setRecycleView()
    }
}