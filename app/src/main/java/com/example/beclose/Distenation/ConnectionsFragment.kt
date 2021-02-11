package com.example.beclose.Distenation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.Adapter.ConnectionAdapter
import com.example.beclose.Adapter.FriendsPagedAdapter
import com.example.beclose.R
import com.example.beclose.RoomDB.RoomConverter
import com.example.beclose.VVM.VM.FriendsFragmentVM
import com.example.beclose.model.UserProfile
import com.example.beclose.databinding.FragmentConnectionsBinding
import com.example.beclose.model.DataField
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout
@AndroidEntryPoint
class ConnectionsFragment : Fragment() {
    lateinit var binding: FragmentConnectionsBinding
    lateinit var adapter:ConnectionAdapter
    lateinit var vm:FriendsFragmentVM
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm= ViewModelProviders.of(this).get(FriendsFragmentVM::class.java)
        userHandler()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentConnectionsBinding.inflate(inflater,container,false)
        return binding.root
    }
    private fun setRecycleView() {
//        vm.insertFriend(UserProfile())
//        vm.insertFriend(UserProfile())
//        vm.insertFriend(UserProfile())
//        vm.insertFriend(UserProfile())
//        vm.insertFriend(UserProfile())
//        vm.insertFriend(UserProfile())
//        vm.insertFriend(UserProfile())
        vm.getFriendsAll(1,5)
        adapter = ConnectionAdapter(requireActivity().applicationContext)
        adapter.setRepo(vm.repository)
        var linearLayoutManager: LinearLayoutManager = ZoomRecyclerLayout(requireActivity().application.applicationContext)
        linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        binding.connectionsRV.layoutManager = linearLayoutManager
        binding.connectionsRV.isNestedScrollingEnabled=false
        binding.connectionsRV.adapter = adapter
        vm.friendsLiveData1?.observe(requireActivity(),object :Observer<List<UserProfile>>{

            override fun onChanged(t: List<UserProfile>?) {
                if (t != null) {
                    Log.e("RoomSize",t.size.toString())
                    adapter.setList(t)

                    binding.connectionsProgressbar.visibility=View.GONE
                }
            }
        })

    }
    private fun setupFab(){
        binding.fab.setOnClickListener {
            //
            Navigation.findNavController(requireView()).navigate(R.id.action_from_home_to_search_fragment)
        }
        binding.connectionsRV.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    binding.connectionsProgressbar.visibility=View.VISIBLE
                    vm.getFriendsAll(page,limit)
                    page++
                    Log.e("SSSSS",page.toString())
                }
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(dy>-1){
                    binding.fab.collapse()
                }else{
                    binding.fab.expand()
                }
            }
        })
    }
    private fun userHandler(){
        setRecycleView()
        setupFab()
        pagination()
    }
    var page=2
    var limit=5
    private fun pagination(){

    }
    override fun onPause() {
        binding.fab.collapse()
        page=2
        limit=5
        super.onPause()
    }
}