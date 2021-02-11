package com.example.beclose.Adapter

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.BeCloserApp
import com.example.beclose.R
import com.example.beclose.model.UserProfile
import okhttp3.internal.notifyAll
import java.util.ArrayList

class FavouriteAdapter (var arrayList: List<UserProfile>, private var mContext: Context?) : RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder>() {

    init {

    }

    fun setList(arrayList: List<UserProfile>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteHolder {
        //Display item cardView in parent layout in Recycle View
        var view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.favourite_friend_item, parent, false)

        return FavouriteHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: FavouriteHolder, position: Int) {
        //holder.textView.setText(arrayList.get(position).textLine);
        var mediaPlayer=MediaPlayer.create(BeCloserApp.gettINSTANCE(),R.raw.btn_click)
        var isClicked = true
        holder.favouriteBtn.setOnClickListener {
            mediaPlayer.start()
            if (isClicked) {
                holder.favouriteBtn.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_baseline_favorite_red_border_24))
                isClicked = false
            } else {
                holder.favouriteBtn.setImageDrawable(mContext!!.getDrawable(R.drawable.ic_baseline_favorite_24))
                isClicked = true
            }
            holder.itemContainer.animate().translationY(-3500f).alpha(1f).setDuration(800).setStartDelay(300).start()
            var countDownTimer = object : CountDownTimer(1000, 10) {
                override fun onTick(millisUntilFinished_: Long) {
                }
                override fun onFinish() {
                    // do whatever when the bar is full
                    try {
                        arrayList = ArrayList(arrayList).apply { removeAt(position) }
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        notifyDataSetChanged()
                    }
                }
            }.start()
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    //    class ConnectionHolder  //TextView textView;
//    (itemView: View) : RecyclerView.ViewHolder(itemView){
//
//    }
    class FavouriteHolder : RecyclerView.ViewHolder {
        lateinit var favouriteBtn: ImageView
        lateinit var deleteBtn: ImageView
        lateinit var itemContainer: ConstraintLayout

        constructor(itemView: View) : super(itemView) {
            favouriteBtn = itemView.findViewById<ImageView>(R.id.favourite_btn)
//            deleteBtn = itemView.findViewById<ImageView>(R.id.delete_btn)
            itemContainer = itemView.findViewById<ConstraintLayout>(R.id.itemContainer)
            /*textView=itemView.findViewById(R.id.textLine_txt);
               textView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(itemView.getContext(),arrayList.get(getAdapterPosition()).textLine+"",Toast.LENGTH_LONG).show();
                   }
               });*/
        }
    }

    fun getWordItemAt(id: Int): UserProfile? {
        return arrayList[id]
    }

}