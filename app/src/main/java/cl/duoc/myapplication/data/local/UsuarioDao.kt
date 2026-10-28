package cl.duoc.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {

    // REPLACE también sirve para actualizar un usuario existente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE logueado = 1 LIMIT 1")
    suspend fun obtenerLogueado(): UsuarioEntity?

    // Cierra la sesión de todos los usuarios
    @Query("UPDATE usuarios SET logueado = 0")
    suspend fun cerrarSesion()
}
