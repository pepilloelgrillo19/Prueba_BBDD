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
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
//Imports para anuncios
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

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
    private lateinit var publiHand: PublicidadHandler
    /*
    //Variables privadas para anuncios
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"*/

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      /*
        // Inicialización para anuncios
        MobileAds.initialize(this)

        //Opciones de anuncios

        var adRequest = AdRequest.Builder().build()

      InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
          Log.d(TAG, adError.toString())
          mInterstitialAd = null
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
          Log.d(TAG, "Ad was loaded.")
          mInterstitialAd = interstitialAd
        }
      })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }*/



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

        db = DatabaseHandler(this)

        publiHand = PublicidadHandler(this)
        publiHand.getAd()
        publiHand.loadContAd()
        publiHand.loadAd()

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
                publiHand.loadAd()
                /* if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this@MainActivity)
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }*/
                querFull.text = ""
                val contactList = db.queryProvinciaContacts(querProv2)
                for (contact in contactList) {
                    val id = contact.id
                    val name = contact.name
                    val email = contact.email
                    val provincia = contact.provincia
                    querFull.append("$id $name $email $provincia \n")
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
    }
}




