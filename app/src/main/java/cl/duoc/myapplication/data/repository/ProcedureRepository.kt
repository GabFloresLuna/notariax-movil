package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.model.Documento
import cl.duoc.myapplication.model.Procedure

interface ProcedureRepository {

    /**
     * Crea un nuevo trámite y lo guarda en el sistema.
     * Para empresas, nombreCompleto es la razón social y se completan dirección y representante.
     */
    suspend fun createProcedure(
        rut: String,
        nombreCompleto: String,
        tipoPersona: String,
        tipoFirma: String,
        direccion: String = "",
        representante: String = "",
        documentos: List<Documento> = emptyList(),
        mecanismoFirma: String = ""
    ): Result<Procedure>

    /**
     * Obtiene la lista de todos los trámites del usuario (para el historial).
     */
    suspend fun getProcedures(): Result<List<Procedure>>
}