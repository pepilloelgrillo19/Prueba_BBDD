package com.lpg.a240131basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Remind : AppCompatActivity() {

    private lateinit var usEmailRem: EditText
    private lateinit var progBar3: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remind_layout)

        usEmailRem=findViewById(R.id.userEmailRemind)
        progBar3=findViewById(R.id.progressBar3)

        auth = FirebaseAuth.getInstance()
    }
fun remind(view:View){
    val email=usEmailRem.text.toString()
    if(!TextUtils.isEmpty(email))
        progBar3.visibility = View.VISIBLE
        auth.sendPasswordResetEmail(email).addOnCompleteListener(this) {
            task ->
            if(task.isSuccessful){
                startActivity(Intent(this,Login::class.java))
            } else {
                Toast.makeText(this, "Error al enviar el Correo de recuperación", Toast.LENGTH_SHORT).show()
            }
        }

    }

}