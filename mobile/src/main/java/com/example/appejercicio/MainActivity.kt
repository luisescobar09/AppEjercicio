package com.example.appejercicio

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val NOTIFICATION_REQUEST_CODE = 1234
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("DEVICE_ID", 0)
        var editor = sharedPreferences.edit()
        editor.putString("device_id", "1")
        editor.commit()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener {
                    task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Log.e("TOKEN", token)
            }
        )

        var device_id = sharedPreferences.getString("device_id", "")
        Log.e("DEVICE id", device_id.toString())
        if(device_id == "1") {
            setContentView(R.layout.activity_main)
        }
        else {
            setContentView(R.layout.login)
        }

        val btn_device_id = findViewById<Button>(R.id.btn_deviceID)
        // set on-click listener
        btn_device_id.setOnClickListener() {
            val editTextDeviceID = findViewById<EditText>(R.id.editTextDeviceID).text.toString()
            if(editTextDeviceID !="" && editTextDeviceID.length == 10){
                deviceID(editTextDeviceID = editTextDeviceID, sharedPreferences = sharedPreferences)
            }
            else {
                Toast.makeText(this@MainActivity, "Token no valido", Toast.LENGTH_SHORT).show()
            }
        }


        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]


    }
    val database = Firebase.database("https://ejercicio-69ce0-default-rtdb.firebaseio.com/")
     fun deviceID(
        editTextDeviceID : String,
        sharedPreferences: SharedPreferences
    ) {
         var data = ""
         database.getReference(editTextDeviceID).child("userdata").child("creation_account").get().addOnSuccessListener {
             data = it.value.toString()
             Log.e("firebase", data)
             if(data != "null") {
                 var editor = sharedPreferences.edit()
                 editor.putString("device_id", editTextDeviceID)
                 editor.commit()
                 Toast.makeText(this@MainActivity, "Bienvenido", Toast.LENGTH_SHORT).show()
                 val intent = Intent(this, MainActivity2::class.java).apply {
                     putExtra("DEVICE_ID", editTextDeviceID)
                     putExtra("creation_account", data)
                 }
                 startActivity(intent)
             }
             else {
                 Toast.makeText(this@MainActivity, "Verifique el token ingresado", Toast.LENGTH_SHORT).show()
             }
         }.addOnFailureListener{
             Log.e("firebase", "Error getting data", it)
         }
    }

}
/*
class MainActivity : AppCompatActivity() {
    // Declare the launcher at the top of your Activity/Fragment:
    private val serverKey = "AAAAmEf2ISo:APA91bE14luDsulF-k5oDEl__VSF49fFv8ic0aPiq2FGVpzNPzxZAn08ofq6IZdNAH8pXrKkpN6ozymyo42YPUBw_9a-zH1BN0a5GxmJ_jQq1i4hX6HCEmniAAs6RqUdnLbj7P0RPOEQ"
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener {
                    task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Log.e("TOKEN", token)

                /*val message = Message.builder()
                    .putData("score", "850")
                    .putData("time", "2:45")
                    .setToken(token)
                    .build();
                val response = FirebaseMessaging.getInstance().send(message);

                Log.e("Successfully sent message: " , "$response");*/
                /*val build = FCMSend.Builder("<To Device Token>")
                    .setTitle("<Title>")
                    .setBody("<Message>")
                val result = build.send().Result()*/
            }
        )
        setContentView(R.layout.activity_main)

    }
}*/