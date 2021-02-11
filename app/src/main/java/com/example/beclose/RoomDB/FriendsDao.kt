package com.example.beclose.RoomDB

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beclose.model.UserProfile

@Dao
interface FriendsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  fun insertFriend(user: UserProfile)
    @Query("delete from FriendsUserTable where roomID=:friendId")
    public  fun deleteFriend(friendId: Long)
    @Query("delete from friendsusertable")
    public fun removeAll()
    @Query("select* from FriendsUserTable ORDER BY name ASC")
    public  fun getFriends():List<UserProfile>//LiveData<PagedList<UserProfile>>
    @Query("select* from FriendsUserTable where roomID in(:list)  ORDER BY roomID ASC")
    public  fun getFriends1(list: List<Int>): List<UserProfile>//LiveData<PagedList<UserProfile>>
    @Query("select* from FriendsUserTable where isFavourite=:isFavourite ORDER BY name ASC")
    public  fun getFavouriteFriends(isFavourite: Boolean):LiveData<List<UserProfile>>
    @Query("SELECT COUNT(roomID) FROM FriendsUserTable")
    fun getRowCount(): Int?
    @Query("UPDATE FriendsUserTable SET  isFavourite = :isFavourite WHERE roomID = :id")
    fun setFavourite(id:Long,isFavourite:Boolean)
}