package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InteressesActivity : AppCompatActivity() {

    private lateinit var btnLutas: Button
    private lateinit var btnYoga: Button
    private lateinit var btnNatacao: Button
    private lateinit var btnMusculacao: Button
    private lateinit var btnProsseguir: Button

    private val selectedInterests = mutableSetOf<Button>() // Lista de botões selecionados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interesses)

        // Atualize os IDs conforme sua refatoração
        btnLutas = findViewById(R.id.btnLutas)
        btnYoga = findViewById(R.id.btnYoga)
        btnNatacao = findViewById(R.id.btnNatacao)
        btnMusculacao = findViewById(R.id.btnMusculacao)
        btnProsseguir = findViewById(R.id.btnProsseguir)

        val buttons = listOf(btnLutas, btnYoga, btnNatacao, btnMusculacao)

        buttons.forEach { button ->
            button.setOnClickListener {
                toggleSelection(button)
            }
        }

        btnProsseguir.isEnabled = false

        btnProsseguir.setOnClickListener {
            if (selectedInterests.isNotEmpty()) {
                val selectedNames = selectedInterests.map { it.text.toString() }
                val intent = Intent(this, ProximaActivity::class.java)
                intent.putStringArrayListExtra("selectedInterests", ArrayList(selectedNames))
                startActivity(intent)
            }
        }
    }

    private fun toggleSelection(button: Button) {
        if (selectedInterests.contains(button)) {
            selectedInterests.remove(button)
            button.setBackgroundResource(R.drawable.button_background) // Voltar ao estado original
        } else {
            selectedInterests.add(button)
            button.setBackgroundResource(R.drawable.button_selected) // Mudar aparência
        }

        btnProsseguir.isEnabled = selectedInterests.isNotEmpty()
    }
}
