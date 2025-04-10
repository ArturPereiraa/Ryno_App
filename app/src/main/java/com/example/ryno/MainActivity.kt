package com.example.ryno

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtSenha: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvCriarConta: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        // Verifica se já está logado
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this, InteressesActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        edtEmail = findViewById(R.id.editTextText2)
        edtSenha = findViewById(R.id.editTextText)
        btnLogin = findViewById(R.id.btnLogin)
        tvCriarConta = findViewById(R.id.Tv_criar_conta)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val senha = edtSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            // Verifica tipo no Firestore
                            db.collection("Profissionais").document(userId).get()
                                .addOnSuccessListener { document ->
                                    sharedPref.edit().putBoolean("isLoggedIn", true).apply()

                                    if (document.exists()) {
                                        val intent = Intent(this, EspecialidadesActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Pode colocar outra verificação para "Alunos" se quiser
                                        val intent = Intent(this, InteressesActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        tvCriarConta.setOnClickListener {
            val intent = Intent(this, CadastreSeActivity::class.java)
            startActivity(intent)
        }
    }
}
