package cl.duoc.myapplication.data.repository

import cl.duoc.myapplication.model.Documento
import cl.duoc.myapplication.model.MecanismoFirma
import cl.duoc.myapplication.model.Procedure
import cl.duoc.myapplication.model.ProcedureStatus
import cl.duoc.myapplication.model.TipoFirma
import cl.duoc.myapplication.model.TipoPersona
import kotlinx.coroutines.delay
import java.util.UUID

class ProcedureRepositoryImpl : ProcedureRepository {

    // Simulamos la base de datos de trámites.
    // La hacemos 'companion object' para que los datos persistan mientras la app esté abierta.
    companion object {
        private val tramitesDb = mutableListOf<Procedure>(
            Procedure(
                id = "demo-1",
                rut = "11.111.111-1",
                nombreCompleto = "Juan Pérez",
                tipoPersona = TipoPersona.NATURAL,
                tipoFirma = TipoFirma.SIMPLE,
                estado = ProcedureStatus.APROBADO,
                fechaCreacion = "20/10/2026",
                documentos = listOf(Documento(uri = "", nombre = "hoja_tramite_juan_perez.pdf")),
                mecanismoFirma = MecanismoFirma.MANUSCRITA
            ),
            Procedure(
                id = "demo-2",
                rut = "12.345.678-5",
                nombreCompleto = "María González",
                tipoPersona = TipoPersona.NATURAL,
                tipoFirma = TipoFirma.AVANZADA,
                estado = ProcedureStatus.APROBADO,
                fechaCreacion = "21/10/2026",
                documentos = listOf(
                    Documento(uri = "", nombre = "poder_simple_maria_gonzalez.pdf"),
                    Documento(uri = "", nombre = "copia_documento_maria_gonzalez.jpg")
                ),
                mecanismoFirma = "${MecanismoFirma.CERTIFICADO} (simulado)"
            ),
            Procedure(
                id = "demo-3",
                rut = "76.123.456-0",
                nombreCompleto = "Comercial Andes SpA",
                tipoPersona = TipoPersona.EMPRESA,
                tipoFirma = TipoFirma.AVANZADA,
                estado = ProcedureStatus.APROBADO,
                fechaCreacion = "22/10/2026",
                direccion = "Av. Providencia 1234, Santiago",
                representante = "Pedro Ramírez",
                documentos = listOf(Documento(uri = "", nombre = "contrato_comercial_andes.pdf")),
                mecanismoFirma = "${MecanismoFirma.E_TOKEN} (simulado)"
            ),
            Procedure(
                id = "demo-4",
                rut = "18.972.631-7",
                nombreCompleto = "Carlos Soto",
                tipoPersona = TipoPersona.NATURAL,
                tipoFirma = TipoFirma.SIMPLE,
                estado = ProcedureStatus.RECHAZADO,
                fechaCreacion = "23/10/2026",
                documentos = listOf(Documento(uri = "", nombre = "declaracion_carlos_soto.pdf")),
                mecanismoFirma = MecanismoFirma.MANUSCRITA
            )
        )
    }

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
        delay(2000) // Simulamos que el trámite se está procesando en el servidor

        val nuevoTramite = Procedure(
            id = UUID.randomUUID().toString(), // Genera un ID único
            rut = rut,
            nombreCompleto = nombreCompleto,
            tipoPersona = tipoPersona,
            tipoFirma = tipoFirma,
            estado = ProcedureStatus.PENDIENTE, // Todo trámite nuevo empieza pendiente
            fechaCreacion = "Hoy", // En una app real usarías LocalDate.now()
            direccion = direccion,
            representante = representante,
            documentos = documentos,
            mecanismoFirma = mecanismoFirma
        )

        tramitesDb.add(0, nuevoTramite) // Lo agregamos al inicio de la lista
        return Result.success(nuevoTramite)
    }

    override suspend fun getProcedures(): Result<List<Procedure>> {
        delay(1000) // Simulamos tiempo de carga
        return Result.success(tramitesDb.toList()) // Devolvemos una copia de la lista
    }
}