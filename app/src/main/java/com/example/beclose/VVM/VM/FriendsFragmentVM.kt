package com.example.beclose.VVM.VM

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.beclose.Repository.Repository
import com.example.beclose.model.UserProfile
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.combine
import kotlin.math.log

public class FriendsFragmentVM @ViewModelInject constructor(
    var repository: Repository,
    app: Application
) :
    AndroidViewModel(app) {
    var friendsLiveData: LiveData<PagedList<UserProfile>>? = null
    var friendsLiveData1: MutableLiveData<List<UserProfile>> = MutableLiveData()

    fun getFriendsAll(pageNum: Int, pageSize: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var list: List<Int> =ArrayList()
                var rowCount=repository.getRowCount()
                if (rowCount!=null){
                    Log.e("MyRoom!!", rowCount.toString())
                    list= (((pageNum-1)*pageSize)..(pageSize*pageNum)).toList()
                    if ((pageSize*pageNum) > rowCount){
                        list = (((pageNum-1)*pageSize)..rowCount).toList()
                    }
                }
                var d=repository.getCashedFriends1(list)
                withContext(Dispatchers.Main){
                    friendsLiveData1.value=d
                }
            } catch (e: Exception) {
                Log.e("####", e.message.toString())
            }
        }
    }

    init {
        Log.e("55555555", "dssfdghjkl;;''lkjgd")

//        getAll()
//        viewMod/elScope.launch(Dispatchers.IO) {
//            val config2=PagedList.Config.Builder()
//                .setPageSize(4)
//                .setInitialLoadSizeHint(4)
//                .setEnablePlaceholders(false)
//                .build()
//            val dataFactory = repository.getCashedFriends()
//            friendsLiveData=LivePagedListBuilder(dataFactory,config2).build()
//        }

    }
    fun getAll(){
        viewModelScope.launch(Dispatchers.IO){
            var d=repository.getCashedFriends()
            withContext(Dispatchers.Main){
                friendsLiveData1.value=d
            }
        }
    }
    fun getFriends() {
//        var rep=repository.getFireBaseInstance()
//        Log.e("###REp",rep.getstr().toString())
//        viewModelScope.launch(Dispatchers.IO) {
//            friendsLiveData = repository.getCashedFriends()
//            }
    }

    val myPagingConfig = Config(
        pageSize = 50,
        prefetchDistance = 150,
        enablePlaceholders = true
    )

    fun getFiendsPagining() {
//        val items= PagedList.Config(
//            pageSize = 50,
//            prefetchDistance = 150,
//            enablePlaceholders = true
//        )
        Log.e("55555555", "dssfdghjkl;;''lkjgd")
        viewModelScope.launch(Dispatchers.IO) {
            val dataFactory = repository.getCashedFriends()
            val config2 = PagedList.Config.Builder()
                .setPageSize(4)
                .setInitialLoadSizeHint(4)
                .setEnablePlaceholders(false)
                .build()
//            friendsLiveData = LivePagedListBuilder(dataFactory, config2).build()
        }
    }

    fun deleteFriend(friend: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteCashedFriend(friendID = friend.roomID!!)
            } catch (e: Exception) {
                Log.e("####", e.message.toString())
            }
        }
    }

    fun insertFriend(friend: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertFriendToCash(friend)
            } catch (e: Exception) {
                Log.e("####", e.message.toString())
            }
        }
    }

    fun removeAll() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeAll()
            } catch (e: Exception) {
                Log.e("####", e.message.toString())
            }
        }
    }
}