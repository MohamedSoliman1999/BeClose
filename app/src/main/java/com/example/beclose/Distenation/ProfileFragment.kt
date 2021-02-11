package com.example.beclose.Distenation

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclose.Adapter.BottomSheetFragment
import com.example.beclose.Adapter.BottomSheetFragmentDirections
import com.example.beclose.Adapter.ContactsArrayAdapterProfileRV
import com.example.beclose.MainActivity
import com.example.beclose.Utils.SharedDB
import com.example.beclose.VVM.Observers.IOnClickListenerBottomSheet
import com.example.beclose.VVM.VM.ProfileFragmentVM
import com.example.beclose.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(),IOnClickListenerBottomSheet {
    lateinit var vm: ProfileFragmentVM
    lateinit var binding: FragmentProfileBinding
    lateinit var adapter: ContactsArrayAdapterProfileRV
    private val GALLERY_REQUEST_CODE = 1234
    var navHostFragment: NavHostFragment? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm=ViewModelProvider(this).get(ProfileFragmentVM::class.java)
        userHandler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    fun userHandler(){
        setStatus()
        BottomSheetFragment.onClick =this
        binding.profileImage!!.setOnClickListener {
            showButtomsheet()
        }
        setupFab()
        var profileBitmap:Bitmap=SharedDB.getLocalImageProfile()
        binding.profileImage!!.setImageBitmap(profileBitmap)
        setRecycleView()
        binding.profileBackBtnIV!!.setOnClickListener {
            requireActivity().onBackPressed()
        }
//        binding.btnGroup.selectButtonWithAnimation(binding.publicBtn)
        binding.privateBtn!!.setOnClickListener {
            if ((activity as MainActivity).showInternetSnakeBar()){
                saveStatusInFirebase(0)
            }else{
                setStatus()
            }
        }
        binding.publicBtn!!.setOnClickListener {
            if ((activity as MainActivity).showInternetSnakeBar()){
                saveStatusInFirebase(1)
            }else{
                setStatus()
            } }
        binding.lockBtn!!.setOnClickListener {
            if ((activity as MainActivity).showInternetSnakeBar()){
                saveStatusInFirebase(-1)
            }else{
                setStatus()
            }
        }
    }
    private fun setStatus(){
        var status=SharedDB.getUser().public
        Log.e("##Profile",status.toString())
        when (status) {
            1 -> {
                Log.e("%%%%%%%%%%%%%%%","%%%%%%%%%%%")
                binding.btnGroup!!.selectButtonWithAnimation(binding.publicBtn)
            }
            0 -> {
                binding.btnGroup!!.selectButtonWithAnimation(binding.privateBtn!!)
                Log.e("@@@@@@@@@@@@","@@@@@@@@@@@@@@@@@@@@@2")
    //            binding.privateBtn?.let { binding.btnGroup.selectButtonWithAnimation(it) }
            }
            -1 -> {
                binding.btnGroup!!.selectButtonWithAnimation(binding.lockBtn!!)
                Log.e("$$$$$$$$$$$$$$$$$$$$$","$$$$$$$$$$$$$")
    //            binding.lockBtn?.let { binding.btnGroup.selectButtonWithAnimation(it) }
            }
            else -> {
                binding.btnGroup!!.selectButtonWithAnimation(binding.publicBtn)
                Log.e("jjjjjjj","jjjjjjjjjj")
                saveStatusInFirebase(1)
            }
        }
    }
    private fun showButtomsheet() {
        var d = binding.profileImage.drawable
        val bitmap: Bitmap = (d as BitmapDrawable).bitmap
        var action= ProfileFragmentDirections.actionFromProfileFragmentToBottomSheet(bitmap)
        Navigation.findNavController(requireView()).navigate(action)
    }
    private fun pickImage(requestCode: Int) {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, requestCode)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selectedImageUri = data?.data
                    launchImageCrop(selectedImageUri!!)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        if ((activity as MainActivity).showInternetSnakeBar()){
                            binding.profileImage.setImageURI(result.uri)
                            SharedDB.saveLocalImageProfile(binding.profileImage.drawable.toBitmap())
                            SharedDB.encodeTobase64(binding.profileImage.drawable.toBitmap())?.let {
                                saveProfileImageInFirebase(
                                    it
                                )
                            }
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Log.e(TAG, "Crop error: ${result.getError()}")
                    }
                }
            }
        }
    }
    private fun launchImageCrop(uri: Uri){
       var intent= CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1920, 1920)
                .setCropShape(CropImageView.CropShape.OVAL) // default is rectangle
                .getIntent(requireContext())
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
    }
    override fun onClickListenerSelectImage() {
        //        check for internet
        pickImage(GALLERY_REQUEST_CODE)
    }
    override fun onClickListenerViewImage() {
        var d=binding.profileImage.drawable
        val bitmap = (d as BitmapDrawable).bitmap
        var action= BottomSheetFragmentDirections.actionFromProfileToFullScreenSheet(bitmap)
        Navigation.findNavController(requireView()).navigate(action)
    }
    private fun setupFab(){
        binding.fab.setOnClickListener {
            //check internet
            if ((activity as MainActivity).showInternetSnakeBar()){
                var action=ProfileFragmentDirections.actionFromProfileToAddNewField().setIsFromProfile(1)
                Navigation.findNavController(requireView()).navigate(action)
            }

        }
        binding.userDataRV.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy>-1){
                    binding.fab.collapse()
                }else{
                    binding.fab.expand()
                }
            }
        })
    }
    private fun setRecycleView() {
        var localUser=SharedDB.getUser()
        adapter = ContactsArrayAdapterProfileRV(localUser.fields!!, requireActivity())
       // var linearLayoutManager: LinearLayoutManager = ZoomRecyclerLayout(activity!!.application.applicationContext)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(requireActivity().application.applicationContext)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        binding.userDataRV.layoutManager = linearLayoutManager
//        binding.connectionsRV.isNestedScrollingEnabled=false
        binding.userDataRV.adapter = adapter

//        modelView = ViewModelProviders.of(this).get(ScanViewModel::class.java)
//        modelView.liveArray.observe(this, Observer {
//            adapter!!.setList(it)
//        })
    }
    override fun onPause() {
        binding.fab.collapse()
        super.onPause()
    }
    fun saveProfileImageInFirebase(imageString:String){
        CoroutineScope(Dispatchers.IO).launch {
            var fUser:FirebaseUser=vm.getFirebase().firebaseAuth.currentUser!!
            vm.getFirebase().dbReference= FirebaseDatabase.getInstance().reference.child("Users").child(fUser.uid)
            vm.getFirebase().dbReference.child("image").setValue(imageString)
        }
    }
    fun saveStatusInFirebase(status:Int){
        CoroutineScope(Dispatchers.IO).launch {
            var localUser=SharedDB.getUser()
            localUser.public=status
            SharedDB.saveUSer(localUser)
            var fUser:FirebaseUser=vm.getFirebase().firebaseAuth.currentUser!!
            vm.getFirebase().dbReference= FirebaseDatabase.getInstance().reference.child("Users").child(fUser.uid)
            vm.getFirebase().dbReference.child("public").setValue(status)
        }


    }

}