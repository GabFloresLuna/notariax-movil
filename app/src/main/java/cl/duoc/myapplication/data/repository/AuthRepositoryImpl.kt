package cl.duoc.myapplication.data.repository

import kotlinx.coroutines.delay

class AuthRepositoryImpl : AuthRepository {

    // Simulamos una base de datos de usuarios en memoria
    private val usuariosRegistrados = mutableMapOf<String, String>() // Correo a Password

    override suspend fun login(correo: String, password: String): Result<Unit> {
        delay(1500) // Simulamos que tarda 1.5 segundos en responder el servidor

        // Validación simulada
        return if (usuariosRegistrados[correo] == password || (correo == "admin@notaria.cl" && password == "123456")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        }
    }

    override suspend fun registerUser(
        nombre: String, apellidos: String, rut: String, correo: String, password: String
    ): Result<Unit> {
        delay(1500) // Simulamos tiempo de red

        if (usuariosRegistrados.containsKey(correo)) {
            return Result.failure(Exception("Este correo ya está registrado"))
        }

        // "Guardamos" el usuario
        usuariosRegistrados[correo] = password
        return Result.success(Unit)
    }
}