package com.example.beclose.Adapter

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.R
import com.example.beclose.model.UserProfile
import com.google.firebase.firestore.auth.User
import com.like.LikeButton
import com.like.OnLikeListener

class FriendsPagedAdapter  : PagedListAdapter<UserProfile, FriendsPagedAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var favouriteBtn:com.like.LikeButton
        lateinit var deleteBtn: ImageView
        lateinit var carditem: ConstraintLayout
        fun bindView(userEntity: UserProfile) {
            favouriteBtn=itemView.findViewById<com.like.LikeButton>(R.id.favourite_connections_btn)
            deleteBtn=itemView.findViewById<ImageView>(R.id.delete_btn)
            carditem=itemView.findViewById<ConstraintLayout>(R.id.itemContainer)
            setitemData()
            }
        fun setitemData(){
            var item: UserProfile? =getItem(position)
            Log.e("bindingView","56+6545464")
            if (item != null) {
                Log.e("adapter",item.roomID.toString())
            }
            favouriteBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
//                ("Not yet implemented")
                }

                override fun unLiked(likeButton: LikeButton?) {
//                ("Not yet implemented")
                }

            })
            deleteBtn.setOnClickListener {
                carditem.animate().translationX(2500f).alpha(1f).setDuration(800).setStartDelay(300).start()
                var countDownTimer = object : CountDownTimer(1000, 10) {
                    override fun onTick(millisUntilFinished_: Long) {
                    }
                    override fun onFinish() {
                        // do whatever when the bar is full
                        try {
//                            arrayList = ArrayList(arrayList).apply { removeAt(position) }
//                            notifyDataSetChanged()
                        }catch (e:Exception){}
                    }
                }.start()
            }
        }
        }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserProfile>() {
            override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile):Boolean {
                Log.e("ID", oldItem.roomID.toString())
               return oldItem.roomID == newItem.roomID
            }


            override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile) =
                oldItem.equals(newItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.friends_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("444444","55555555")
        var item=getItem(position)
        if (item!=null){
            holder.bindView(item)
        }
    }
//
//    override fun getItemId(position: Int): Long {
//        return currentList!!.get(position)!!.roomID!!
//    }
//
    override fun getItemCount(): Int {
        if (currentList!=null){
            return currentList!!.size
        }
    return 0
    }
}