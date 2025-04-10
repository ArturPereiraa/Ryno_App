package com.example.ryno

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.json.JSONObject

class LocalidadeActivity : AppCompatActivity() {

    private lateinit var autoCompleteCidade: AutoCompleteTextView
    private lateinit var btnProsseguir: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var cidades: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localidade)

        autoCompleteCidade = findViewById(R.id.autoCompleteCidade)
        btnProsseguir = findViewById(R.id.btnProssequir)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        btnProsseguir.visibility = View.GONE

        cidades = carregarCidadesDoAssets()

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cidades)
        autoCompleteCidade.setAdapter(adapter)

        autoCompleteCidade.setOnItemClickListener { _, _, _, _ ->
            btnProsseguir.visibility = View.VISIBLE
        }

        btnProsseguir.setOnClickListener {
            val cidadeSelecionada = autoCompleteCidade.text.toString()

            val userId = auth.currentUser?.uid
            if (userId != null) {
                val userRef = firestore.collection("Profissionais").document(userId)
                val data = hashMapOf("cidade" to cidadeSelecionada)

                userRef.set(data, SetOptions.merge()) // 🔄 Substituição do update

                    .addOnSuccessListener {
                        Toast.makeText(this, "Cidade salva com sucesso!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, PerfilProfissionalActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao salvar cidade: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun carregarCidadesDoAssets(): List<String> {
        val inputStream = assets.open("cidades.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val cidadeList = mutableListOf<String>()
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("cidades")

        for (i in 0 until jsonArray.length()) {
            val cidade = jsonArray.getJSONObject(i).getString("nome")
            cidadeList.add(cidade)
        }

        return cidadeList
    }
}
