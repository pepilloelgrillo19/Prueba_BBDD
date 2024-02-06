package com.lpg.a240131basededatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registrar : AppCompatActivity() {

    private lateinit var usNameReg : EditText
    private lateinit var usSurnReg : EditText
    private lateinit var usEmailReg : EditText
    private lateinit var usPasswReg : EditText
    private lateinit var progBar2 : ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_layout)
        usNameReg=findViewById(R.id.userNameReg)
        usSurnReg=findViewById(R.id.userSurnameReg)
        usEmailReg=findViewById(R.id.userEmailReg)
        usPasswReg=findViewById(R.id.userPassReg)
        progBar2=findViewById(R.id.progressBar2)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")

    }
}