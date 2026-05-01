package com.example.equipocuatro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RetosDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_RETOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DESCRIPCION TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RETOS")
        onCreate(db)
    }

    fun obtenerRetos(): MutableList<Reto> {
        val retos = mutableListOf<Reto>()
        val cursor = readableDatabase.query(
            TABLE_RETOS,
            arrayOf(COLUMN_ID, COLUMN_DESCRIPCION),
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC"
        )

        cursor.use {
            val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
            val descripcionIndex = it.getColumnIndexOrThrow(COLUMN_DESCRIPCION)
            while (it.moveToNext()) {
                retos.add(
                    Reto(
                        id = it.getInt(idIndex),
                        descripcion = it.getString(descripcionIndex)
                    )
                )
            }
        }

        return retos
    }

    fun insertarReto(descripcion: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPCION, descripcion)
        }
        return writableDatabase.insert(TABLE_RETOS, null, values)
    }

    fun actualizarReto(reto: Reto): Int {
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPCION, reto.descripcion)
        }
        return writableDatabase.update(
            TABLE_RETOS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(reto.id.toString())
        )
    }

    fun eliminarReto(id: Int): Int {
        return writableDatabase.delete(
            TABLE_RETOS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    companion object {
        private const val DATABASE_NAME = "retos.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_RETOS = "retos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DESCRIPCION = "descripcion"
    }
}
