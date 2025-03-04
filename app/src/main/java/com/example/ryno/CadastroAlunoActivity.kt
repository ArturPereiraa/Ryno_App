package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CadastroAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta_aluno)

        val btnCriarAluno = findViewById<Button>(R.id.Btn_CriarAluno)  // Encontra o botão pelo ID
        btnCriarAluno.setOnClickListener {
            val intent = Intent(this, InteressesActivity::class.java)  // Abre a nova tela
            startActivity(intent)}


    }
}
