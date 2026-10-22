package cl.duoc.myapplication.model

/**
 * Representa el estado de la interfaz de usuario para la pantalla "Ingresar Trámite".
 * 
 * @param tipoPersona Selección actual entre "Persona natural" y "Empresa".
 * @param rut Texto ingresado en el campo RUT.
 * @param nombre Texto ingresado en el campo Nombre.
 * @param apellido Texto ingresado en el campo Apellido.
 * @param tipoFirma Selección actual entre "Simple" y "Avanzada".
 * 
 * @param rutError Mensaje de error si el RUT es inválido (null si es válido).
 * @param nombreError Mensaje de error si el nombre está vacío.
 * @param apellidoError Mensaje de error si el apellido está vacío.
 * 
 * @param isLoading Indica si se está procesando el trámite (para mostrar el loader en el botón).
 */
data class ProcedureUiState(
    // --- Datos del formulario ---
    val tipoPersona: String = "Persona natural", // Valor por defecto
    val rut: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val tipoFirma: String = "Simple", // Valor por defecto

    // --- Manejo de errores (Validaciones) ---
    val rutError: String? = null,
    val nombreError: String? = null,
    val apellidoError: String? = null,

    // --- Estado de la UI ---
    val isLoading: Boolean = false
)