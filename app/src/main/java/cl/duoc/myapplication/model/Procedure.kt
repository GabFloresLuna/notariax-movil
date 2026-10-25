package cl.duoc.myapplication.model

/**
 * Representa un trámite notarial real (ya guardado en el sistema).
 */
data class Procedure(
    val id: String,
    val rut: String,
    val nombreCompleto: String, // Persona natural: nombre + apellido. Empresa: razón social
    val tipoPersona: String, // "Persona natural" o "Empresa"
    val tipoFirma: String,   // "Simple" o "Avanzada"
    val estado: ProcedureStatus, // Usamos un Enum para los estados
    val fechaCreacion: String, // Podría ser un LocalDate si usas java.time
    val direccion: String = "", // Solo se completa para empresas
    val representante: String = "", // Solo se completa para empresas
    val documentos: List<Documento> = emptyList() // Documentos adjuntos al trámite
)

/**
 * Enum para manejar los estados posibles de un trámite.
 */
enum class ProcedureStatus {
    PENDIENTE,
    EN_REVISION,
    APROBADO,
    RECHAZADO
}