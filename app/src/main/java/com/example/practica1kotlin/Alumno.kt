package com.example.practica1kotlin

// Clase de datos que representa a un alumno guardado en la base de datos
data class Alumno(
    val id: Int,
    val nombre: String,
    val cuenta: String,
    val imagen: String,
    val correo: String
)
