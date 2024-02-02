package com.lpg.a240131basededatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var partQuerBut : Button
    private lateinit var querID : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        namUser = findViewById(R.id.nombreUser)
        emUser = findViewById(R.id.emailUser)
        saveBut = findViewById(R.id.saveButton)
        querBut = findViewById(R.id.fullQueryButton)
        querFull = findViewById(R.id.querCompleta)
        queName = findViewById(R.id.recupName)
        querEmail = findViewById(R.id.recupEmail)
        partQuerBut = findViewById(R.id.recupButton)
        querID = findViewById(R.id.pedirId)

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
        partQuerBut.setOnClickListener {
            queName.text = ""
            querEmail.text = ""
            val querId2 = querID.text.toString().toIntOrNull()

            val contactList = db.recorrerBBDD()
            for (contact in contactList){

                    if(contact.id == querId2){
                        queName.text= contact.name
                        querEmail.text = contact.email
                    }

            }





            /* if (querId == null){
                Toast.makeText(applicationContext, "Introduce una ID válida", Toast.LENGTH_LONG).show()
            }else{
                val contactList = db.recorrerBBDD()
                for (contact in contactList){

                    if(contact.id == querId){
                        queName.text= contact.name
                        querEmail.text = contact.email
                    }

                }


            }*/



        }

    }

}