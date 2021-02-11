package com.example.beclose.Distenation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beclose.Adapter.NotificationItemAdapter
import com.example.beclose.Adapter.SearchItemAdapter
import com.example.beclose.databinding.FragmentNotificationBinding
import com.example.beclose.model.UserProfile

class NotificationFragment : Fragment() {
    lateinit var binding:FragmentNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userHandler()
    }

    fun userHandler(){
        setRecycleView()
    }
    private fun setRecycleView() {
        lateinit var adapter: NotificationItemAdapter
        var users=ArrayList<UserProfile>()
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())
        users.add(UserProfile())


        adapter = NotificationItemAdapter(users, requireActivity().application.applicationContext)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(requireActivity().application.applicationContext)//ZoomRecyclerLayout(requireActivity().application.applicationContext)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.notificationRV.layoutManager = linearLayoutManager
        binding.notificationRV.isNestedScrollingEnabled=false
        binding.notificationRV.adapter = adapter

//        modelView = ViewModelProviders.of(this).get(ScanViewModel::class.java)
//        modelView.liveArray.observe(this, Observer {
//            adapter!!.setList(it)
//        })
    }
}