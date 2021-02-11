package com.example.beclose.Utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.example.beclose.BeCloserApp
import com.example.beclose.R
import com.example.beclose.model.DataField
import com.example.beclose.model.UserProfile
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.paperdb.Paper
import java.io.ByteArrayOutputStream


class SharedDB {
    companion object{
        fun clearAllData(){
            Paper.clear(BeCloserApp.gettINSTANCE().applicationContext)
        }
        fun saveUSer(user: UserProfile){
            Paper.init(BeCloserApp.gettINSTANCE().applicationContext)
            try {
                 user.fields = user.fields!!.distinctBy{it.fieldData} as ArrayList<DataField>
                user.friendsTokn = user.friendsTokn!!.distinctBy{it} as ArrayList<String>
                user.waitingListYouSent = user.waitingListYouSent!!.distinctBy{it} as ArrayList<String>
            }catch (e: Exception){Log.e("#####", e.message.toString())}
            Paper.book().write("user", user)
        }
        fun getUser():UserProfile{
            Paper.init(BeCloserApp.gettINSTANCE().applicationContext)
            var user: UserProfile = Paper.book().read("user", UserProfile())
            if (user==null){
                user=UserProfile()
            }
            try {
                user.waitingListYouSent = user.waitingListYouSent!!.distinctBy{it} as ArrayList<String>
                user.fields = user.fields!!.distinctBy{it.fieldData} as ArrayList<DataField>
                user.friendsTokn = user.friendsTokn!!.distinctBy{it} as ArrayList<String>
            }catch (e: Exception){Log.e("#####", e.message.toString())}
            return user
        }
        fun saveLocalImageProfile(image: Bitmap){
            Paper.init(BeCloserApp.gettINSTANCE().applicationContext)
            Paper.book().write("profileImage", encodeTobase64(image))
            Log.e("###Save", image.height.toString() + " " + image.width)
        }
        fun getLocalImageProfile():Bitmap{
            Paper.init(BeCloserApp.gettINSTANCE().applicationContext)
//            var w = 5
//            var h = 5
//            var conf = Bitmap.Config.ARGB_8888; // see other conf types
//            var bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
            var str:String=Paper.book().read("profileImage", String())
            if (str!=null&&!str.isEmpty()){
                var image: Bitmap  = decodeBase64(str)!!
                return image
            }
//            Log.e("###", image.height.toString() + " " + image.width)
            return (BeCloserApp.gettINSTANCE().applicationContext!!.getDrawable(R.drawable.me))!!.toBitmap()
        }
        fun encodeTobase64(image: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
            Log.e("Image Log:", imageEncoded)
            return imageEncoded
        }
        fun decodeBase64(input: String?): Bitmap? {
            val decodedByte: ByteArray = Base64.decode(input, 0)
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        }
        fun generateQRCode(QRString: String):Bitmap{
            var multiFormatWriter = MultiFormatWriter()
            var bitmatrix: BitMatrix =multiFormatWriter.encode(
                QRString,
                BarcodeFormat.QR_CODE,
                350,
                350
            )
            var barcode= BarcodeEncoder()
            var bitmap:Bitmap= barcode.createBitmap(bitmatrix)
            return bitmap
        }

    }
}