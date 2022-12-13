package com.example.appejercicio.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import kotlin.random.Random

val database = Firebase.database("https://ejercicio-69ce0-default-rtdb.firebaseio.com/")

data class UserData(val creation_account: String = "")
class UserDataViewModel : ViewModel() {
    fun getData(device_id: String) {
        val value = database.getReference(device_id).child("userdata").child("creation_account").get().addOnSuccessListener {
            Log.e("firebase", "Got value ${it.value}")
            if (it.value == null) {
                val sdf = LocalDateTime.now().toString()
                writeToDB(datetime = sdf, device_id = device_id)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
    fun writeToDB(datetime: String, device_id: String) {
        val userData = UserData(datetime)
        database.getReference().child(device_id).child("userdata").setValue(userData)
    }


}

data class HistFactsRunningData(val exercise: Int = 0, val datetime: String, val distance: Int, val calories_burned : Double, val num_steps : Int, val avg_heart_rate: Double, val time: Int)
data class HistFactsData(val exercise: Int = 0, val datetime: String, val reps: Int, val calories_burned : Double, val avg_heart_rate: Double,  val time: Int)


data class HistHeartRateData(val datetime: String = "", val value: Double = 0.0)
class HistHeartRateClass: ViewModel() {
    private val _Data = mutableStateOf<List<HistHeartRateData>>(emptyList())
    val Data: MutableState<List<HistHeartRateData>> = _Data

    fun getData(device_id: String){
        database.getReference(device_id).child("hist_heart_rate").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _Data.value = snapshot.getValue<List<HistHeartRateData>>()!!
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error getting data", error.toException())
                }
            }
        )
    }

    fun writeToDB(histHeartRatedata : HistHeartRateData, index: Int, device_id: String) {
        var myRef = database.getReference(device_id).child("hist_heart_rate")
        listOf(histHeartRatedata).forEach(){
            myRef.child(index.toString()).setValue(it)
        }
    }
}

fun writeHistHeartRate(
    datetime: String, value: Double, device_id: String
) {
    var _Data = mutableStateOf<List<HistHeartRateData>>(emptyList())
    var Data: MutableState<List<HistHeartRateData>> = _Data
    var histHeartRatedata = HistHeartRateData(datetime = datetime, value = value)

    database.getReference(device_id).child("hist_heart_rate").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _Data.value = snapshot.getValue<List<HistHeartRateData>>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error getting data", error.toException())
            }
        }
    )
    var index = Data.value.size
    var myRef = database.getReference(device_id).child("hist_heart_rate")
    listOf(histHeartRatedata).forEach(){
        myRef.child(index.toString()).setValue(it)
    }
}

data class workoutData(val name: String = "", val reps: Int = 0)
class workoutViewModel : ViewModel() {
    private val _workoutData = mutableStateOf<List<workoutData>>(emptyList())
    val workoutData: State<List<workoutData>> = _workoutData

    fun getData() {
        database.getReference("workout").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _workoutData.value = snapshot.getValue<List<workoutData>>()!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error getting data", error.toException())
                }
            }
        )
    }

    fun writeToDB(Workout: workoutData, index: Int) {
        val myRef = database.getReference("workout")
        listOf(Workout).forEach(){
            myRef.child(index.toString()).setValue(it)
        }
    }
}

