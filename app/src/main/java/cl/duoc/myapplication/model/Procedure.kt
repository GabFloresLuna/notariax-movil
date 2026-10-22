package cl.duoc.myapplication.model

/**
 * Representa un trámite notarial real (ya guardado en el sistema).
 */
data class Procedure(
    val id: String,
    val rut: String,
    val nombreCompleto: String, // Combinación de nombre + apellido
    val tipoPersona: String, // "Persona natural" o "Empresa"
    val tipoFirma: String,   // "Simple" o "Avanzada"
    val estado: ProcedureStatus, // Usamos un Enum para los estados
    val fechaCreacion: String // Podría ser un LocalDate si usas java.time
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