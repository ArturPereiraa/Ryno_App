package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val tvCriarConta = findViewById<TextView>(R.id.Tv_criar_conta)
        tvCriarConta.setOnClickListener{
            val intent = Intent(this,CadastreSeActivity::class.java)
            startActivity(intent)
        }
    }
}
