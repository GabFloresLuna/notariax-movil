package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.model.Procedure
import cl.duoc.myapplication.model.ProcedureStatus
import kotlinx.coroutines.delay
import java.util.UUID

class ProcedureRepositoryImpl : ProcedureRepository {

    // Simulamos la base de datos de trámites.
    // La hacemos 'companion object' para que los datos persistan mientras la app esté abierta.
    companion object {
        private val tramitesDb = mutableListOf<Procedure>(
            // Dejamos un trámite de ejemplo para que el historial no esté vacío al inicio
            Procedure(
                id = UUID.randomUUID().toString(),
                rut = "11.111.111-1",
                nombreCompleto = "Juan Pérez",
                tipoPersona = "Persona natural",
                tipoFirma = "Simple",
                estado = ProcedureStatus.APROBADO,
                fechaCreacion = "20/10/2023"
            )
        )
    }

    override suspend fun createProcedure(
        rut: String, nombre: String, apellido: String, tipoPersona: String, tipoFirma: String
    ): Result<Procedure> {
        delay(2000) // Simulamos que el trámite se está procesando en el servidor

        val nuevoTramite = Procedure(
            id = UUID.randomUUID().toString(), // Genera un ID único
            rut = rut,
            nombreCompleto = "$nombre $apellido",
            tipoPersona = tipoPersona,
            tipoFirma = tipoFirma,
            estado = ProcedureStatus.PENDIENTE, // Todo trámite nuevo empieza pendiente
            fechaCreacion = "Hoy" // En una app real usarías LocalDate.now()
        )

        tramitesDb.add(0, nuevoTramite) // Lo agregamos al inicio de la lista
        return Result.success(nuevoTramite)
    }

    override suspend fun getProcedures(): Result<List<Procedure>> {
        delay(1000) // Simulamos tiempo de carga
        return Result.success(tramitesDb.toList()) // Devolvemos una copia de la lista
    }
}