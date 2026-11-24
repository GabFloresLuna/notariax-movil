// app/src/test/java/cl/duoc/myapplication/data/repository/AuthRepositoryImplTest.kt
package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.data.local.UsuarioDao
import cl.duoc.myapplication.data.local.UsuarioEntity
import cl.duoc.myapplication.data.remote.ApiService
import cl.duoc.myapplication.model.AuthResponse
import cl.duoc.myapplication.model.LoginRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryImplTest {

    // API y DAO simulados: no se usa internet ni Room
    private val api = mockk<ApiService>()
    private val usuarioDao = mockk<UsuarioDao>()
    private val repository = AuthRepositoryImpl(api, usuarioDao)

    // Crea un error HTTP como el que lanza Retrofit
    private fun errorHttp(codigo: Int): HttpException {
        val respuesta = Response.error<Any>(codigo, ResponseBody.create(null, "{}"))
        return HttpException(respuesta)
    }

    @Before
    fun reiniciarSesion() = runBlocking {
        // tokenValidado es estático: logout() lo deja en false antes de cada prueba
        coEvery { usuarioDao.cerrarSesion() } returns Unit
        repository.logout()
    }

    @Test
    fun login_correcto_guardaSesionConToken() = runBlocking {
        coEvery { api.login(LoginRequest("a@a.cl", "clave1234")) } returns AuthResponse("tok")
        coEvery { usuarioDao.insertar(any()) } returns Unit

        val resultado = repository.login("a@a.cl", "clave1234")

        assertTrue(resultado.isSuccess)
        coVerify { usuarioDao.insertar(match { it.token == "tok" && it.logueado && it.password == "" }) }
    }

    @Test
    fun login_conCredencialesMalas_devuelveError() = runBlocking {
        coEvery { api.login(any()) } throws errorHttp(403)

        val resultado = repository.login("a@a.cl", "mala")

        assertEquals("Correo o contraseña incorrectos", resultado.exceptionOrNull()?.message)
    }

    @Test
    fun registerUser_correoRepetido_devuelveError403() = runBlocking {
        coEvery { api.signup(any()) } throws errorHttp(403)

        val resultado = repository.registerUser("Ana", "Soto", "12345678-5", "a@a.cl", "clave1234")

        assertEquals("Este correo ya está registrado", resultado.exceptionOrNull()?.message)
    }

    @Test
    fun getUsuarioLogueado_tokenVencido_cierraSesion() = runBlocking {
        coEvery { usuarioDao.obtenerLogueado() } returns
            UsuarioEntity(correo = "a@a.cl", password = "", logueado = true, token = "abc")
        coEvery { api.me(any()) } throws errorHttp(401)

        assertNull(repository.getUsuarioLogueado())
        coVerify(atLeast = 1) { usuarioDao.cerrarSesion() }
    }
}
