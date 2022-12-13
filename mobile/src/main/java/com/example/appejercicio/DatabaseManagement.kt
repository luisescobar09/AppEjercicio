package com.example.appejercicio

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

val database = Firebase.database("https://ejercicio-69ce0-default-rtdb.firebaseio.com/")

data class UserData(val nombre: String = "", val estatura: Int = 0, val peso: Double = 0.0, val edad: Int = 0, val imc: Double = 0.0, val creation_account: String = "")
    fun writeUserDataToDB(device_id: String, nombre: String, estatura: Int, peso: Double, edad: Int , imc: Double, creation_account: String ) {
        val userData = UserData(nombre, estatura, peso, edad, imc, creation_account)
        database.getReference().child(device_id).child("userdata").setValue(userData)
    }
