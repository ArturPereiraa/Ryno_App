package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroProfessorActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta_professor)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nome = findViewById<EditText>(R.id.NomeProfessor)
        val email = findViewById<EditText>(R.id.EmailProfessor)
        val telefone = findViewById<EditText>(R.id.TelefoneProfessor)
        val senha = findViewById<EditText>(R.id.SenhaProfessor)
        val cref = findViewById<EditText>(R.id.CREF)
        val btnCriarProfessor = findViewById<Button>(R.id.Btn_CriarProfessor)

        btnCriarProfessor.setOnClickListener {
            val nomeText = nome.text.toString().trim()
            val emailText = email.text.toString().trim()
            val telefoneText = telefone.text.toString().trim()
            val senhaText = senha.text.toString().trim()
            val crefText = cref.text.toString().trim()

            if (nomeText.isEmpty() || emailText.isEmpty() || telefoneText.isEmpty() || senhaText.isEmpty() || crefText.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (senhaText.length < 6) {
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailText, senhaText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        if (userId == null) {
                            Toast.makeText(this, "Erro ao obter ID do usuário!", Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }

                        val professor = hashMapOf(
                            "nome" to nomeText,
                            "email" to emailText,
                            "telefone" to telefoneText,
                            "cref" to crefText,
                            "tipo" to "professor"
                        )

                        db.collection("Profissionais").document(userId)
                            .set(professor)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, EspecialidadesActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erro ao salvar dados: ${e.message}", Toast.LENGTH_SHORT).show()
                                Log.e("CadastroProfessor", "Erro Firestore: ", e)
                            }

                    } else {
                        val error = task.exception?.message ?: "Erro desconhecido"
                        Toast.makeText(this, "Erro ao criar conta: $error", Toast.LENGTH_SHORT).show()
                        Log.e("CadastroProfessor", "Erro: $error")
                    }
                }
        }
    }
}
