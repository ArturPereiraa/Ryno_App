package com.example.ryno


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilProfissionalActivity: AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etTelefone: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSalvar: Button


    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var nomeOriginal = ""
    private var telefoneOriginal = ""
    private var emailOriginal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_profissional)


        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etNome = findViewById(R.id.editNome)
        etTelefone = findViewById(R.id.editTelefone)
        etEmail = findViewById(R.id.editEmail)
        btnSalvar = findViewById(R.id.btnSalvar)

        btnSalvar.visibility = View.GONE

        val userId = auth.currentUser?.uid ?: return

        val docRef = firestore.collection("profissionais").document(userId)
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                nomeOriginal = document.getString("nome") ?: ""
                telefoneOriginal = document.getString("telefone") ?: ""
                emailOriginal = document.getString("email") ?: ""

                etNome.setText(nomeOriginal)
                etTelefone.setText(telefoneOriginal)
                etEmail.setText(emailOriginal)
            }
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nomeAlterado = etNome.text.toString() != nomeOriginal
                val telefoneAlterado = etTelefone.text.toString() != telefoneOriginal
                val emailAlterado = etEmail.text.toString() != emailOriginal
                btnSalvar.visibility = if (nomeAlterado || telefoneAlterado || emailAlterado) View.VISIBLE else View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etNome.addTextChangedListener(watcher)
        etTelefone.addTextChangedListener(watcher)
        etEmail.addTextChangedListener(watcher)

        btnSalvar.setOnClickListener {
            val updates = mapOf(
                "nome" to etNome.text.toString(),
                "telefone" to etTelefone.text.toString(),
                "email" to etEmail.text.toString()
            )
            firestore.collection("profissionais").document(userId).update(updates)
        }
    }
}
