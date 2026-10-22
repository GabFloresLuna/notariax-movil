package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.model.Procedure

interface ProcedureRepository {
    
    /**
     * Crea un nuevo trámite y lo guarda en el sistema.
     */
    suspend fun createProcedure(
        rut: String,
        nombre: String,
        apellido: String,
        tipoPersona: String,
        tipoFirma: String
    ): Result<Procedure>

    /**
     * Obtiene la lista de todos los trámites del usuario (para el historial).
     */
    suspend fun getProcedures(): Result<List<Procedure>>
}