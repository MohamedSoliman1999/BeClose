package com.example.beclose.Distenation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.beclose.Adapter.ContactsAdapterAutoComplete
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.Utils.SharedDB
import com.example.beclose.VVM.Observers.AutoCompleteCallBack
import com.example.beclose.VVM.VM.NewFieldFragmentVM
import com.example.beclose.databinding.FragmentNewFieldBinding
import com.example.beclose.model.DataField
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewFieldFragment : Fragment(), AutoCompleteCallBack {
    lateinit var vm:NewFieldFragmentVM
    lateinit var binding:FragmentNewFieldBinding
    var mArrayList=ArrayList<DataField>()
    var isUpdate:DataField?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentNewFieldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm=ViewModelProvider(this).get(NewFieldFragmentVM::class.java)
        if (arguments!=null){
            isUpdate=NewFieldFragmentArgs.fromBundle(requireArguments()).isUpdate
            if (isUpdate != null) {
                binding.fFieldNameACTV.setText(isUpdate!!.fieldName)
                binding.fFieldNameACTV.isEnabled=false
                binding.fAuthTilUserType.isEnabled=false
                binding.fFieldContentTiet.setText(isUpdate!!.fieldData)
                binding.fNewFieldBtnAdd.setText("Update")
            }
        }
        userHanduler()
    }
    private fun userHanduler(){
        mArrayList=setUpAutoComplete()
        binding.fFieldNameACTV.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.fFieldNameACTV.setText("")
            }
            mArrayList= setUpAutoComplete()
        }
        binding.fNewFieldBtnAdd.setOnClickListener {
            if ((activity as MainActivity).showInternetSnakeBar()){
                if (binding.fFieldNameACTV.text.isNullOrEmpty()||binding.fFieldContentTiet.text.isNullOrEmpty()){
                    Toast.makeText(requireActivity(), "Please Fill The Empty Fields", Toast.LENGTH_SHORT).show()
                }else{
                    showAlert()
                }
            }

        }
    }
    override fun getFieldName(name: String) {
        binding.fFieldNameACTV.setText(name)
        binding.fFieldContentTiet.requestFocus()
    }
    private fun setUpAutoComplete():ArrayList<DataField>{
        var arrayList=ArrayList<DataField>()
        var autoCompleteFieldName= arrayListOf<String>("Gmail", "Facebook", "Instagram", "YouTube", "Yahoo", "WhatsApp", "Google", "Github","Location", "Linked In", "Phone",  "Mail", "Website", "Address")
        var autoCompleteFieldIcon= arrayListOf<Int>(
                (R.drawable.ic_googleblack),
                (R.drawable.ic_facebookblack),
                (R.drawable.ic_instagramblack),
                (R.drawable.ic_youtubeblack),
                R.drawable.ic_yahoo,
                R.drawable.ic_whatsappblack,
                R.drawable.ic_googleblack,
                R.drawable.ic_githubblack,
                R.drawable.ic_baseline_location_on_24,
                R.drawable.ic_linkedinblack,
                R.drawable.ic_baseline_phone_in_talk_24,
                R.drawable.ic_baseline_attach_email_24,
                R.drawable.ic_baseline_insert_link_24,
                R.drawable.ic_baseline_location_on_24,
        )

        for (i in 0 until autoCompleteFieldName.size){
            var item=DataField(autoCompleteFieldName.get(i), autoCompleteFieldIcon.get(i))
            arrayList.add(item)
        }
        var contactAdapter= ContactsAdapterAutoComplete(requireActivity().applicationContext, 0, arrayList)
        contactAdapter.setAutoCompleteClickListenerclick(this)
//        var adapter=ArrayAdapter<String>(activity!!.applicationContext,android.R.layout.simple_dropdown_item_1line,autoCompleteArray)
        binding.fFieldNameACTV.setAdapter(contactAdapter)
        return arrayList
    }
    private fun showAlert(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage("Do You Want To Add Another Item?")
        builder.setTitle("Confirm")
        builder.setCancelable(false)
        builder
                .setPositiveButton(
                        "Yes",
                        DialogInterface.OnClickListener { dialog, which -> // When the user click yes button
                            // then app will close
                            if ((activity as MainActivity).showInternetSnakeBar()){
                                addField()
                                binding.fNewFieldBtnAdd.setText("Add")
                            }
                        })

        builder
                .setNegativeButton(
                        "No",
                        DialogInterface.OnClickListener { dialog, which -> // If user click no
                            // then dialog box is canceled.
                            if ((activity as MainActivity).showInternetSnakeBar()){
                                (activity as MainActivity).hideKeyBoard()
                                dialog.cancel()
                                addField()
                                if (arguments!=null&&NewFieldFragmentArgs.fromBundle(requireArguments()).isFromProfile==-1){
                                    if(isUpdate!=null){
                                        requireActivity().onBackPressed()
                                    }else{
                                        Navigation.findNavController(requireView()).navigate(R.id.action_from_add_newField_to_home)
                                    }
                                }else{
                                    requireActivity().onBackPressed()
                                }
                            }
                        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun getBitmap(fieldName:String):Int?{
        var image:Int?=null
        for (i in 0 until mArrayList.size){
           if(mArrayList.get(i).fieldName==fieldName){
               image=mArrayList.get(i).icon
               return image
           }
        }
        return (R.drawable.ic_baseline_website_24)
    }
    fun addField(){
        var user = SharedDB.getUser()
        var data=DataField(binding.fFieldNameACTV.text.toString(), binding.fFieldContentTiet.text.toString(), getBitmap(binding.fFieldNameACTV.text.toString()))
        var indx=-1
        if (isUpdate!=null){
            indx= user.fields!!.indexOfFirst{
                it.fieldName == isUpdate!!.fieldName
                it.fieldData==isUpdate!!.fieldData
            }
        }
        if (indx>-1){
            user.fields!!.get(indx).fieldData=data.fieldData
        }else{
            user.fields!!.add(data)
        }
        SharedDB.saveUSer(user)
        saveFieldInFirebase(user.fields!!)
        Toast.makeText(context, SharedDB.getUser().fields!!.size.toString(), Toast.LENGTH_SHORT).show()
    }
    fun updateField(field:DataField,newField:DataField){
        var user = SharedDB.getUser()
        var indxToUpdate=user.fields!!.indexOf(field)
        user.fields!!.get(indxToUpdate).fieldData =newField.fieldData
//        user.fields!!.add(DataField(binding.fFieldNameACTV.text.toString(), binding.fFieldContentTiet.text.toString(), getBitmap(binding.fFieldNameACTV.text.toString())))
        SharedDB.saveUSer(user)
        Toast.makeText(context, SharedDB.getUser().fields!!.size.toString(), Toast.LENGTH_SHORT).show()
    }
    fun saveFieldInFirebase(field:ArrayList<DataField>){
        var fUser: FirebaseUser =vm.getFirebase().firebaseAuth.currentUser!!
        vm.getFirebase().dbReference= FirebaseDatabase.getInstance().reference.child("Users").child(fUser.uid)
        vm.getFirebase().dbReference.child("Fields").setValue(field)
    }
}