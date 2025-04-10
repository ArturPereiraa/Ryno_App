package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EspecialidadesActivity : AppCompatActivity() {

    private lateinit var btnLutas: Button
    private lateinit var btnYoga: Button
    private lateinit var btnNatacao: Button
    private lateinit var btnMusculacao: Button
    private lateinit var btnProsseguir: Button

    private val selectedInterests = mutableSetOf<Button>() // Lista de botões selecionados

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_especialidade)

        // Inicializa Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Botões
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

                val currentUser = auth.currentUser
                val userId = currentUser?.uid

                if (userId != null) {
                    val data = hashMapOf(
                        "especialidades" to selectedNames
                    )

                    db.collection("Profissionais").document(userId)
                        .update(data as Map<String, Any>)
                        .addOnSuccessListener {
                            val intent = Intent(this, LocalidadeActivity::class.java)
                            intent.putStringArrayListExtra("selectedInterests", ArrayList(selectedNames))
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao salvar especialidades", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun toggleSelection(button: Button) {
        if (selectedInterests.contains(button)) {
            selectedInterests.remove(button)
            button.setBackgroundResource(R.drawable.button_background) // Estado original
        } else {
            selectedInterests.add(button)
            button.setBackgroundResource(R.drawable.button_selected) // Estado selecionado
        }

        btnProsseguir.isEnabled = selectedInterests.isNotEmpty()
    }
}
