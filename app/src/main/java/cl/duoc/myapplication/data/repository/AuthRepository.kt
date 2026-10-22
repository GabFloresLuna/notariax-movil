package cl.duoc.myapplication.data.repository

/**
 * Interfaz que define las operaciones de autenticación.
 * Usar una interfaz nos permite cambiar la implementación (ej: pasar de Mock a Firebase)
 * sin tener que cambiar el resto de la aplicación.
 */
interface AuthRepository {
    
    /**
     * Intenta iniciar sesión con las credenciales dadas.
     * @return Result.success(Unit) si es correcto, Result.failure(Exception) si falla.
     */
    suspend fun login(correo: String, password: String): Result<Unit>

    /**
     * Registra un nuevo usuario en el sistema.
     */
    suspend fun registerUser(
        nombre: String,
        apellidos: String,
        rut: String,
        correo: String,
        password: String
    ): Result<Unit>
}