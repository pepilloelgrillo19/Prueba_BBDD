package com.lpg.a240131basededatos

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    //Declaración de las variables que se inicializarán más tarde (lateinit)
    private lateinit var namUser: EditText
    private lateinit var emUser: EditText
    private lateinit var saveBut: Button
    private lateinit var db: DatabaseHandler
    private lateinit var querBut: Button
    private lateinit var querFull: TextView
    private lateinit var queName: TextView
    private lateinit var querEmail: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        namUser = findViewById(R.id.nombreUser)
        emUser = findViewById(R.id.emailUser)
        saveBut = findViewById(R.id.saveButton)
        querBut = findViewById(R.id.queryButton)
        querFull = findViewById(R.id.querCompleta)
        queName = findViewById(R.id.recupName)

        db = DatabaseHandler(this)

        saveBut.setOnClickListener {
            val name = namUser.text.toString().trim()
            val email = emUser.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()){
                val id = db.addContact(name,email)
                if (id == -1L){
                    //Error al guardar en la base de datos
                    Toast.makeText(applicationContext, "Ha ocurrido un error", Toast.LENGTH_LONG).show()


                }else{
                    //Se ha guardado el registro
                    namUser.text.clear()
                    emUser.text.clear()
                    Toast.makeText(applicationContext, "Se ha guardado el contacto", Toast.LENGTH_LONG).show()

                }

            } else { //cuando el usuario no ha metido un campo
                //No se ha rellenado correctamente
                Toast.makeText(applicationContext, "Te falta algún campo por rellenar", Toast.LENGTH_LONG).show()

            }


        }
        querBut.setOnClickListener {
            val contactList = db.recorrerBBDD()

            //Manda la consulta al LogCat
            for (contact in contactList){
                Log.d("Contacto ", "ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}")

            }

            querFull.text = ""
            querFull.text = contactList.joinToString()


        }

    }

}