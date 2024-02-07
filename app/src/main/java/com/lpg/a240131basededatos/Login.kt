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

class Login : AppCompatActivity() {

    private lateinit var usLogNam: EditText
    private lateinit var usLogPass : EditText
    private lateinit var progBar : ProgressBar

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)

        usLogNam=findViewById(R.id.userLogEmail)
        usLogPass=findViewById(R.id.userLogPass)
        progBar=findViewById(R.id.progressBar)

        auth= FirebaseAuth.getInstance()
    }

    fun remind(view: View) {
        startActivity(Intent(this,Remind::class.java))
    }

    fun registrar(view:View) {
        startActivity(Intent(this,Registrar::class.java))
    }

    fun login(view:View) {
        loginUser()
    }

    private fun loginUser(){
        val user:String = usLogNam.text.toString()
        val password:String = usLogPass.text.toString()
        if( !TextUtils.isEmpty(user)
            && !TextUtils.isEmpty(password) )
        {
            progBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(user,password).addOnCompleteListener(this){
                task->
                if (task.isSuccessful) {
                    action()
                }else{
                    Toast.makeText(this,"Error en la autenticación",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun action(){
        startActivity(Intent(this,MainActivity::class.java))
    }
}