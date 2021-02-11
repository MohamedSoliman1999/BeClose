package com.example.beclose

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.beclose.Forms.Firebase.BeCloseFirebaseAuth
import com.example.beclose.Utils.SharedDB
import com.example.beclose.VVM.Observers.IOnBackPressed
import com.example.beclose.VVM.VM.MainActivityVM
import com.example.beclose.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton


@AndroidEntryPoint
@Singleton
@ActivityScoped
open class MainActivity : AppCompatActivity() {
    lateinit var vm: MainActivityVM
    lateinit var binding: ActivityMainBinding
    private lateinit var view:View
    var navHostFragment: NavHostFragment? = null
    private var onBackPressedListener: IOnBackPressed? = null
    lateinit var firebaseAuth: BeCloseFirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        view=binding.root
        setContentView(view)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.activity_app_nav_host_fragment) as NavHostFragment?
        //NavigationUI.setupWithNavController(binding.activityAppBottomNavBar, navHostFragment!!.navController )

        handleUserInteractions()
        vm=ViewModelProvider(this).get(MainActivityVM::class.java)
        firebaseAuth=vm.getFirebase()
//        firebaseAuth.observeForRequest()//on work Manager
        //setContentView(R.layout.activity_main)

    }
    public fun hideFabVisibility(hideFabAlone: Boolean) {
//        if (hideFabAlone) {
//            binding.activityAppFabAdd.visibility = View.GONE
//        } else {
//            binding.activityAppFabAdd.visibility = View.VISIBLE
//        }
    }
    private fun handleUserInteractions() {
//        showMyDialog(1)
     /*   binding.activityAppFabAdd.setOnClickListener { //                Fragment f = getForegroundFragment();
            val destination: NavDestination ?= getFragment()
            Log.d("yup", "f name id: " + destination?.id)
            Log.d("yup", "f name label: " + destination?.label)
            Log.d("yup", "f name nav name: " + destination?.navigatorName)
            if ("fragment_home" == destination?.label) {
                //navHostFragment?.getNavController()?.navigate(R.id.action_to_add_offer)
            } else if ("fragment_branches" == destination?.label) {
               // navHostFragment?.getNavController()?.navigate(R.id.action_to_add_branch)
            } else if ("fragment_notifications" == destination?.label) {
            }
        }*/

        setStatusBarColor(R.color.black)
    }
    fun getFragment(): NavDestination? {
        return NavHostFragment.findNavController(supportFragmentManager.primaryNavigationFragment!!.requireFragmentManager().fragments[0]).currentDestination
        //return navHostFragment == null ? null : navHostFragment.getNavController().getCurrentDestination().getId();
    }
    fun setStatusBarVisiablie(isVisible: Boolean){
        if(isVisible){
            // Show status bar
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            // Hide status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
    @JvmName("setOnBackPressedListener1")
    fun setOnBackPressedListener(onBackPresssedListener: IOnBackPressed?) {
        this.onBackPressedListener = onBackPresssedListener
    }
    override fun onBackPressed() {
        setStatusBarVisiablie(true)
        Log.e("####", "shffasdfdgf")
        if (onBackPressedListener != null) {
            Log.e("####", "shffasdfdgf")
            onBackPressedListener!!.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
    fun removeFragment(fragment: Fragment){
        //current fragment
        //val fragment = this.supportFragmentManager.findFragmentById(R.id.activity_app_nav_host_fragment)!!
        val manager: FragmentManager = this.supportFragmentManager
        val trans: FragmentTransaction = manager.beginTransaction()
        trans.remove(fragment)
        trans.commit()
        manager.popBackStack()
    }
    public fun setStatusBarColor(color: Int){
        val window: Window = getWindow()
// clear FLAG_TRANSLUCENT_STATUS flag:
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
// finally change the color
// finally change the color
        if(color==-1){
            window.setDecorFitsSystemWindows(true)
        }else{
            window.setStatusBarColor(ContextCompat.getColor(this, color))
        }
    }
    fun hideKeyBoard(){
        val view = this!!.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
    fun getCurrentFragment():Fragment{
        val currentFragment = navHostFragment!!.childFragmentManager.fragments[0]
        return currentFragment
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseAuth.onActivityResult(requestCode, resultCode, data)
    }
    fun shareImageUri(bitmap: Bitmap) {
        val bitmapPath: String = Images.Media.insertImage(contentResolver, bitmap, "title", null)
        val bitmapUri = Uri.parse(bitmapPath)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
//        intent.type = "image/png"
        startActivity(intent)
    }
    fun showMyDialog(isDescription: Int) {
         var jobTitleTV="Please Add Your Description or Jop Title"
         var resetPasswordTV="Please enter your email to reset password"
         var jobBtnTV="Submit"
         var resetBtnTV="Reset Password"
         var builder: AlertDialog.Builder= AlertDialog.Builder(this, R.style.AlertDialogTheme)
         var alertview:View = LayoutInflater.from(this).inflate(
             R.layout.description_or_reset_dialog, findViewById<ConstraintLayout>(
                 R.id.layoutDialogt
             )
         )
         builder.setView(alertview)
         var alertDialog=builder.create()
         alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         alertDialog.show()
         val alertBtn=alertview.findViewById<Button>(R.id.alert_btn_sign_in)
         val alertET=alertview.findViewById<com.google.android.material.textfield.TextInputEditText>(
             R.id.alert_tiet_email_address
         )
         val alertMessageTV=alertview.findViewById<TextView>(R.id.alert_message)
         val alertETGoogle=alertview.findViewById<com.google.android.material.textfield.TextInputLayout>(
             R.id.dialog_til_email_address
         )
         if (isDescription==1){
             alertBtn.text=jobBtnTV
             alertETGoogle.hint="Job Description"
             alertMessageTV.text=jobTitleTV
         }else{
             alertBtn.text=resetBtnTV
             alertMessageTV.text=resetPasswordTV
         }
         alertBtn.setOnClickListener {

             if (alertET.text.isNullOrEmpty()){
                 Toast.makeText(
                     this.applicationContext,
                     "Please Fill the Field",
                     Toast.LENGTH_SHORT
                 ).show()
             }else{

                 if (isDescription==1){
                     if(alertET.text.toString().length>50){
                         Toast.makeText(
                             this.applicationContext,
                             "Sorry your description is too long",
                             Toast.LENGTH_SHORT
                         ).show()
                     }else{
//                         done
                         var localUser=SharedDB.getUser()
                         localUser.description=alertET.text.toString()
                         SharedDB.saveUSer(localUser)
                         alertDialog.dismiss()
                     }
                 }else{
//                     resete password
                     firebaseAuth.firebaseAuth.sendPasswordResetEmail(alertET.text.toString()).addOnSuccessListener {
                         Log.e("$###Fsucces", "sfdfadsfsdsd")
                         Toast.makeText(
                             this.applicationContext,
                             "You Will Receive The Reset Link Soon",
                             Toast.LENGTH_SHORT
                         ).show()
                         alertDialog.dismiss()
                     }.addOnFailureListener {
                         Log.e("$###", "sfdfadsfsdsd" + it.message)
                         Toast.makeText(
                             this.applicationContext,
                             "Error: " + it.message,
                             Toast.LENGTH_SHORT
                         ).show()
                     }
                 }
//                 Toast.makeText(this, alertET.text, Toast.LENGTH_SHORT).show()

             }

         }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
    fun showSnakeBar(txt: String?, duration: Int, textColor: Int) {
        val snackbar = Snackbar.make(view, txt!!, Snackbar.LENGTH_INDEFINITE)
        snackbar.setDuration(duration)
        snackbar.setTextColor(this.resources.getColor(textColor))
        snackbar.show()
    }
    var internetLastStatus=-1
    fun showInternetSnakeBar():Boolean{
        var isconnected=isNetworkConnected()
        if(!isconnected){
            showSnakeBar(txt = "Bad Internet Connection",4000,R.color.red)
            internetLastStatus=1
        }else if(internetLastStatus==1){
            showSnakeBar(txt = "Internet Is Connected Successfully",4000,R.color.colorGreenMessage)
            internetLastStatus=-1
        }
        return isconnected
    }
}