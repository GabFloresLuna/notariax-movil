package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.data.local.AppDatabase
import cl.duoc.myapplication.data.local.UsuarioDao
import cl.duoc.myapplication.data.remote.ApiService
import cl.duoc.myapplication.data.remote.RetrofitInstance
import cl.duoc.myapplication.model.Documento
import cl.duoc.myapplication.model.DocumentoRequest
import cl.duoc.myapplication.model.Procedure
import cl.duoc.myapplication.model.ProcedureRequest
import cl.duoc.myapplication.model.ProcedureResponse
import cl.duoc.myapplication.model.ProcedureStatus
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProcedureRepositoryImpl(
    private val api: ApiService = RetrofitInstance.api,
    // Del usuario con sesión abierta sacamos el token para llamar a Xano
    private val usuarioDao: UsuarioDao = AppDatabase.instancia!!.usuarioDao()
) : ProcedureRepository {

    private val mensajeSesion = "Tu sesión expiró, inicia sesión nuevamente"
    private val mensajeSinConexion = "Sin conexión con el servidor"

    override suspend fun createProcedure(
        rut: String,
        nombreCompleto: String,
        tipoPersona: String,
        tipoFirma: String,
        direccion: String,
        representante: String,
        documentos: List<Documento>,
        mecanismoFirma: String
    ): Result<Procedure> {
        val token = obtenerToken() ?: return Result.failure(Exception(mensajeSesion))

        return try {
            // 1. Se crea el trámite (Xano lo asocia al usuario del token)
            val creado = api.createProcedure(
                token,
                ProcedureRequest(
                    rut = rut,
                    nombreCompleto = nombreCompleto,
                    tipoPersona = tipoPersona,
                    tipoFirma = tipoFirma,
                    direccion = direccion,
                    representante = representante,
                    mecanismoFirma = mecanismoFirma
                )
            )

            // 2. Cada documento adjunto se guarda asociado al trámite
            for (documento in documentos) {
                api.createDocumento(
                    token,
                    DocumentoRequest(procedureId = creado.id, nombre = documento.nombre, uri = documento.uri)
                )
            }

            // 3. Se vuelve a pedir el trámite a Xano para devolver lo que quedó guardado
            getProcedure(creado.id.toString())
        } catch (e: HttpException) {
            Result.failure(errorHttp(e))
        } catch (e: IOException) {
            Result.failure(Exception(mensajeSinConexion))
        }
    }

    override suspend fun getProcedures(): Result<List<Procedure>> {
        val token = obtenerToken() ?: return Result.failure(Exception(mensajeSesion))

        return try {
            val tramites = api.getProcedures(token).map { respuesta ->
                // Los documentos de cada trámite se piden aparte
                val documentos = api.getDocumentos(token, respuesta.id).map {
                    Documento(uri = it.uri ?: "", nombre = it.nombre ?: "")
                }
                convertirAProcedure(respuesta, documentos)
            }
            Result.success(tramites)
        } catch (e: HttpException) {
            Result.failure(errorHttp(e))
        } catch (e: IOException) {
            Result.failure(Exception(mensajeSinConexion))
        }
    }

    override suspend fun getProcedure(id: String): Result<Procedure> {
        val token = obtenerToken() ?: return Result.failure(Exception(mensajeSesion))
        val idNumerico = id.toIntOrNull() ?: return Result.failure(Exception("Id de trámite inválido"))

        return try {
            val respuesta = api.getProcedure(token, idNumerico)
            val documentos = api.getDocumentos(token, idNumerico).map {
                Documento(uri = it.uri ?: "", nombre = it.nombre ?: "")
            }
            Result.success(convertirAProcedure(respuesta, documentos))
        } catch (e: HttpException) {
            Result.failure(errorHttp(e))
        } catch (e: IOException) {
            Result.failure(Exception(mensajeSinConexion))
        }
    }

    // Devuelve "Bearer <token>" o null si no hay sesión abierta
    private suspend fun obtenerToken(): String? {
        val token = usuarioDao.obtenerLogueado()?.token
        return if (token.isNullOrEmpty()) null else "Bearer $token"
    }

    // Si Xano responde 401 el token ya no sirve: se cierra la sesión guardada
    private suspend fun errorHttp(e: HttpException): Exception {
        if (e.code() == 401) {
            usuarioDao.cerrarSesion()
            return Exception(mensajeSesion)
        }
        return Exception("Error del servidor (${e.code()})")
    }

    // Pasa el trámite de Xano al modelo que usa la app
    private fun convertirAProcedure(respuesta: ProcedureResponse, documentos: List<Documento>): Procedure {
        val estado = try {
            ProcedureStatus.valueOf(respuesta.estado ?: "")
        } catch (e: IllegalArgumentException) {
            ProcedureStatus.PENDIENTE
        }
        val fecha = respuesta.createdAt?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
        } ?: ""

        return Procedure(
            id = respuesta.id.toString(),
            rut = respuesta.rut ?: "",
            nombreCompleto = respuesta.nombreCompleto ?: "",
            tipoPersona = respuesta.tipoPersona ?: "",
            tipoFirma = respuesta.tipoFirma ?: "",
            estado = estado,
            fechaCreacion = fecha,
            direccion = respuesta.direccion ?: "",
            representante = respuesta.representante ?: "",
            documentos = documentos,
            mecanismoFirma = respuesta.mecanismoFirma ?: ""
        )
    }
}
