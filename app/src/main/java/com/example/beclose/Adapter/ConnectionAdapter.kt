package com.example.beclose.Adapter

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.BeCloserApp
import com.example.beclose.model.UserProfile
import com.example.beclose.R
import com.example.beclose.Repository.Repository
import com.google.firebase.firestore.auth.User
import com.like.LikeButton
import com.like.OnLikeListener
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ActivityScoped
class ConnectionAdapter() : RecyclerView.Adapter<ConnectionAdapter.ConnectionHolder>() {
    private lateinit var arrayList: ArrayList<UserProfile>
    private lateinit var mContext: Context
    lateinit var repository:Repository
    constructor(arrayList: List<UserProfile>,context: Context) : this() {
        this.mContext=context
        this.arrayList= arrayList as ArrayList<UserProfile>
    }
    fun setRepo(repository: Repository) {
        this.repository=repository
    }
    constructor(context: Context) : this() {
        this.mContext=context
        arrayList= ArrayList<UserProfile>()
    }

    fun setList(arrayListt: List<UserProfile>) {
        arrayList.addAll(arrayListt)
        arrayList= arrayList.distinctBy { it.roomID } as ArrayList<UserProfile>
        for (i in arrayList){
            Log.e("RoomID",i.roomID.toString()+" --- "+i.isFavourite)
        }
        notifyDataSetChanged()
        Toast.makeText(mContext.applicationContext, arrayList.size.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionHolder {
        //Display item cardView in parent layout in Recycle View
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.friends_item, parent, false)
        return ConnectionHolder(view)
    }

    override fun onBindViewHolder(holder: ConnectionHolder, position: Int) {
        Log.e("RoomID",arrayList.get(position).roomID.toString()+"## "+(position+1))
        holder.carditem.setOnClickListener {
            Toast.makeText(mContext, "Room: "+arrayList.get(position).roomID+" "+(position+1), Toast.LENGTH_SHORT).show()
        }
        holder.favouriteBtn.isLiked = arrayList.get(position).isFavourite==true
        //holder.textView.setText(arrayList.get(position).textLine);
        var mediaPlayer= MediaPlayer.create(BeCloserApp.gettINSTANCE(),R.raw.btn_click)
        holder.favouriteBtn.setOnLikeListener(object :OnLikeListener{
            override fun liked(likeButton: LikeButton?) {
                mediaPlayer.start()
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        repository.setFriendsFavourite(arrayList.get(position).roomID!!,true)
                    }catch (e:Exception){
                        Log.e("%%%%%%",e.message.toString())
                    }
                }
            }
            override fun unLiked(likeButton: LikeButton?) {
                mediaPlayer.start()
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        repository.setFriendsFavourite(arrayList.get(position).roomID!!,false)
                    }catch (e:Exception){
                        Log.e("%%%%%%",e.message.toString())
                    }
                }
            }

        })
        holder.deleteBtn.setOnClickListener {
            holder.carditem.animate().translationX(2500f).alpha(1f).setDuration(800).setStartDelay(300).start()
            var countDownTimer = object : CountDownTimer(1000, 10) {
                    override fun onTick(millisUntilFinished_: Long) {
                    }
                    override fun onFinish() {
                        // do whatever when the bar is full
                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                try{
                                    repository.deleteCashedFriend(arrayList.get(position).roomID!!)
                                }catch (e:Exception){
                                    Log.e("%%%%%%",e.message.toString())
                                }
                            }
                            arrayList = ArrayList(arrayList).apply { removeAt(position) }
                            notifyDataSetChanged()
                        }catch (e:Exception){}
                    }
                }.start()
        }
    }
    class ConnectionHolder:RecyclerView.ViewHolder{
    lateinit var favouriteBtn:com.like.LikeButton
    lateinit var deleteBtn:ImageView
    lateinit var carditem:ConstraintLayout
    constructor(itemView: View) : super(itemView) {
        favouriteBtn=itemView.findViewById<com.like.LikeButton>(R.id.favourite_connections_btn)
        deleteBtn=itemView.findViewById<ImageView>(R.id.delete_btn)
        carditem=itemView.findViewById<ConstraintLayout>(R.id.itemContainer)
    }
    }



    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(
        holder: ConnectionHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }
}
