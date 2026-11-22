package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.data.local.UsuarioDao
import cl.duoc.myapplication.data.local.UsuarioEntity
import cl.duoc.myapplication.data.remote.ApiService
import cl.duoc.myapplication.model.DocumentoResponse
import cl.duoc.myapplication.model.ProcedureResponse
import cl.duoc.myapplication.model.ProcedureStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProcedureRepositoryImplTest {

    // API y DAO simulados: no se usa internet ni Room
    private val api = mockk<ApiService>()
    private val usuarioDao = mockk<UsuarioDao>()
    private val repository = ProcedureRepositoryImpl(api, usuarioDao)

    @Test
    fun getProcedures_sinSesion_devuelveError() = runBlocking {
        coEvery { usuarioDao.obtenerLogueado() } returns null

        val resultado = repository.getProcedures()

        assertTrue(resultado.isFailure)
        assertEquals("Tu sesión expiró, inicia sesión nuevamente", resultado.exceptionOrNull()?.message)
    }

    @Test
    fun getProcedures_conSesion_enviaTokenYConvierteLosTramites() = runBlocking {
        coEvery { usuarioDao.obtenerLogueado() } returns
            UsuarioEntity(correo = "a@a.cl", password = "", logueado = true, token = "abc")
        coEvery { api.getProcedures("Bearer abc") } returns listOf(
            ProcedureResponse(
                id = 1, createdAt = null, rut = "11.111.111-1", nombreCompleto = "Juan Pérez",
                tipoPersona = "Persona natural", tipoFirma = "Simple", estado = "APROBADO",
                direccion = null, representante = null, mecanismoFirma = null
            )
        )
        coEvery { api.getDocumentos("Bearer abc", 1) } returns
            listOf(DocumentoResponse(id = 10, nombre = "hoja.pdf", uri = "content://hoja"))

        val resultado = repository.getProcedures()

        val tramites = resultado.getOrNull()!!
        assertEquals(1, tramites.size)
        assertEquals("1", tramites[0].id)
        assertEquals(ProcedureStatus.APROBADO, tramites[0].estado)
        assertEquals("hoja.pdf", tramites[0].documentos[0].nombre)
    }
}
