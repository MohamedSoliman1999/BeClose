package com.example.beclose.Forms.Firebase

import android.app.Activity
import android.content.ClipData.Item
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.beclose.Forms.Firebase.Notification.*
import com.example.beclose.Forms.Firebase.Notification.FCMContacts.Companion.TOPIC
import com.example.beclose.MainActivity
import com.example.beclose.R
import com.example.beclose.Utils.SharedDB
import com.example.beclose.VVM.Observers.OnActivityResult
import com.example.beclose.model.DataField
import com.example.beclose.model.UserProfile
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.image.CloseableBitmap
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.*
import java.net.URL
import javax.inject.Inject


@ActivityScoped
class BeCloseFirebaseAuth : OnActivityResult {
    constructor()
//    if login for first will pass to home but shold check for number of fields
    private lateinit var fragment: Fragment
    public lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123
    private lateinit var fbUser:FirebaseUser
    var notify=false
    //    public lateinit var user: FirebaseUser
    //database
    lateinit var dbReference: DatabaseReference //for retriveng lestener
    lateinit var fbStore:FirebaseFirestore  //for push data inside db
    lateinit var activity:Activity

    @Inject
    lateinit var retrofitInstance:NotificationAPI
    @Inject
    constructor(a: Activity) {
        activity=a
        authenticationRequest()
    }
    fun setCurrentFragment(){
        this.fragment =(activity as MainActivity).getCurrentFragment()
    }
    //google auth
    private fun authenticationRequest(){
        // auth instance
        firebaseAuth= FirebaseAuth.getInstance()
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity!!.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // [END config_signin]
        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)

    }
    public fun signInGoogleForClick() {
        //FirebaseAuth.getInstance().signOut()
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
//        if (firebaseAuth.currentUser!=null){
//            Navigation.findNavController(fragment.view!!).navigate(R.id.action_from_LoginContainer_to_home)
//        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        // [START_EXCLUDE silent]
        // showProgressBar()
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithCredential:success")
                        val user = firebaseAuth.currentUser
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                        // [START_EXCLUDE]
                        //   val view = binding.mainLayout
                        // [END_EXCLUDE]
                        Snackbar.make(
                            fragment.requireView(),
                            "Authentication Failed.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        // updateUI(null)
                    }

                    // [START_EXCLUDE]
                    //hideProgressBar()
                    // [END_EXCLUDE]
                }
    }
    public fun onAppStart() {
        if(firebaseAuth.currentUser!=null){
            fbUser= firebaseAuth.currentUser!!

            if(fragment.view!=null){
                var localUser=SharedDB.getUser()
                if(fbUser!=null&&localUser.fields!!.size>0){
//                    getUserData  ()
//                    observeForRequest()//on work Manager
                    Log.e(
                        "######",
                        "Tok " + fbUser.getUid() + "  uid " + fbUser.uid + "   Token " + FirebaseInstanceId.getInstance().token
                    )
//                    updateUserData()//444444444444444444444444444444444444444444444444444
                    Navigation.findNavController(fragment.requireView()).navigate(R.id.action_from_splash_to_home)
                }else{
                    //goto add fields
                    Navigation.findNavController(fragment.requireView()).navigate(R.id.action_from_splash_to_add_newField)
                }
            }
            //FirebaseAuth.getInstance().signOut()
            /* var googleSignInAccount:GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity!!.applicationContext)!!
             googleSignInClient.getEmail()*/
        }else{
            //else new Login
            Navigation.findNavController(fragment.requireView()).navigate(R.id.action_from_splash_to_loginContainer)
        }
    }
    public fun afterLogin() {
        try {
            fbUser=firebaseAuth.currentUser!!
                if(fragment.view!=null){
//                    observeForRequest()//on work Manager
                    var localUser=SharedDB.getUser()
                    localUser.token=FirebaseInstanceId.getInstance().token!!
                    localUser.uId=fbUser.uid
                    SharedDB.saveUSer(localUser)
                    Navigation.findNavController(fragment.requireView()).navigate(R.id.action_from_LoginContainer_to_walk_through)

                }

        }catch (e: Exception){}

    }
    //Email
    public fun signUpEmailForClick(email: String, password: String, fullName: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                Toast.makeText(
                    activity.applicationContext,
                    "Sign Up Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                var localUser = SharedDB.getUser()
                localUser.name = fullName
                localUser.password = password
                localUser.fields!!.add(
                    DataField(
                        "Email",
                        email,
                        R.drawable.ic_baseline_attach_email_24
                    )
                )
                localUser.token = FirebaseInstanceId.getInstance().token!!
                localUser.uId = fbUser.uid
                SharedDB.saveUSer(localUser)
                Log.e("Firebase", firebaseAuth.currentUser.toString())
                if (firebaseAuth.currentUser !== null) {
                    updateUserData()
                    afterLogin()
                } else {
                    var countDownTimer = object : CountDownTimer(2000, 6) {
                        override fun onTick(millisUntilFinished_: Long) {
                        }

                        override fun onFinish() {
                            // do whatever when the bar is full
                            try {
                                signUpEmailForClick(email, password, fullName)
                            } catch (e: Exception) {
                            }
                        }
                    }.start()
                }
            } else {
                Toast.makeText(
                    activity.applicationContext,
                    p0.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    public fun signInEmailForClick(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                Toast.makeText(
                    activity.applicationContext,
                    "Sign In Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                if (firebaseAuth.currentUser !== null) {
                    getUserData()
                    afterLogin()
                } else {
                    var countDownTimer = object : CountDownTimer(2000, 6) {
                        override fun onTick(millisUntilFinished_: Long) {
                        }

                        override fun onFinish() {
                            // do whatever when the bar is full
                            try {
                                signInEmailForClick(email, password)
                            } catch (e: Exception) {
                            }
                        }
                    }.start()
                }
            } else {
                Toast.makeText(
                    activity.applicationContext,
                    p0.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                if (firebaseAuth.currentUser!==null){
                    fbUser=firebaseAuth.currentUser!!


//                    if (localUser.name!=null&& localUser.name!!.length>0){
//                        getUserData()
//                    }
                    dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid)
                    dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var id = snapshot.value
                            if (id == null) {
                                var localUser = SharedDB.getUser()
                                localUser.name = firebaseAuth!!.currentUser!!.displayName
                                if (localUser.image == null || localUser.image.isNullOrEmpty()) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val b = getBitmapFromUri(firebaseAuth.currentUser!!.photoUrl!!)
                                            withContext(Dispatchers.Main) {
                                                localUser = SharedDB.getUser()
                                                localUser.image = SharedDB.encodeTobase64(b)
                                                SharedDB.saveUSer(localUser)
                                                localUser = SharedDB.getUser()
                                                saveProfileImageInFirebase(localUser.image!!)
                                            }
                                            Log.e("####", localUser.image.toString())
                                        } catch (e: Exception) {
                                            Log.e("#####", e.message.toString())
                                        }

                                    }
//                            localUser.image=  SharedDB.encodeTobase64(getBitmapFromUri(firebaseAuth.currentUser!!.photoUrl!!))
                                }
                                var email: String? = firebaseAuth?.currentUser?.email
                                if (!email.isNullOrEmpty() && email.length > 3) {
                                    if (!localUser.fields!!.contains(
                                            DataField(
                                                "Email",
                                                email,
                                                R.drawable.ic_baseline_attach_email_24
                                            )
                                        )
                                    ) {
                                        localUser.fields!!.add(
                                            DataField(
                                                "Email",
                                                email,
                                                R.drawable.ic_baseline_attach_email_24
                                            )
                                        )
                                    }
                                }
                                var phone: String? = firebaseAuth?.currentUser?.phoneNumber
                                if (!phone.isNullOrEmpty() && phone.length > 4) {
                                    if (!localUser.fields!!.contains(
                                            DataField(
                                                "Email",
                                                phone,
                                                R.drawable.ic_baseline_phone_in_talk_24
                                            )
                                        )
                                    ) {
                                        localUser.fields!!.add(
                                            DataField(
                                                "Email",
                                                phone,
                                                R.drawable.ic_baseline_phone_in_talk_24
                                            )
                                        )
                                    }
                                }
                                localUser.token = FirebaseInstanceId.getInstance().token!!
                                localUser.uId = fbUser.uid
                                try {
                                    localUser.fields =
                                        localUser.fields!!.distinctBy { it.fieldData } as ArrayList<DataField>
                                } catch (e: Exception) {
                                    Log.e("######", e.message.toString())
                                }
                                localUser.public = 1
                                SharedDB.saveUSer(localUser)
                                updateUserData()
                                Log.e("NOTGET","HHHHHH")
                            } else {
                                Log.e("GETDATA","HHHHHH")
                                getUserData()
                            }
                            afterLogin()
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

                }else{
                    var countDownTimer = object : CountDownTimer(2000, 6) {
                        override fun onTick(millisUntilFinished_: Long) {
                        }
                        override fun onFinish() {
                            // do whatever when the bar is full
                            try {
                                signInGoogleForClick()
                            }catch (e: Exception){}
                        }
                    }.start()
                }
            } catch (e: ApiException) {
                Log.e(ContentValues.TAG, "Google sign in failed", e)
            }
        }

    }
    data class UserDataWithoutArr(
        val token: String?,
        val name: String?,
        val password: String?,
        val image: String?,
        val isPublic: Int?
    )
//    save user data and photo should be inside work manager and all inside connectivity manager
    public fun updateUserData(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                /*   fbStore= FirebaseFirestore.getInstance()
                   var documentReference:DocumentReference=fbStore.collection("Users").document(fbUser.uid)
                   var localUser=SharedDB.getUser()
                   documentReference.set("localUser").addOnSuccessListener(object : OnSuccessListener<Void> {
                       override fun onSuccess(p0: Void?) {
                           Toast.makeText(BeCloserApp.gettINSTANCE().applicationContext, "Data Added Successfully", Toast.LENGTH_SHORT).show()
                       }
                   })
                           .addOnFailureListener(object :OnFailureListener{
                               override fun onFailure(p0: java.lang.Exception) {
                                   Toast.makeText(BeCloserApp.gettINSTANCE().applicationContext, p0.toString(), Toast.LENGTH_SHORT).show()
                               }

                           })*/
                fbUser=firebaseAuth.currentUser!!
                var localUser=SharedDB.getUser()
                Log.e("#ffff", localUser.image.toString())
                dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid)
                dbReference.setValue(
                    UserDataWithoutArr(
                        localUser.token,
                        localUser.name,
                        localUser.password,
                        localUser.image,
                        localUser.public
                    )
                )
                try {
                    localUser.fields =localUser.fields!!.distinctBy{it.fieldData} as ArrayList<DataField>
                }catch (e: Exception){Log.e("######", e.message.toString())}
                dbReference.child("Fields").setValue(localUser.fields)
                dbReference.child("Friends").setValue(localUser.friendsTokn)
            }catch (e: Exception){
                Log.e("Error during uploadData", e.message.toString())
            }
        }
    }
    public fun getUserData(){
       CoroutineScope(Dispatchers.IO).launch {
           //      ----------------------------------------------------------  getfields
           try{
               dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid).child("Fields")
               dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       val t: GenericTypeIndicator<ArrayList<DataField>> = object : GenericTypeIndicator<ArrayList<DataField>>() {}
                       val yourStringArray = snapshot.getValue(t)
                       var localUser: UserProfile = SharedDB.getUser()
                       if (yourStringArray!=null){
                           localUser.fields = yourStringArray
                           SharedDB.saveUSer(localUser)
                       }
//                Toast.makeText(getContext(), yourStringArray!![0].getName(), Toast.LENGTH_LONG).show()
                   }

                   override fun onCancelled(firebaseError: DatabaseError) {
                       Log.e("The read failed: ", firebaseError.message)
                   }
               })
           }catch(e:Exception){
               Log.e("#####Firebase",e.message.toString())
           }

           fbUser= firebaseAuth.currentUser!!
//        dbReference.removeEventListener(getToken)
//        FriendlyMessage Friendlymessage = dataSnapshot.getValue(FriendlyMessage.class);
//      ----------------------------------------------------------  getfields
           /*     dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid).child("Fields")
                dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.getValue() != null) {
                                var localUser: UserProfile = SharedDB.getUser()
                                var listObject = snapshot.getValue() as ArrayList<*>
                                for (i in listObject) {
                                    var arr = (i as HashMap<*, *>)
                                    val icon: Int? = arr.get("icon").toString().toInt()
                                    localUser.fields!!.add(
                                        DataField(
                                            arr.get("fieldName").toString(), arr.get(
                                                "fieldData"
                                            ).toString(), icon
                                        )
                                    )
                                }
                                try {
                                    localUser.fields =
                                        localUser.fields!!.distinctBy { it.fieldData } as ArrayList<DataField>
                                } catch (e: Exception) {
                                    Log.e("#####", e.message.toString())
                                }
                                SharedDB.saveUSer(localUser)
                            }
                        } catch (e: Exception) {
                            Log.e("4444444ErrorFields", e.message.toString())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("####FireBaseAuth", error.message)
                    }

                })*/
//        ----------------------------------------------------------get user
           dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid)
           dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   try {
                       var localUser: UserProfile = SharedDB.getUser()
                       if (snapshot.child("public") != null) {
                           if (localUser.public == null) {
                               localUser.public =
                                   snapshot.child("public").getValue().toString().toInt()
                               Log.e("444444", "" + localUser.public)
                           }

                       }
                       if (snapshot.child("name") != null) {
                           if (localUser.name == null && localUser.name == "null") {
                               localUser.name = snapshot.child("name").getValue().toString()
                           }
                       }
                       if (snapshot.child("image") != null) {
                           localUser.image = snapshot.child("image").getValue().toString()
                           Log.e("LocalImageFireBase", localUser.image.toString())
                           SharedDB.saveLocalImageProfile(SharedDB.decodeBase64(localUser.image)!!)
                       }
                       if (snapshot.child("password") != null) {
                           localUser.password = snapshot.child("password").getValue().toString()
                       }
                       localUser.uId = firebaseAuth.currentUser!!.uid
                       SharedDB.saveUSer(localUser)
                   } catch (e: Exception) {
                       Log.e(
                           "444444444ErrorUser",
                           e.message.toString() + " " + SharedDB.getUser().name
                       )
                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   Log.e("####FireBaseAuth", error.message)
               }

           })
//        ---------------------------------------------------------getFriends
           dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid).child("Friends")
           dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   try {
                       val userFriends: ArrayList<String>? = snapshot.getValue() as ArrayList<String>?
                       var localUser: UserProfile = SharedDB.getUser()
                       if (userFriends != null) {
                           for (i in userFriends) {
                               localUser.friendsTokn.add(i)
                           }
                       }
                       SharedDB.saveUSer(localUser)
                   } catch (e: Exception) {
                       Log.e("444444444ErrorFriends", e.message.toString())
                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   Log.e("####FireBaseAuth", error.message)
               }

           })
       }

    }
    public fun getFriendsData(){
        var localUser=SharedDB.getUser()
        for (i in localUser.friendsTokn){
            dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(i)
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        Log.e("Friends", snapshot.getValue().toString())
                        var name = snapshot.child("name").getValue().toString()
                        snapshot.child("image").getValue().toString()
                        var isPublic = snapshot.child("public").getValue().toString() != "false"
                        var listObject: ArrayList<DataField> =
                            snapshot.child("Fields").getValue() as ArrayList<DataField>
                        for (i in listObject) {
                            var arr = (i as HashMap<*, *>)
                            val icon: Int? = arr.get("icon").toString().toInt()
                            DataField(
                                arr.get("fieldName").toString(),
                                arr.get("fieldData").toString(),
                                icon
                            )//Add in Friends List
                        }
//                        save friends in local
//                        you need to cancel observation
                    } catch (e: Exception) {
                        Log.e("444444444ErrorFriends", e.message.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("####FireBaseAuth", error.message)
                }

            })
        }

    }
    public fun observeOnFriendRequest(){
        fbUser= firebaseAuth.currentUser!!
        //        ---------------------------------------------------------getFriends
        dbReference=FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid).child(
            "WaitingList"
        )
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val userWaiting: ArrayList<String>? = snapshot.getValue() as ArrayList<String>?
                    var localUser: UserProfile = SharedDB.getUser()
                    if (userWaiting != null) {
                        for (i in userWaiting) {
                            localUser.waitingListYouWait.add(i)
                        }
                    }
                    SharedDB.saveUSer(localUser)
                } catch (e: Exception) {
                    Log.e("444444444ErrorFriends", e.message.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("####FireBaseAuthWaiting", error.message)
            }

        })
//        dbReference.removeEventListener()  to remove databaseListener
    }
    public fun friendRequest(Token: String){

    }
    public fun savePhoto(){

    }
    public fun getPhoto(){

    }
    public fun signOut(){
        try{
            FirebaseAuth.getInstance().signOut()
            SharedDB.clearAllData()
            Log.e("ssddsdssd", SharedDB.getUser().token.toString())
//            clear SharedPrefrence
            Navigation.findNavController(fragment.requireView()).navigate(R.id.action_from_home_settings_to_LoginContainer)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
//    -------------------------------------Notification
    fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
    try {
//        val response = RetrofitInstance.api.postNotification(notification)
        val response=retrofitInstance.postNotification(notification)
        if(response.isSuccessful) {
            Log.e("####", "Send Succussfully")
//            Log.d(TAG, "Response: ${Gson().toJson(response).toString()}")
        } else {
            Log.e(TAG, response.errorBody().toString())
        }
    } catch (e: Exception) {
        Log.e(TAG, e.toString())
    }
}
    fun notificationOnClick(t: String){
//        getnew friend data to get if it public or ont
        FirebaseService.sharedPref = activity.getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
//            etToken.setText(it.token)
            Log.e("SendNotification", " Token " + it.token)
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        val title = "Soliman"
        val message = "Welcom from other side, Surprise mother fucker"
        val recipientToken =t
        if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
            Log.e("Now I send Notifiaciion", "Done")
            PushNotification(
                NotificationData(title, message, FirebaseInstanceId.getInstance().token!!),
                recipientToken
            ).also {
                try{
                    sendNotification(it)
                }catch (e: Exception){
                    Log.e("PushNotifiation", " Error" + e.message)
                }
            }
        }
    }
//    ---------------------------------------Image
    suspend fun getBitmapFromUri(imageUri: Uri): Bitmap = withContext(Dispatchers.Default) {
    val imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri).build()
    val dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, activity)
    val result = DataSources.waitForFinalResult(dataSource) as CloseableReference<CloseableBitmap>

    val bitmap = result.get().underlyingBitmap

    CloseableReference.closeSafely(result)
    dataSource.close()
    return@withContext bitmap
}
    fun load(url: String):Bitmap{
        URL(url).openStream().use {
            var b:Bitmap=BitmapFactory.decodeStream(it)
            return b
        }
    }
    fun saveProfileImageInFirebase(imageString: String){
        CoroutineScope(Dispatchers.IO).launch {
            var fUser=firebaseAuth.currentUser!!
            dbReference= FirebaseDatabase.getInstance().reference.child("Users").child(fUser.uid)
            dbReference.child("image").setValue(imageString)
        }
    }
    fun getstr():String{
        return "Soliman"
    }
}


//        dbReference.removeEventListener()  to remove databaseListener