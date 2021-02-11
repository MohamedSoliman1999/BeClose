package com.example.beclose.Distenation

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.beclose.MainActivity
import com.example.beclose.Utils.SharedDB
import com.example.beclose.VVM.VM.ScanFragmentVM
import com.example.beclose.databinding.FragmentScanQRBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanQRFragment : Fragment() {
    lateinit var vm:ScanFragmentVM
    lateinit var binding: FragmentScanQRBinding
    private val requestCodeCameraPermision=1001
    private var codeScanner: CodeScanner? =null
    private val REQUEST_IMAGE = 127
    private var notify=false
    //Try Catch when lunch fragment ############################
    //done with auto focus
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentScanQRBinding.inflate(inflater, container, false)
        var view=binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm= ViewModelProviders.of(this).get(ScanFragmentVM::class.java)
        (activity as MainActivity).hideFabVisibility(true)
        if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            askForCameraPermission()
        }else{
            setupControl2()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==requestCodeCameraPermision&&grantResults.isNotEmpty()){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            //Granted
                Toast.makeText(activity, "Permssion Granted", Toast.LENGTH_SHORT).show()
                setupControl2()
            }else{
                Toast.makeText(activity, "Permssion Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupControl2(){
       try{
           val activity = requireActivity()
           codeScanner = CodeScanner(activity, binding.surfaceViewScanner)
           codeScanner!!.decodeCallback = DecodeCallback {
               activity.runOnUiThread {
                   val str=it.text
                   var localUser=SharedDB.getUser()
                   localUser.friendsTokn!!.add(str)
                   SharedDB.saveUSer(localUser)
                   sendNotification(str)
//                   Toast.makeText(activity, localUser.friends?.size.toString(), Toast.LENGTH_LONG).show()
               }
           }
           binding.surfaceViewScanner.setOnClickListener {
               codeScanner!!.startPreview()
           }
       }catch (e: Exception){
           Toast.makeText(activity, "Some Thing wrong", Toast.LENGTH_SHORT).show()
       }
        setUserHandler()
    }
    fun askForCameraPermission(){
        activity?.let { ActivityCompat.requestPermissions(
            it,
            arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermision
        ) }
    }
    override fun onResume() {
    super.onResume()
    if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
        askForCameraPermission()
    }else{
        if(codeScanner==null){
            setupControl2()
        }else{
            codeScanner!!.startPreview()
        }
    }
}
    override fun onPause() {
        super.onPause()
        try{
            codeScanner!!.releaseResources()
        }catch (e: Exception){}
    }
    private fun setUserHandler(){
        animation()
        binding.uploadImageIV.setOnClickListener {
            pickImage(REQUEST_IMAGE)
        }
    }
    private fun animation(){
        binding.uploadImageIV.translationX=800f
        binding.uploadImageIV.alpha=0f
        binding.uploadImageIV.animate().translationX(0f).alpha(1f).setDuration(2000).setStartDelay(
            700
        ).start()
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
        if (resultCode == Activity.RESULT_OK) {
           // val thumbnail = data!!.data
            val selectedImageUri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(
                activity?.getContentResolver(),
                selectedImageUri
            )
//            binding.uploadImageIV.setImageBitmap(bitmap)
            var localUser=SharedDB.getUser()
            encodeBitmapImage(bitmap)?.let { localUser.friendsTokn!!.add(it) }
            SharedDB.saveUSer(localUser)
//            Toast.makeText(activity, encodeBitmapImage(bitmap), Toast.LENGTH_SHORT).show()
         /*   val pathUtil:PathUtil = PathUtil(activity)
            val path: String = pathUtil.getPath(thumbnail)
            if (requestCode == REQUEST_IMAGE) {
                firstImagePath = path
                binding.fOfferManagementSdv1.setImageURI(thumbnail)
            }*/

        }
    }
    private fun encodeBitmapImage(bitmap: Bitmap):String?{
            var width = bitmap.getWidth()
            var height = bitmap.getHeight();
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            var source = RGBLuminanceSource(width, height, pixels);
            var bBitmap = BinaryBitmap(HybridBinarizer(source));
            var reader = MultiFormatReader();
            try {
                var result = reader.decode(bBitmap);
                notify=true
                sendNotification(result.toString())
                return result.toString();
            }
            catch (e: Exception) {
                Log.e(TAG, "decode exception", e);
                Toast.makeText((activity as MainActivity).applicationContext, "Sorry that is not User", Toast.LENGTH_SHORT).show()
                return null;
            }
    }
    fun sendNotification(uID: String){
        Log.e("SendNotification",uID.toString())
       if (uID!=null&&uID.length>5){
           try {
               vm.getFirebaseInstance().dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(uID).child("token")
               vm.getFirebaseInstance().dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                           var token=snapshot.getValue(String::class.java)
                           if (token != null&&token.length>5) {
                                   Log.e("tttttt",token.toString())
                               vm.getFirebaseInstance().notificationOnClick(token)
                               Toast.makeText((activity as MainActivity).applicationContext, "Request is sent successfully", Toast.LENGTH_SHORT).show()
                           }else{
                               Toast.makeText((activity as MainActivity).applicationContext, "Sorry that is not User", Toast.LENGTH_SHORT).show()
                           }
                           Log.e("getToken",snapshot.getValue().toString())
                   }
                   override fun onCancelled(error: DatabaseError) {
                       Toast.makeText((activity as MainActivity).applicationContext, "Sorry that is not User", Toast.LENGTH_SHORT).show()
                       Log.e("####FireBaseAuth",error.message)
                   }
               })
           }catch (e:Exception){
               Toast.makeText((activity as MainActivity).applicationContext, "Sorry that is not User", Toast.LENGTH_SHORT).show()
               Log.e("getToken",e.message.toString())
           }
       }else{
           Toast.makeText((activity as MainActivity).applicationContext, "Sorry that is not User", Toast.LENGTH_SHORT).show()
       }
    }
}