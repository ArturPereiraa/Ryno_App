package com.example.ryno

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProximaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proxima)

        val textView = findViewById<TextView>(R.id.textViewMensagem)
        val selectedInterests = intent.getStringArrayListExtra("selectedInterests") ?: listOf()

        textView.text = "Interesses Selecionados:\n" + selectedInterests.joinToString("\n")
    }
}
