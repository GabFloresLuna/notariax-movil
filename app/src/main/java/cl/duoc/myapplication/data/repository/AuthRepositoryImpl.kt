package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.data.local.AppDatabase
import cl.duoc.myapplication.data.local.UsuarioDao
import cl.duoc.myapplication.data.local.UsuarioEntity
import cl.duoc.myapplication.data.remote.ApiService
import cl.duoc.myapplication.data.remote.RetrofitInstance
import cl.duoc.myapplication.model.ApiError
import cl.duoc.myapplication.model.LoginRequest
import cl.duoc.myapplication.model.RecoverRequest
import cl.duoc.myapplication.model.SignupRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val api: ApiService = RetrofitInstance.api,
    // Room solo guarda quién tiene la sesión abierta y su token
    private val usuarioDao: UsuarioDao = AppDatabase.instancia!!.usuarioDao()
) : AuthRepository {

    companion object {
        // Xano gratis permite pocas peticiones, así que el token se valida una sola vez por sesión
        private var tokenValidado = false
    }

    override suspend fun login(correo: String, password: String): Result<Unit> {
        return try {
            val respuesta = api.login(LoginRequest(email = correo, password = password))
            // Guardamos la sesión abierta y el token (la contraseña no se guarda)
            usuarioDao.insertar(
                UsuarioEntity(
                    correo = correo,
                    password = "",
                    logueado = true,
                    token = respuesta.authToken
                )
            )
            tokenValidado = true
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión con el servidor"))
        }
    }

    override suspend fun registerUser(
        nombre: String, apellidos: String, rut: String, correo: String, password: String
    ): Result<Unit> {
        return try {
            api.signup(
                SignupRequest(
                    name = nombre,
                    apellidos = apellidos,
                    rut = rut,
                    email = correo.trim(),
                    password = password
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            // Xano responde 403 cuando el correo ya existe; cualquier otro código es otro problema
            if (e.code() == 403) {
                Result.failure(Exception("Este correo ya está registrado"))
            } else {
                Result.failure(Exception("No se pudo crear el usuario: ${mensajeDelServidor(e)}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión con el servidor"))
        }
    }

    // Xano responde siempre lo mismo, exista o no el correo
    override suspend fun recoverPassword(correo: String): Result<Unit> {
        return try {
            api.recover(RecoverRequest(email = correo))
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("No se pudo enviar la solicitud"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión con el servidor"))
        }
    }

    // Devuelve el correo si la sesión sigue válida en Xano (se consulta solo la primera vez); si el token venció (401), cierra la sesión
    override suspend fun getUsuarioLogueado(): String? {
        val usuario = usuarioDao.obtenerLogueado() ?: return null
        if (tokenValidado) return usuario.correo

        return try {
            api.me("Bearer ${usuario.token}")
            tokenValidado = true
            usuario.correo
        } catch (e: HttpException) {
            if (e.code() == 401) {
                usuarioDao.cerrarSesion()
                null
            } else {
                usuario.correo
            }
        } catch (e: IOException) {
            // Sin internet no podemos validar el token, se mantiene la sesión guardada
            usuario.correo
        }
    }

    override suspend fun logout() {
        tokenValidado = false
        usuarioDao.cerrarSesion()
    }

    // Saca el mensaje que Xano envía en el cuerpo del error (por ejemplo "Invalid email format.")
    private fun mensajeDelServidor(e: HttpException): String {
        val cuerpo = e.response()?.errorBody()?.string() ?: return "error ${e.code()}"
        return try {
            Gson().fromJson(cuerpo, ApiError::class.java)?.message ?: "error ${e.code()}"
        } catch (ex: JsonSyntaxException) {
            "error ${e.code()}"
        }
    }
}
