package com.example.practica1kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practica1kotlin.databinding.ActivityAgregarAlumnoBinding

// Pantalla para registrar un nuevo alumno
class AgregarAlumnoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarAlumnoBinding
    private lateinit var dbHelper: AlumnosDBHelper

    // Guarda el URI de la imagen que el usuario eligió de la galería
    private var uriImagenSeleccionada: Uri? = null

    // Selector de imágenes: abre la galería y filtra solo archivos de imagen
    private val selectorImagen = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            // Pedimos permiso permanente para poder seguir usando esta imagen después
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            uriImagenSeleccionada = uri
            binding.imagenSeleccionada.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarAlumnoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = AlumnosDBHelper(this)

        // Al presionar el botón abrimos el selector de imágenes de la galería
        binding.botonSeleccionarImagen.setOnClickListener {
            selectorImagen.launch(arrayOf("image/*"))
        }

        // Al presionar guardar, validamos los campos y guardamos el alumno
        binding.botonGuardar.setOnClickListener {
            guardarAlumno()
        }
    }

    // Valida que los campos no estén vacíos y guarda el alumno en SQLite
    private fun guardarAlumno() {
        val nombre = binding.campoNombre.text.toString().trim()
        val cuenta = binding.campoCuenta.text.toString().trim()
        val correo = binding.campoCorreo.text.toString().trim()

        // Verificamos que ningún campo esté vacío
        if (nombre.isEmpty() || cuenta.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, R.string.mensaje_campos_vacios, Toast.LENGTH_SHORT).show()
            return
        }

        // Si el usuario eligió una imagen guardamos su URI como texto, si no guardamos vacío
        val imagenTexto = uriImagenSeleccionada?.toString() ?: ""

        val nuevoAlumno = Alumno(
            id = 0,
            nombre = nombre,
            cuenta = cuenta,
            imagen = imagenTexto,
            correo = correo
        )

        dbHelper.insertarAlumno(nuevoAlumno)
        Toast.makeText(this, R.string.mensaje_alumno_guardado, Toast.LENGTH_SHORT).show()

        // Cerramos esta pantalla y regresamos al listado
        finish()
    }
}
