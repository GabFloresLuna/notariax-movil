package cl.duoc.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de usuarios guardada en el teléfono
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val correo: String,
    val password: String,
    val logueado: Boolean = false // true = la sesión sigue abierta
)
