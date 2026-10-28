package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.data.local.AppDatabase
import cl.duoc.myapplication.data.local.UsuarioEntity
import kotlinx.coroutines.delay

class AuthRepositoryImpl : AuthRepository {

    private val usuarioDao = AppDatabase.instancia!!.usuarioDao()

    private suspend fun crearUsuariosDePrueba() {
        if (usuarioDao.buscarPorCorreo("admin@notaria.cl") == null) {
            usuarioDao.insertar(UsuarioEntity("admin@notaria.cl", "123456"))
        }
        if (usuarioDao.buscarPorCorreo("usuario@gmail.com") == null) {
            usuarioDao.insertar(UsuarioEntity("usuario@gmail.com", "1234"))
        }
    }

    override suspend fun login(correo: String, password: String): Result<Unit> {
        delay(1500)
        crearUsuariosDePrueba()

        val usuario = usuarioDao.buscarPorCorreo(correo)

        return if (usuario != null && usuario.password == password) {
            usuarioDao.insertar(usuario.copy(logueado = true))
            Result.success(Unit)
        } else {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        }
    }

    override suspend fun registerUser(
        nombre: String, apellidos: String, rut: String, correo: String, password: String
    ): Result<Unit> {
        delay(1500)

        if (usuarioDao.buscarPorCorreo(correo) != null) {
            return Result.failure(Exception("Este correo ya está registrado"))
        }

        usuarioDao.insertar(UsuarioEntity(correo = correo, password = password))
        return Result.success(Unit)
    }

    override suspend fun recoverPassword(correo: String): Result<Unit> {
        delay(1500)
        crearUsuariosDePrueba()

        return if (usuarioDao.buscarPorCorreo(correo) != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("No hemos encontrado una cuenta con este correo"))
        }
    }

    override suspend fun getUsuarioLogueado(): String? {
        return usuarioDao.obtenerLogueado()?.correo
    }

    override suspend fun logout() {
        usuarioDao.cerrarSesion()
    }
}
