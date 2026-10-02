package com.example.practica1kotlin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1kotlin.databinding.ItemAlumnoBinding

// Adapter que conecta la lista de alumnos con el RecyclerView del listado
class AlumnoAdapter(private val listaAlumnos: List<Alumno>) :
    RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {

    // ViewHolder que guarda las referencias a las vistas de cada tarjeta
    class AlumnoViewHolder(val binding: ItemAlumnoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val inflador = LayoutInflater.from(parent.context)
        val binding = ItemAlumnoBinding.inflate(inflador, parent, false)
        return AlumnoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = listaAlumnos[position]

        holder.binding.textoNombre.text = alumno.nombre
        holder.binding.textoCuenta.text = alumno.cuenta
        holder.binding.textoCorreo.text = alumno.correo

        // Si el alumno tiene una imagen guardada la mostramos, si no usamos el ícono por defecto
        if (alumno.imagen.isNotEmpty()) {
            try {
                holder.binding.imagenAlumno.setImageURI(Uri.parse(alumno.imagen))
            } catch (error: Exception) {
                holder.binding.imagenAlumno.setImageResource(R.drawable.ic_persona_default)
            }
        } else {
            holder.binding.imagenAlumno.setImageResource(R.drawable.ic_persona_default)
        }
    }

    override fun getItemCount(): Int {
        return listaAlumnos.size
    }
}
