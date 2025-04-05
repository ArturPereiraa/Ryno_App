package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CadastreSeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastre_se_como)

        val btnProfessor = findViewById<Button>(R.id.Btn_Professor)  // Encontra o botão pelo ID
        btnProfessor.setOnClickListener {
            val intent = Intent(this, CadastroProfessorActivity::class.java)  // Abre a nova tela
            startActivity(intent)}

        val btnAluno = findViewById<Button>(R.id.Btn_aluno)  // Encontra o botão pelo ID
        btnAluno.setOnClickListener {
            val intent = Intent(this, CadastroAlunoActivity::class.java)  // Abre a nova tela
            startActivity(intent)

        }
    }
}
