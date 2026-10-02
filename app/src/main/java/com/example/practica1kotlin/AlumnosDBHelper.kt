package com.example.practica1kotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Esta clase se encarga de crear y manejar la base de datos SQLite de los alumnos
class AlumnosDBHelper(contexto: Context) :
    SQLiteOpenHelper(contexto, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS) {

    companion object {
        private const val NOMBRE_BASE_DATOS = "alumnos.db"
        private const val VERSION_BASE_DATOS = 1

        private const val TABLA_ALUMNOS = "Alumnos"
        private const val COLUMNA_ID = "id"
        private const val COLUMNA_NOMBRE = "nombre"
        private const val COLUMNA_CUENTA = "cuenta"
        private const val COLUMNA_IMAGEN = "imagen"
        private const val COLUMNA_CORREO = "correo"
    }

    // Se ejecuta la primera vez que se crea la base de datos
    override fun onCreate(db: SQLiteDatabase) {
        // Creamos la tabla Alumnos con sus columnas
        val sentenciaCrearTabla = "CREATE TABLE $TABLA_ALUMNOS (" +
                "$COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMNA_NOMBRE TEXT, " +
                "$COLUMNA_CUENTA TEXT, " +
                "$COLUMNA_IMAGEN TEXT, " +
                "$COLUMNA_CORREO TEXT)"
        db.execSQL(sentenciaCrearTabla)

        // Insertamos 3 alumnos de ejemplo para que la lista no se vea vacía
        insertarAlumnoEjemplo(db, "Juan Pérez García", "2021123456", "juan.perez@ucol.mx")
        insertarAlumnoEjemplo(db, "María López Hernández", "2021654321", "maria.lopez@ucol.mx")
        insertarAlumnoEjemplo(db, "Carlos Ramírez Torres", "2021789012", "carlos.ramirez@ucol.mx")
    }

    // Método privado para insertar los alumnos de ejemplo al crear la base de datos
    private fun insertarAlumnoEjemplo(db: SQLiteDatabase, nombre: String, cuenta: String, correo: String) {
        val valores = ContentValues()
        valores.put(COLUMNA_NOMBRE, nombre)
        valores.put(COLUMNA_CUENTA, cuenta)
        valores.put(COLUMNA_IMAGEN, "")
        valores.put(COLUMNA_CORREO, correo)
        db.insert(TABLA_ALUMNOS, null, valores)
    }

    // Se ejecuta si en el futuro cambia la versión de la base de datos
    override fun onUpgrade(db: SQLiteDatabase, versionVieja: Int, versionNueva: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_ALUMNOS")
        onCreate(db)
    }

    // Inserta un nuevo alumno en la tabla Alumnos usando ContentValues
    fun insertarAlumno(alumno: Alumno) {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put(COLUMNA_NOMBRE, alumno.nombre)
        valores.put(COLUMNA_CUENTA, alumno.cuenta)
        valores.put(COLUMNA_IMAGEN, alumno.imagen)
        valores.put(COLUMNA_CORREO, alumno.correo)
        db.insert(TABLA_ALUMNOS, null, valores)
        db.close()
    }

    // Obtiene la lista completa de alumnos guardados, recorriendo el cursor con un while
    fun obtenerAlumnos(): List<Alumno> {
        val listaAlumnos = mutableListOf<Alumno>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLA_ALUMNOS", null)

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNA_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_NOMBRE))
                val cuenta = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_CUENTA))
                val imagen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_IMAGEN))
                val correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_CORREO))

                val alumno = Alumno(id, nombre, cuenta, imagen, correo)
                listaAlumnos.add(alumno)

                cursor.moveToNext()
            }
        }
        cursor.close()
        db.close()

        return listaAlumnos
    }
}
