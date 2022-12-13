package com.example.appejercicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val device_id = intent.getStringExtra("DEVICE_ID")
        val creation_account = intent.getStringExtra("creation_account").toString()
        val textview = findViewById<TextView>(R.id.textView5).apply {
            text = "ID Dispositivo: $device_id"
        }

        val btnGuardarDatos = findViewById<Button>(R.id.btnGuadarDatos)
        btnGuardarDatos.setOnClickListener() {
            val edtNombre = findViewById<EditText>(R.id.edtNombre).text.toString()
            val edtEstatura = findViewById<EditText>(R.id.edtEstatura).text.toString()
            val edtPeso = findViewById<EditText>(R.id.edtPeso).text.toString()
            val edtEdad = findViewById<EditText>(R.id.edtEdad).text.toString()
            if (edtNombre != "" && edtEstatura != "" && edtPeso != "" && edtEdad != "") {
                var imc : Double = edtPeso.toDouble() / ((edtEstatura.toDouble() / 100) * (edtEstatura.toDouble()) / 100)
                imc = String.format("%.3f", imc).toDouble()
                writeUserDataToDB(device_id = device_id.toString(), nombre = edtNombre, estatura = edtEstatura.toInt(), peso = edtPeso.toDouble(), edad = edtEdad.toInt(), imc = imc, creation_account = creation_account)
                Toast.makeText(this@MainActivity2, "Datos almacenados corectamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity3::class.java).apply {
                    putExtra("DEVICE_ID", device_id)
                    putExtra("NOMBRE", edtNombre)
                }
                startActivity(intent)
            }
            else {
                Toast.makeText(this@MainActivity2, "Faltan datos por llenar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}