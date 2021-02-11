package com.example.beclose.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.beclose.Forms.Firebase.BeCloseFirebaseAuth
import com.example.beclose.RoomDB.FriendsDao
import com.example.beclose.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class Repository @Inject constructor(
    private var beCloseFirebaseAuth: BeCloseFirebaseAuth,
    private var roomDao: FriendsDao
)
{

    public fun getFireBaseInstance(): BeCloseFirebaseAuth {
        return beCloseFirebaseAuth
    }

     suspend fun insertFriendToCash(user: UserProfile) {
        roomDao.insertFriend(user)
    }

    suspend fun deleteCashedFriend(friendID: Long) {
        roomDao.deleteFriend(friendID)
    }
     suspend fun getCashedFriends1(list:List<Int>):List<UserProfile> {
        return roomDao.getFriends1(list)
    }
    suspend fun getCashedFriends():List<UserProfile> {
        return roomDao.getFriends()
    }
    suspend fun removeAll(){
        roomDao.removeAll()
    }
    suspend fun getRowCount(): Int? {
        return roomDao.getRowCount()
    }
    suspend fun setFriendsFavourite(id:Long,isFavourite:Boolean) {
        return roomDao.setFavourite(id,isFavourite)
    }
    /*Room*/
}