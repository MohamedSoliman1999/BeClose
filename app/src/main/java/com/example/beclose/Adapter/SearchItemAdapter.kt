package com.example.beclose.Adapter

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.model.UserProfile
import com.example.beclose.R
import com.example.beclose.Utils.ItemAnimation
import com.like.LikeButton
import com.like.OnLikeListener
import java.util.*


class SearchItemAdapter(var arrayList: List<UserProfile>, private var mContext: Context?) : RecyclerView.Adapter<SearchItemAdapter.ConnectionHolder>() {

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
        view = mInflater.inflate(R.layout.item_search, parent, false)
        return ConnectionHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ConnectionHolder, position: Int) {
        //holder.textView.setText(arrayList.get(position).textLine);
        ItemAnimation.animateFadeIn(holder.itemView, position);
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
//    lateinit var deleteBtn:ImageView
//    lateinit var carditem:ConstraintLayout
    constructor(itemView: View) : super(itemView) {
//        favouriteBtn=itemView.findViewById<com.like.LikeButton>(R.id.favourite_connections_btn)
//        deleteBtn=itemView.findViewById<ImageView>(R.id.delete_btn)
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
