package com.example.appejercicio.presentation

import android.annotation.SuppressLint
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.sharp.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import androidx.wear.input.RemoteInputIntentHelper
import com.google.type.DateTime
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random



@Composable
fun LoginScreen(
    navigation: NavController,
    device_id: String
    ){
    Scaffold() {
        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item { Text(text = "Bienvenido", textAlign = TextAlign.Center) }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { Text(text = "Ingresa el siguiente código en la app complementaria", textAlign = TextAlign.Center) }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { Text(text = device_id, textAlign = TextAlign.Center, fontSize = 20.sp,fontWeight = FontWeight.Medium) }
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item { Button(onClick = { navigation.navigate("welcome/$device_id") },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Yellow,
                        contentColor = Color.Blue
                )) {
                    Text(text = "Continuar", fontSize = 14.sp)
                } }
            }
        }
    }
}


@Composable
fun WelcomeScreen(
    navigation: NavController,
    device_id: String
){
    var valueChanged = remember {
        mutableStateOf("")
    }
    val value = database.getReference(device_id).child("userdata").child("nombre").get().addOnSuccessListener {
        if (it.value != null) {
            Log.e("firebase", "Got value ${it.value}")
            valueChanged.value = it.value.toString()
        }
    }.addOnFailureListener{
        Log.e("firebase", "Error getting data", it)
    }
    Scaffold() {

        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item { Text(text = "Hola ${valueChanged.value}", fontSize = 16.sp, textAlign = TextAlign.Center) }
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item { ChipBaseRunning(contentModifier, iconModifier, navigation, device_id) }
                item { ChipBaseSquats(contentModifier, iconModifier, navigation, device_id) }
                item { ChipBaseCrunches(contentModifier, iconModifier, navigation, device_id) }
            }
        }
    }
}

@Composable
fun ChipBaseRunning(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    navigation: NavController,
    device_id: String
) {
    Chip(label = { Text("Correr", textAlign = TextAlign.Center) },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.Red,
            contentColor = Color.White
        ),
        onClick = {
            navigation.navigate("running/$device_id")
        },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "",
                modifier = iconModifier
            )
        },
    )
}

@Composable
fun ChipBaseSquats(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    navigation: NavController,
    device_id: String
) {
    Chip(label = { Text("Sentadillas", textAlign = TextAlign.Center) },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.Cyan,
            contentColor = Color.Black
        ),
        onClick = {
            navigation.navigate("squats/$device_id")
        },
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "",
                modifier = iconModifier
            )
        }
    )
}

@Composable
fun ChipBaseCrunches(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    navigation: NavController,
    device_id: String
) {
    Chip(label = { Text("Abdominales", textAlign = TextAlign.Center) },
        onClick = {
            navigation.navigate("crunches/$device_id")
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.Green,
            contentColor = Color.DarkGray
        ),
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Sharp.Add,
                contentDescription = "",
                modifier = iconModifier
            )
        }
    )
}

@Composable
fun RunningScreen(
    navigation: NavController,
    device_id: String
) {
    val items = listOf("50", "100", "200", "300", "500", "700", "800", "1000", "1200", "1500")
    val state = rememberPickerState(items.size)
    Scaffold() {
        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item { Text(text = "Correr", textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Medium) }
                item { Spacer(modifier = Modifier.height(5.dp)) }
                item { Text(text = "Configura la rutina:", fontSize = 14.sp)}
                item { Spacer(modifier = Modifier.height(5.dp)) }
                item { Text(text = "Distancia en metros a recorrer:", fontSize = 10.sp) }
                item {
                        Picker(state = state, modifier = Modifier.size(75.dp, 50.dp)) {
                            Text(text = items[it], modifier = Modifier.padding(10.dp))
                        }
                }
                item { Spacer(modifier = Modifier.height(5.dp)) }
                item {
                    var position = state.selectedOption
                    val distance = items[position]
                    Button(
                        onClick = { navigation.navigate("running_play/$device_id/$distance") },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Yellow,
                            contentColor = Color.Blue
                        )
                    ) {
                        Text(text = "Iniciar")
                    }
                }
            }
        }
    }
}
// https://ofertasportatilesya.com/cuantas-calorias-queman-100-sentadillas/
// https://www.runningcorrer.com.ar/calculadoras-running/calculadora-de-calorias-que-se-pierden-al-correr/
// https://es.quora.com/Cu%C3%A1ntas-calor%C3%ADas-queman-100-abdominales-Y-100-sentadillas
// https://quemarcalorias.es/deportes/abdominales

@SuppressLint("SuspiciousIndentation")
@Composable
fun RunningPlayScreen(
    navigation: NavController,
    device_id: String,
    distance : String,
    viewModel: HistHeartRateClass
) {
    val ctx = LocalContext.current
    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val mAccelerometer : Sensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_GRAVITY
    )
    var dvalue = remember {
        mutableStateOf("0")
    }
    val SensorListener1 = object: SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {
            p0 ?: return
            if (p0 != null) {
                if (p0.sensor.type == Sensor.TYPE_GRAVITY) {
                    val msg = " " + p0.values[0]
                    dvalue.value = p0.values[0].toString()
                    var a = p0!!.values[0]
                    //Log.e("Value", "${dvalue.value}")
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            println("onAccuracyChanged: Sensor: $p0: accuracy: $p1 ")
        }
    }
    sensorManager.registerListener(
        SensorListener1,
        mAccelerometer,
        SensorManager.SENSOR_DELAY_GAME
    )
    val heartSensor: Sensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_HEART_RATE)
    var heartSensorValue = remember {
        mutableStateOf("0")
    }
    val HeartRateSensorListener = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {
            p0 ?: return
            if (p0 != null) {
                p0.values.firstOrNull()?.let {
                    if (p0.sensor.type == Sensor.TYPE_HEART_RATE) {
                        heartSensorValue.value = p0.values[0].toString()
                        Log.d("value", "Valor Ritmo: ${heartSensorValue.value}")
                    }
                }
            }
        }
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            println("onAccuracyChanged: Sensor: $p0: accuracy: $p1 ")
        }
    }
    sensorManager.registerListener(
        HeartRateSensorListener,
        heartSensor,
        SensorManager.SENSOR_DELAY_NORMAL
    )


    Scaffold() {
        val listState = rememberScalingLazyListState()
        var heartRateList : MutableList<Double> = mutableListOf()
        var distance_calculated = 0.0
            Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item {
                    val modifier = contentModifier
                    val totalTime = 100L
                    val initialValue: Float = 1f
                    var size by remember {
                        mutableStateOf(IntSize.Zero)
                    }
                    // create variable for value
                    var value by remember {
                        mutableStateOf(initialValue)
                    }
                    // create variable for current time
                    var currentTime by remember {
                        mutableStateOf(totalTime)
                    }
                    // create variable for isTimerRunning
                    var isTimerRunning by remember {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
                        if(currentTime >= 0 && isTimerRunning) {
                            delay(100L)
                            currentTime += 100L
                            value = currentTime / totalTime.toFloat()
                        }
                    }

                    Log.e("Time", "${currentTime / 100L}")

                    if ((currentTime/1000L) > 0 && (currentTime/1000L).toInt() % 2 == 0) {
                        Log.e("VLAUE", "${heartSensorValue.value}")
                        if (heartRateList.lastOrNull() != heartSensorValue.value.toDouble() && heartSensorValue.value.toDouble() != 0.0) {
                            heartRateList.add(heartSensorValue.value.toDouble())
                            Log.e("LIST", "${heartRateList}")
                            val datetime = LocalDateTime.now().toString()
                            viewModel.getData(device_id = device_id)
                            val index = viewModel.Data.value.size
                            viewModel.writeToDB(histHeartRatedata = HistHeartRateData(datetime = datetime, value = heartRateList.lastOrNull().toString().toDouble()), index = index, device_id = device_id)
                            //writeHistHeartRate(datetime = datetime, value = heartRateList.lastOrNull().toString().toDouble(), device_id = device_id)
                        }
                    }
                    distance_calculated = (dvalue.value.toDouble() * ((currentTime / 1000L) * (currentTime / 1000L))) / 2

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .onSizeChanged {
                                size = it
                            }
                    )
                    {

                        // add value of the timer
                        Text(
                            text = (currentTime / 1000L).toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        // create button to start or stop the timer
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(7.dp)
                                .height(20.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (currentTime <= 0L) {
                                        currentTime = totalTime
                                        isTimerRunning = true
                                    } else {
                                        isTimerRunning = !isTimerRunning
                                    }
                                },
                                //modifier = Modifier.align(Alignment.BottomCenter),
                                // change button color
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                                        Color.Green
                                    } else {
                                        Color.Red
                                    }
                                )
                            ) {
                                Text(
                                    // change the text of button based on values
                                    text = if (isTimerRunning && currentTime >= 0L) "Detener"
                                    else if (!isTimerRunning && currentTime >= 0L) "Iniciar"
                                    else "Restart",
                                    fontSize = 10.sp,
                                )
                            }
                            Button(
                                onClick = {
                                    if (currentTime >= 0L) {
                                        currentTime = totalTime
                                        isTimerRunning = true
                                    } else {
                                        isTimerRunning = !isTimerRunning
                                    }
                                    heartRateList.clear()
                                },
                                //modifier = Modifier.align(Alignment.BottomCenter),
                                // change button color
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Blue
                                )
                            )
                            {
                                Text(text = "Reiniciar", fontSize = 9.sp,)
                            }
                        }

                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .onSizeChanged {
                                size = it
                            }
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))
                        Text(text = "Distancia a recorrer: $distance metros", fontSize = 10.sp)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .onSizeChanged {
                                size = it
                            }
                    ){
                        Spacer(modifier = Modifier.height(130.dp))
                        Text(text = "Distancia recorrida: ${distance_calculated.toInt()} metros", fontSize = 10.sp)
                    }


                    if (distance_calculated.toInt() >= distance.toInt()) {
                        var valueChanged = remember {
                            mutableStateOf("0")
                        }
                        database.getReference(device_id).child("userdata").child("peso").get().addOnSuccessListener {
                            if (it.value != null) {
                                Log.e("firebase", "Got value ${it.value}")
                                valueChanged.value = it.value.toString()
                            }
                        }.addOnFailureListener{
                            Log.e("firebase", "Error getting data", it)
                        }
                        if (currentTime <= 0L) {
                            currentTime = totalTime
                            isTimerRunning = true
                        } else {
                            isTimerRunning = !isTimerRunning
                        }

                        val datetime = LocalDateTime.now().toString()
                        val distance = distance.toInt()
                        var peso = valueChanged.value.toDouble()
                        var calories_burned = ((1.03 * peso) * (distance.toDouble()/1000)).toInt()
                        var num_steps = (distance * 2) - 7
                        var avg_heart_rate = heartRateList.average()

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .onSizeChanged {
                                    size = it
                                }
                        ){
                            Spacer(modifier = Modifier.height(160.dp))
                            Text(text = "Calorías quemadas: $calories_burned", fontSize = 8.sp)
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .onSizeChanged {
                                    size = it
                                }
                        ){
                            Spacer(modifier = Modifier.height(180.dp))
                            Text(text = "No. pasos: $num_steps", fontSize = 8.sp)
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .onSizeChanged {
                                    size = it
                                }
                        ){
                            Spacer(modifier = Modifier.height(200.dp))
                            Text(text = "Ritmo cardíaco promedio: $avg_heart_rate", fontSize = 8.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SquatsScreen(
    navigation: NavController,
    device_id: String
) {
    Scaffold() {
        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item {
                    val reps = remember { mutableStateOf("") }
                    Log.e("Label", "${reps.value}")
                    val launcher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                            it.data?.let { data ->
                                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                                val ipAddress: CharSequence? = results.getCharSequence("ip_address")
                                reps.value = ipAddress as String
                            }
                        }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colors.background
                            ), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Chip( colors = ChipDefaults.chipColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White),
                            label = { Text("Sentadillas") },
                            onClick = {
                                val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                                val remoteInputs: List<RemoteInput> = listOf(
                                    RemoteInput.Builder("ip_address")
                                        .setLabel("Sentadillas a realizar")
                                        .build()
                                )
                                RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)
                                launcher.launch(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Sentadillas a realizar: ${reps.value}", textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                      if(reps.value != "") {
                                          val a: Int? = try { reps.value.toInt() } catch (e: NumberFormatException) { null }
                                          navigation.navigate("squats_play/$device_id/${a.toString()}")
                                      }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Yellow,
                                contentColor = Color.Blue )
                        ) {
                            Text(text = "Iniciar rutina")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SquatsPlaysScreen(
    navigation: NavController,
    device_id: String,
    reps : String
) {
    Scaffold() {
        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item {
                    Text(text = "$device_id $reps")
                }
            }
        }
    }
}

@Composable
fun CrunchesScreen(
    navigation: NavController,
    device_id: String
) {
    Scaffold() {
        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item {
                    val reps = remember { mutableStateOf("") }
                    Log.e("Label", "${reps.value}")
                    val launcher =
                        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                            it.data?.let { data ->
                                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                                val ipAddress: CharSequence? = results.getCharSequence("ip_address")
                                reps.value = ipAddress as String
                            }
                        }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colors.background
                            ), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Chip( colors = ChipDefaults.chipColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White),
                            label = { Text("Abdominales") },
                            onClick = {
                                val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                                val remoteInputs: List<RemoteInput> = listOf(
                                    RemoteInput.Builder("ip_address")
                                        .setLabel("Abdominales a realizar")
                                        .build()
                                )
                                RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)
                                launcher.launch(intent)
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Abdominales a realizar: ${reps.value}", textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                if(reps.value != "") {
                                    val a: Int? = try { reps.value.toInt() } catch (e: NumberFormatException) { null }
                                    navigation.navigate("crunches_play/$device_id/${a.toString()}")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Yellow,
                                contentColor = Color.Blue )
                        ) {
                            Text(text = "Iniciar rutina")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CrunchesPlayScreen(
    navigation: NavController,
    device_id: String,
    reps: String
) {
    Scaffold() {
        val listState = rememberScalingLazyListState()
        Scaffold(timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        }, vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }, positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
        ) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(
                    align = Alignment.Center
                )
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                state = listState
            ) {
                item {
                    Text(text = "$device_id $reps")
                }
            }
        }
    }
}

@Composable
fun workOutScreen(viewModel: workoutViewModel, sharedPreferences: SharedPreferences) {
    var device_id = sharedPreferences.getString("device_id", "")
    if (device_id == "1") {
        val randomCode = Random.nextLong(1000000000, 9999999999)
        var editor = sharedPreferences.edit()
        editor.putString("device_id", "$randomCode")
        editor.commit()
    }
    device_id = sharedPreferences.getString("device_id", "")
    viewModel.getData()
    val index = viewModel.workoutData.value.size
    ScalingLazyColumn() {
        items(viewModel.workoutData.value) {
                workout ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = workout.name,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
        item {
            Button(onClick = {
                viewModel.writeToDB(workoutData("carrera", 10), index)
            }) {
                Text(text = "Add to Firebase")
            }
        }
        item {
            Text(text = "$device_id")
        }
    }
}