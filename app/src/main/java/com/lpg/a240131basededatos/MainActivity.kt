package com.lpg.a240131basededatos

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var namUser: EditText
    private lateinit var emUser: EditText
    private lateinit var saveBut: Button
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        namUser = findViewById(R.id.nombreUser)
        emUser = findViewById(R.id.emailUser)
        saveBut = findViewById(R.id.saveButton)

        db = DatabaseHandler(this)

        saveBut.setOnClickListener {
            val name = namUser.text.toString().trim()
            val email = emUser.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()){
                val id = db.addContact(name,email)
                if (id == -1L){
                    //Error al guardar en la base de datos
                    //TODO Toast
                    Toast.makeText(applicationContext, "Ha ocurrido un error", Toast.LENGTH_LONG).show()


                }else{
                    //TODO Toast para avisar de que se ha guardado el registro
                    namUser.text.clear()
                    emUser.text.clear()
                    Toast.makeText(applicationContext, "Se ha guardado el contacto", Toast.LENGTH_LONG).show()

                }

            } else { //cuando el usuario no ha metido un campo
                //TODO Toast
                Toast.makeText(applicationContext, "Te falta algún campo por rellenar", Toast.LENGTH_LONG).show()

            }


        }

    }

}