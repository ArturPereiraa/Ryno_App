package com.example.ryno

import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroAlunoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta_aluno)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nome = findViewById<EditText>(R.id.NomeAluno)
        val email = findViewById<EditText>(R.id.EmailAluno)
        val telefone = findViewById<EditText>(R.id.TelefoneAluno)
        val senha = findViewById<EditText>(R.id.SenhaAluno)
        val btnCriarAluno = findViewById<Button>(R.id.Btn_CriarAluno)

        btnCriarAluno.setOnClickListener {
            val nomeText = nome.text.toString().trim()
            val emailText = email.text.toString().trim()
            val telefoneText = telefone.text.toString().trim()
            val senhaText = senha.text.toString().trim()

            // Verificar se os campos estão vazios
            if (nomeText.isEmpty() || emailText.isEmpty() || telefoneText.isEmpty() || senhaText.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar formato do email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar senha (mínimo de 6 caracteres)
            if (senhaText.length < 6) {
                Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Criando usuário no Firebase Authentication
// Criando usuário no Firebase Authentication
            auth.createUserWithEmailAndPassword(emailText, senhaText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("CadastroAluno", "Conta criada com sucesso!")
                        val userId = auth.currentUser?.uid
                        Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                        if (userId == null) {
                            Log.e("CadastroAluno", "Erro: userId é nulo!")
                            Toast.makeText(this, "Erro ao obter ID do usuário!", Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }

                        val aluno = hashMapOf(
                            "nome" to nomeText,
                            "email" to emailText,
                            "telefone" to telefoneText,
                            "tipo" to "aluno"
                        )

                        // Salvando no Firestore
                        db.collection("alunos").document(userId).set(aluno)
                            .addOnSuccessListener {

                                // Redireciona após salvar com sucesso
                                val intent = Intent(this, InteressesActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Erro ao salvar dados!", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        val errorMessage = task.exception?.message ?: "Erro desconhecido"
                        Toast.makeText(this, "Erro ao criar conta: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}
