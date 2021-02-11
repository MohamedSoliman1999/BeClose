package com.example.beclose.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.R
import com.example.beclose.model.WalkThroughModel

class WalkThroughAdapter (private var arrayList:ArrayList<WalkThroughModel>): RecyclerView.Adapter<WalkThroughAdapter.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.walk_through_item,parent,false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.bind(arrayList.get(position))
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }
    inner class BindingViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var title_txt=view.findViewById<TextView>(R.id.title_txt_walk_through)
        var desctiption=view.findViewById<TextView>(R.id.description_txt_walk_through)
        var imageV=view.findViewById<ImageView>(R.id.image_walk_through)
        fun bind(item:WalkThroughModel){
            title_txt.setText(item.title)
            desctiption.setText(item.description)
            imageV.setImageResource(item.image)
        }
    }
}