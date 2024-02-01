package com.lpg.a240131basededatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MyDatabase"
        private const val TABLE_NAME = "Contacts"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_EMAIL TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addContact(name:String, email:String):Long{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_EMAIL, email)
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
                     //Ahora hay que guardar los valores que hemos recuperado
                     val contact = Contact(id,name,email)
                     //Y los añadimos en la variable del tipo lista que recogerá todos los valores
                     contactList.add(contact)

                 }while (it.moveToNext())
             }
        }
        return contactList
    }
}