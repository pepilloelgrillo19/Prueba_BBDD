package com.lpg.a240131basededatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "MyDatabase"
        private const val TABLE_NAME = "Contacts"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PROV = "provincia"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_EMAIL TEXT, $KEY_PROV TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addContact(name:String, email:String, provincia:String):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_EMAIL, email)
        values.put(KEY_PROV, provincia)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return(success)
    }

    fun recorrerBBDD():List<Contact>{
        val contactList = mutableListOf<Contact>()
        val db = this.readableDatabase
        // Para seleccionar todos los registros de la BBDD
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        //Crea el cursor
        val cursor = db.rawQuery(selectQuery,null)
        //Para empezar a usar el cursor
        cursor.use{

             if (it.moveToFirst()){
                 do{
                     //Primero sacamos el valor de id del primer registro
                     val id = it.getInt(it.getColumnIndex(KEY_ID))
                     val name = it.getString(it.getColumnIndex(KEY_NAME))
                     val email = it.getString(it.getColumnIndex(KEY_EMAIL))
                     val provincia = it.getString(it.getColumnIndex(KEY_PROV))
                     //Ahora hay que guardar los valores que hemos recuperado
                     val contact = Contact(id,name,email,provincia)
                     //Y los añadimos en la variable del tipo lista que recogerá todos los valores
                     contactList.add(contact)

                 }while (it.moveToNext())
             }
        }
        return contactList
    }

    fun queryProvinciaContacts(provincia: String):List<Contact>{
        val contactList = mutableListOf<Contact>()
        val db = this.readableDatabase

        val selectQuery = ("SELECT * FROM $TABLE_NAME WHERE $KEY_PROV='$provincia'")
        val cursor = db.rawQuery(selectQuery,null)

        cursor.use{

            if (it.moveToFirst()){
                do{
                    val id = it.getInt(it.getColumnIndex(KEY_ID))
                    val name = it.getString(it.getColumnIndex(KEY_NAME))
                    val email = it.getString(it.getColumnIndex(KEY_EMAIL))
                    val provincia = it.getString(it.getColumnIndex(KEY_PROV))
                    val contact = Contact(id,name,email,provincia)
                    contactList.add(contact)

                }while (it.moveToNext())
            }
        }
        return contactList

    }

    fun selecProvUnica():List<String>{
        val contactList = mutableListOf<String>()
        val db = this.readableDatabase

        val selectQuery = ("SELECT DISTINCT $KEY_PROV FROM $TABLE_NAME")
        val cursor = db.rawQuery(selectQuery,null)

        cursor.use{

            if (it.moveToFirst()){
                do{
                    //val id = it.getInt(it.getColumnIndex(KEY_ID))
                    //val name = it.getString(it.getColumnIndex(KEY_NAME))
                    //val email = it.getString(it.getColumnIndex(KEY_EMAIL))
                    val provincia = it.getString(it.getColumnIndex(KEY_PROV))
                    //val contact = Contact(id,name,email,provincia)
                    contactList.add(provincia)

                }while (it.moveToNext())
            }
        }
        return contactList

    }

}