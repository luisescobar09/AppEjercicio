/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.appejercicio.presentation

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.compose.material.*
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.appejercicio.R
import com.example.appejercicio.presentation.theme.AppEjercicioTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlin.random.Random
import kotlin.random.nextUInt



@Suppress("DEPRECATION")
class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {
    private val STEP_SENSOR_LISTENER = 20
    private lateinit var mSensorManager: SensorManager
    var mAccelerometer: Sensor? = null
    private var resume = false
    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun onCreate(savedInstanceState: Bundle?) {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("DEVICE_ID", 0)
        setupPermissions()
        ambientController = AmbientModeSupport.attach(this)
        setContent {
            WearApp(sharedPreferences, UserDataViewModel())
        }
    }



    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            //this, Manifest.permission.ACTIVITY_RECOGNITION
            this, Manifest.permission.ACTIVITY_RECOGNITION
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("permisos", "Permiso denegado...")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            //this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), STEP_SENSOR_LISTENER
            this,
            arrayOf(Manifest.permission.BODY_SENSORS, Manifest.permission.ACTIVITY_RECOGNITION),
            STEP_SENSOR_LISTENER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STEP_SENSOR_LISTENER -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("denied", "NO hay permisos...")
                } else {
                    Log.i("granted", "Permisos OK...")
                }
            }
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()


}

class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {
    override fun onEnterAmbient(ambientDetails: Bundle?) {
        super.onEnterAmbient(ambientDetails)
    }

    override fun onExitAmbient() {
        super.onExitAmbient()
    }

    override fun onUpdateAmbient() {
        super.onUpdateAmbient()
    }
}

private fun SensorManager.registerListener(mainActivity: MainActivity, mAccelerometer: Sensor?, sensorDelayNormal: Int) {

}

object NavRoute {
    const val LOGIN_SCREEN = "login"
    const val WELCOME_SCREEN = "welcome/{device_id}"
    const val RUNNING_SCREEN = "running/{device_id}"
    const val RUNNING_SCREEN_PLAY = "running_play/{device_id}/{distance}"
    const val SQUATS_SCREEN = "squats/{device_id}"
    const val SQUATS_SCREEN_PLAY = "squats_play/{device_id}/{reps}"
    const val CRUNCHES_SCREEN = "crunches/{device_id}"
    const val CRUNCHES_SCREEN_PLAY = "crunches_play/{device_id}/{reps}"
}


@Composable
fun WearApp(
    sharedPreferences: SharedPreferences,
    viewModel: UserDataViewModel
) {
    var editor = sharedPreferences.edit()
    editor.putString("device_id", "1")
    editor.commit()
    AppEjercicioTheme {
        val navController = rememberSwipeDismissableNavController()
        val destination : String
        var device_id = sharedPreferences.getString("device_id", "")
        Log.e("DEVICE ID", "$device_id")
        if (device_id == "1") {
            val randomCode = Random.nextLong(1000000000, 9999999999)
            Log.e("CODE", "$randomCode")
            var editor = sharedPreferences.edit()
            editor.putString("device_id", "$randomCode")
            editor.commit()
            device_id = sharedPreferences.getString("device_id", "")
            destination = "login"
            if (device_id != null) {
                viewModel.getData(device_id = device_id)
            }
        }
        else {
            destination = "welcome"
        }

        SwipeDismissableNavHost(navController = navController, startDestination = destination ) {
            composable(NavRoute.LOGIN_SCREEN) {
                if (device_id != null) {
                    LoginScreen(navigation = navController, device_id = device_id)
                }
            }

            composable(NavRoute.WELCOME_SCREEN) { navBackStackEntry ->
                WelcomeScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0")
            }

            composable(NavRoute.RUNNING_SCREEN) { navBackStackEntry ->
                RunningScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0")
            }
            composable(NavRoute.RUNNING_SCREEN_PLAY) { navBackStackEntry ->
                RunningPlayScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0", distance = navBackStackEntry.arguments?.getString("distance") ?: "0", viewModel = HistHeartRateClass())
            }

            composable(NavRoute.SQUATS_SCREEN) { navBackStackEntry ->
                SquatsScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0")
            }
            composable(NavRoute.SQUATS_SCREEN_PLAY) { navBackStackEntry ->
                SquatsPlaysScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0", reps = navBackStackEntry.arguments?.getString("reps") ?: "0")
            }

            composable(NavRoute.CRUNCHES_SCREEN) { navBackStackEntry ->
                CrunchesScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0")
            }
            composable(NavRoute.CRUNCHES_SCREEN_PLAY) { navBackStackEntry ->
                CrunchesPlayScreen(navigation = navController, device_id = navBackStackEntry.arguments?.getString("device_id") ?: "0", reps = navBackStackEntry.arguments?.getString("reps") ?: "0")
            }
        }
    }
}