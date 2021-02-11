package com.example.beclose.Adapter

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.model.UserProfile
import com.example.beclose.R
import com.example.beclose.Utils.ItemAnimation
import com.example.beclose.Utils.SharedDB
import com.like.LikeButton
import com.like.OnLikeListener
import java.util.*


class NotificationItemAdapter(var arrayList: List<UserProfile>, private var mContext: Context?) : RecyclerView.Adapter<NotificationItemAdapter.ConnectionHolder>() {

    init {

    }
    fun setList(arrayList: List<UserProfile>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionHolder {
        //Display item cardView in parent layout in Recycle View
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.item_notification, parent, false)
        return ConnectionHolder(view)
    }
var start=0
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ConnectionHolder, position: Int) {
        //holder.textView.setText(arrayList.get(position).textLine);
        if (start<arrayList.size){
            ItemAnimation.animateFadeIn(holder.itemView, position);
            start+=1
        }
        holder.deleteBtn.setOnClickListener {
            holder.itemView.animate().translationX(2500f).alpha(1f).setDuration(800).setStartDelay(300).start()
            var countDownTimer = object : CountDownTimer(1000, 10) {
                override fun onTick(millisUntilFinished_: Long) {
                }
                override fun onFinish() {
                    // do whatever when the bar is full
                    try {
                        arrayList = java.util.ArrayList(arrayList).apply { removeAt(position) }
                        notifyDataSetChanged()
                    }catch (e:Exception){}
                }
            }.start()

        }
        holder.confirmBtn.setOnClickListener {
            holder.requestContainerLayout.visibility=View.INVISIBLE
            holder.textAcceptance.visibility=View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

//    class ConnectionHolder  //TextView textView;
//    (itemView: View) : RecyclerView.ViewHolder(itemView){
//
//    }
    class ConnectionHolder:RecyclerView.ViewHolder{
//    lateinit var favouriteBtn:com.like.LikeButton
    lateinit var deleteBtn: ImageButton
    lateinit var confirmBtn: ImageButton
    lateinit var requestContainerLayout: LinearLayout
    lateinit var textAcceptance:TextView
//    lateinit var carditem:ConstraintLayout
    constructor(itemView: View) : super(itemView) {
//        favouriteBtn=itemView.findViewById<com.like.LikeButton>(R.id.favourite_connections_btn)
        deleteBtn=itemView.findViewById<ImageButton>(R.id.notification_deleteBtn)
        confirmBtn=itemView.findViewById<ImageButton>(R.id.notification_confirm_btn)
        requestContainerLayout=itemView.findViewById<LinearLayout>(R.id.confirmationContainer)
        textAcceptance=itemView.findViewById(R.id.notification_acceptance_txt)
//        carditem=itemView.findViewById<ConstraintLayout>(R.id.itemContainer)
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
