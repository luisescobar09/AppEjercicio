package com.example.appejercicio

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity3 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val device_id = intent.getStringExtra("DEVICE_ID")
        val nombre = intent.getStringExtra("NOMBRE")
        val textview = findViewById<TextView>(R.id.textView3).apply {
            text = "¡Hola $nombre!"
        }
    }
}