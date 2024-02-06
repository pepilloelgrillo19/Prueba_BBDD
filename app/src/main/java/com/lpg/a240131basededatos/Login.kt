package com.lpg.a240131basededatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
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
}