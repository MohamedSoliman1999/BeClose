package com.example.beclose.Adapter

import android.app.Activity
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.CountDownTimer
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ShareCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.beclose.Distenation.ProfileFragmentDirections
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.Utils.ItemAnimation
import com.example.beclose.Utils.SharedDB
import com.example.beclose.model.DataField
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll


class ContactsArrayAdapterProfileRV(var arrayList: ArrayList<DataField>/*, private var rvOnClick: RVOnClick*/, private var activity: Activity?) : RecyclerSwipeAdapter<ContactsArrayAdapterProfileRV.ContactsHolder>() {
    init {

    }
    fun setList(arrayList: ArrayList<DataField>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        //Display item cardView in parent layout in Recycle View
        val view: View
        val mInflater = LayoutInflater.from(activity)
        view = mInflater.inflate(R.layout.contacts_item, parent, false)
        return ContactsHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        //holder.textView.setText(arrayList.get(position).textLine);
        holder.connectionIV.setImageResource(arrayList.get(position).icon!!)
        holder.fieldNameTV.setText(arrayList.get(position).fieldName)
//        holder.fieldContentTV.movementMethod=(LinkMovementMethod.getInstance());
//        holder.fieldContentTV.autoLinkMask= Linkify.EMAIL_ADDRESSES
//        var spanelString=SpannedString(arrayList.get(position).fieldContent)
        holder.fieldContentTV.setText(arrayList.get(position).fieldData)
        Linkify.addLinks(holder.fieldContentTV,Linkify.ALL)
        holder.fieldContentTV.paintFlags=FLAG_ACTIVITY_NEW_TASK

        holder.swipeItem.showMode = SwipeLayout.ShowMode.PullOut
////        holder.swipeItem.addDrag(SwipeLayout.DragEdge.Left, holder.leftWrapper);
        holder.swipeItem.addDrag(SwipeLayout.DragEdge.Right, holder.rightWrapper);
        mItemManger.bindView(holder.itemView, position);
//        onClick
        holder.editBtn.setOnClickListener {
        //check for internet
            if ((activity as MainActivity).showInternetSnakeBar()){
                var action =ProfileFragmentDirections.actionFromProfileToAddNewFieldToUpdate().setIsUpdate(arrayList.get(position))
                Navigation.findNavController(holder.itemView).navigate(action)
            }
        }
        holder.shareBtn.setOnClickListener {
            ShareCompat.IntentBuilder.from(activity as Activity)
                .setType("text/plain")
                .setChooserTitle("Share With")
                .setText(arrayList.get(position).fieldData)
                .startChooser();
        }
        holder.deleteBtn.setOnClickListener {
//            notifyItemChanged(position)
//            holder.swipeItem.addDrag(SwipeLayout.DragEdge.Left, holder.rightWrapper);
            if ((activity as MainActivity).showInternetSnakeBar()){
                holder.swipeItem.animate().translationX(2500f).alpha(1f).setDuration(800).setStartDelay(300).start()
                var countDownTimer = object : CountDownTimer(1000, 10) {
                    override fun onTick(millisUntilFinished_: Long) {
                    }
                    override fun onFinish() {
                        // do whatever when the bar is full
                        try {
//                            mItemManger.closeItem(position)
                            mItemManger.closeAllItems()
                            arrayList = java.util.ArrayList(arrayList).apply { removeAt(position) }
                            notifyDataSetChanged()
                            CoroutineScope(Dispatchers.IO).launch {
                                var localUser=SharedDB.getUser()
                                localUser.fields=arrayList
                                SharedDB.saveUSer(localUser)
                                saveFieldInFirebase(arrayList)
                            }
                        }catch (e:Exception){
                            Log.e("ContactAdapterProfile",e.message.toString())
                        }
                    }
                }.start()
            }


        }
        if (isCreated<arrayList.size){
            ItemAnimation.animateBottomUp(holder.itemView, position);
            isCreated++
        }
    }
    var isCreated=0
    fun saveFieldInFirebase(field:ArrayList<DataField>){
        var fUser: FirebaseUser =(activity as MainActivity).firebaseAuth.firebaseAuth.currentUser!!
        (activity as MainActivity).firebaseAuth.dbReference= FirebaseDatabase.getInstance().reference.child("Users").child(fUser.uid)
        (activity as MainActivity).firebaseAuth.dbReference.child("Fields").setValue(field)
    }
    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_contact_item
    }

    class ContactsHolder:RecyclerView.ViewHolder{
        lateinit var connectionIV:ImageView
        lateinit var fieldNameTV:TextView
        lateinit var fieldContentTV:TextView
        lateinit var swipeItem:com.daimajia.swipe.SwipeLayout
        lateinit var rightWrapper:LinearLayout
        lateinit var editBtn:ImageButton
        lateinit var shareBtn:ImageButton
        lateinit var deleteBtn: ImageButton

        constructor(itemView: View) : super(itemView) {
        connectionIV=itemView.findViewById(R.id.contacts_item_IV)
        fieldContentTV=itemView.findViewById(R.id.fileContent_TV)
        fieldNameTV=itemView.findViewById(R.id.filedName_TV)
        swipeItem=itemView.findViewById(R.id.swipe_contact_item)
        rightWrapper=itemView.findViewById(R.id.right_wrapper)
        editBtn=itemView.findViewById(R.id.edit)
        deleteBtn=itemView.findViewById(R.id.deleteBtn)
        shareBtn=itemView.findViewById(R.id.share)
        /*textView=itemView.findViewById(R.id.textLine_txt);
           textView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Toast.makeText(itemView.getContext(),arrayList.get(getAdapterPosition()).textLine+"",Toast.LENGTH_LONG).show();
               }
           });*/
    }
    }

}
