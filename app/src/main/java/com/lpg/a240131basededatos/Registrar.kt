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
import com.google.firebase.auth.FirebaseUser
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
    //Es una función pública, que se activa al presionar el botón (desde fuera)
    fun registrar(view: View){
        createNewAccount()
    }
    //Función privada que solo puede llamarse desde el código
    private fun createNewAccount(){
        val name:String = usNameReg.text.toString()
        val lastName:String = usSurnReg.text.toString()
        val email:String = usEmailReg.text.toString()
        val password:String = usPasswReg.text.toString()

        //Comprueba si los campos están vacios o no
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progBar2.visibility = View.VISIBLE

            //Esta función viene de Firebase, y es para realizar el registro
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                task->
                if(task.isComplete){
                    //Variable de clase FireBase para generar la autenficación
                    val user: FirebaseUser?=auth.currentUser
                    verifyEmail(user)
                    val userBD = dbReference.child(user!!.uid)
                    userBD.child("Name").setValue(name)
                    userBD.child("Apellido").setValue(lastName)
                    //Lanzamos la actividad de Login
                    action()
                }
            }
        }
    }
    private fun action(){
        startActivity(Intent(this,Login::class.java))
    }
    private fun verifyEmail(user:FirebaseUser?){

        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task->
                if(task.isComplete) { Toast.makeText(this, "Email enviado",Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "Error al enviar el email",Toast.LENGTH_SHORT).show()
                }
            }
    }
}