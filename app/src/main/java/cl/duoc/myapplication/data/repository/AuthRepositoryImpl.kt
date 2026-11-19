package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.data.local.AppDatabase
import cl.duoc.myapplication.data.local.UsuarioEntity
import cl.duoc.myapplication.data.remote.RetrofitInstance
import cl.duoc.myapplication.model.LoginRequest
import cl.duoc.myapplication.model.SignupRequest
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl : AuthRepository {

    // Room ahora solo guarda quién tiene la sesión abierta
    private val usuarioDao = AppDatabase.instancia!!.usuarioDao()

    override suspend fun login(correo: String, password: String): Result<Unit> {
        return try {
            RetrofitInstance.api.login(LoginRequest(email = correo, password = password))
            // Guardamos la sesión abierta (la contraseña no se guarda)
            usuarioDao.insertar(UsuarioEntity(correo = correo, password = "", logueado = true))
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
            RetrofitInstance.api.signup(
                SignupRequest(
                    name = nombre,
                    apellidos = apellidos,
                    rut = rut,
                    email = correo,
                    password = password
                )
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(Exception("Este correo ya está registrado"))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión con el servidor"))
        }
    }

    // Recuperar contraseña sigue siendo una simulación
    override suspend fun recoverPassword(correo: String): Result<Unit> {
        delay(1500)
        return Result.success(Unit)
    }

    override suspend fun getUsuarioLogueado(): String? {
        return usuarioDao.obtenerLogueado()?.correo
    }

    override suspend fun logout() {
        usuarioDao.cerrarSesion()
    }
}
