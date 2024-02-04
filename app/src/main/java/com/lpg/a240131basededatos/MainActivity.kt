package com.lpg.a240131basededatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
    private lateinit var partQuerBut: Button
    private lateinit var querID: EditText
    private lateinit var sortQuer: Button
    private lateinit var miprov: EditText
    private lateinit var querProv: Spinner
    private lateinit var querProvBut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        namUser = findViewById(R.id.nombreUser)
        emUser = findViewById(R.id.emailUser)
        saveBut = findViewById(R.id.saveButton)
        querBut = findViewById(R.id.fullQueryButton)
        querFull = findViewById(R.id.querCompleta)
        partQuerBut = findViewById(R.id.recupButton)
        querID = findViewById(R.id.pedirId)
        sortQuer = findViewById(R.id.sortQuerButt)
        miprov = findViewById(R.id.miProvincia)
        querProv = findViewById(R.id.spinnerProv)
        querProvBut = findViewById(R.id.querryProvButton)

        db = DatabaseHandler(this)

        //Con esta función, solo recoge las provincias presentes en la BBDD, de forma única
        //La declaro aquí arriba, para que, en caso de que se le sume un contacto, poder meterla en la activación
        //del botón, y que se actualice
        //Se añade .sorted() para que saque la lista ordenada
        var provArray = db.selecProvUnica().sorted()
        var querProv2:String = ""
        //El adapter es necesario para poder llenar y darle funcionabilidad al menú desplegable
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        querProv.adapter = adapter
        //Activa el evento de selección en el menú desplegable
        querProv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            //Le da los argumentos para poder seleccionar la provincia desde el array
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                querProv2 = provArray[position].toString()
                if (querProv2 != ""){
                    Toast.makeText(this@MainActivity, "La provincia seleccionada es:" + provArray[position], Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Según la documentación consultada, es necesario esta función para definir el spinner

            }
        }

        saveBut.setOnClickListener {
            val name = namUser.text.toString().trim()
            val email = emUser.text.toString().trim()
            val provincia = miprov.text.toString().trim()
            if (name.isNotEmpty() && email.isNotEmpty() && provincia.isNotEmpty()) {
                val id = db.addContact(name, email, provincia)
                if (id == -1L) {
                    //Error al guardar en la base de datos
                    Toast.makeText(applicationContext, "Ha ocurrido un error", Toast.LENGTH_LONG)
                        .show()
                } else {
                    //Se ha guardado el registro
                    namUser.text.clear()
                    emUser.text.clear()
                    miprov.text.clear()
                    Toast.makeText(
                        applicationContext,
                        "Se ha guardado el contacto",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else { //cuando el usuario no ha metido un campo
                //No se ha rellenado correctamente
                Toast.makeText(
                    applicationContext,
                    "Te falta algún campo por rellenar",
                    Toast.LENGTH_LONG
                ).show()
            }

            //Necesito meterlo para que se actualice al introducir un nuevo contacto
            //.sorted permite que la salida sea ordenada alfabéticamente
            var provArray = db.selecProvUnica().sorted()
            var querProv2:String = ""
            //El adapter es necesario para poder llenar y darle funcionabilidad al menú desplegable
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            querProv.adapter = adapter
            //Activa el evento de selección en el menú desplegable
            querProv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                //Le da los argumentos para poder seleccionar la provincia desde el array
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    querProv2 = provArray[position].toString()
                    if (querProv2 != ""){
                        Toast.makeText(this@MainActivity, "La provincia seleccionada es:" + provArray[position], Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Según la documentación consultada, es necesario esta función para definir el spinner

                }
            }
        }

        querBut.setOnClickListener {
            val contactList = db.recorrerBBDD()
            //Manda la consulta al LogCat
            for (contact in contactList) {
                Log.d(
                    "Contacto ",
                    "ID: ${contact.id}, Nombre: ${contact.name}, Email: ${contact.email}, Provincia: ${contact.provincia}"
                )
            }
            querFull.text = ""
            querFull.text = contactList.joinToString()
        }

        partQuerBut.setOnClickListener {
            val querId2 = querID.text.toString().toIntOrNull()
            val contactList = db.recorrerBBDD()
            for (contact in contactList) {
                if (contact.id == querId2) {
                    val id = contact.id
                    val name = contact.name
                    val email = contact.email
                    val provincia = contact.provincia
                    querFull.text = "$id: $name $email $provincia"

                }
            }
        }

        sortQuer.setOnClickListener {
            querFull.text = ""
            val contactList = db.recorrerBBDD()
            for (contact in contactList) {
                val id = contact.id
                val name = contact.name
                val email = contact.email
                val provincia = contact.provincia
                querFull.append("$id $name $email $provincia \n")
            }
        }

        querProvBut.setOnClickListener {
            querFull.text = ""
            val contactList = db.queryProvinciaContacts(querProv2)
            for (contact in contactList) {
                val id = contact.id
                val name = contact.name
                val email = contact.email
                val provincia = contact.provincia
                querFull.append("$id $name $email $provincia \n")
            }
            //para que muestre un mensaje si se deja el campo en blanco
            if (querProv2 == ""){
                Toast.makeText(applicationContext, "Campo de provincia vacio.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}




