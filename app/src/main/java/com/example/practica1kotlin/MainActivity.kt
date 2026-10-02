package com.example.practica1kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica1kotlin.databinding.ActivityMainBinding

// Pantalla principal: muestra el listado de alumnos registrados
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: AlumnosDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Preparamos la base de datos y el RecyclerView
        dbHelper = AlumnosDBHelper(this)
        binding.listaAlumnos.layoutManager = LinearLayoutManager(this)

        // Al presionar el botón abrimos la pantalla para agregar un alumno
        binding.botonAgregarAlumno.setOnClickListener {
            val intent = Intent(this, AgregarAlumnoActivity::class.java)
            startActivity(intent)
        }
    }

    // Cada vez que volvemos a esta pantalla recargamos la lista de alumnos
    override fun onResume() {
        super.onResume()
        cargarListaAlumnos()
    }

    // Consulta los alumnos guardados en SQLite y los muestra en el RecyclerView
    private fun cargarListaAlumnos() {
        val listaAlumnos = dbHelper.obtenerAlumnos()
        binding.listaAlumnos.adapter = AlumnoAdapter(listaAlumnos)
    }
}
