package cl.duoc.myapplication.model

/**
 * Opciones del tipo de persona que ingresa el trámite.
 */
object TipoPersona {
    const val NATURAL = "Persona natural"
    const val EMPRESA = "Empresa"
}

/**
 * Opciones del tipo de firma del trámite.
 */
object TipoFirma {
    const val SIMPLE = "Simple"
    const val AVANZADA = "Avanzada"
}

/**
 * Mecanismos de firma. La firma simple es manuscrita digital;
 * los de la firma avanzada se simulan en este MVP.
 */
object MecanismoFirma {
    const val MANUSCRITA = "Firma manuscrita digital"
    const val E_TOKEN = "E-token"
    const val CERTIFICADO = "Certificado digital"
    const val ADQUIRIR = "Adquirir firma electrónica avanzada"
}

/**
 * Representa el estado de la interfaz de usuario para la pantalla "Ingresar Trámite".
 * Los campos cambian según el tipo de persona:
 * - Persona natural: rut, nombre y apellido.
 * - Empresa: rut (de la empresa), razón social, dirección y representante legal.
 *
 * @param tipoPersona Selección actual entre persona natural y empresa.
 * @param rut Texto ingresado en el campo RUT (de la persona o de la empresa).
 * @param nombre Texto ingresado en el campo Nombre (persona natural).
 * @param apellido Texto ingresado en el campo Apellido (persona natural).
 * @param razonSocial Texto ingresado en el campo Razón social (empresa).
 * @param direccion Texto ingresado en el campo Dirección (empresa).
 * @param representante Nombre del representante legal (empresa).
 * @param tipoFirma Selección actual entre firma simple y avanzada.
 * @param documentos Documentos adjuntos al trámite (por ejemplo, la hoja del trámite).
 *
 * @param rutError Mensaje de error si el RUT es inválido (null si es válido).
 * @param nombreError Mensaje de error si el nombre es inválido.
 * @param apellidoError Mensaje de error si el apellido es inválido.
 * @param razonSocialError Mensaje de error si la razón social es inválida.
 * @param direccionError Mensaje de error si la dirección es inválida.
 * @param representanteError Mensaje de error si el representante es inválido.
 * @param documentosError Mensaje de error si no se adjuntó ningún documento.
 * @param errorMessage Mensaje de error general si falla la creación del trámite.
 *
 * @param isLoading Indica si se está procesando el trámite (para mostrar el loader en el botón).
 * @param isSuccess Indica si el trámite se creó correctamente (para mostrar el modal de éxito).
 * @param listoParaFirmar Indica que el formulario es válido y se puede pasar a la pantalla de firma.
 */
data class ProcedureUiState(
    // --- Datos del formulario ---
    val tipoPersona: String = TipoPersona.NATURAL, // Valor por defecto
    val rut: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val razonSocial: String = "",
    val direccion: String = "",
    val representante: String = "",
    val tipoFirma: String = TipoFirma.SIMPLE, // Valor por defecto
    val documentos: List<Documento> = emptyList(),

    // --- Manejo de errores (Validaciones) ---
    val rutError: String? = null,
    val nombreError: String? = null,
    val apellidoError: String? = null,
    val razonSocialError: String? = null,
    val direccionError: String? = null,
    val representanteError: String? = null,
    val documentosError: String? = null,
    val errorMessage: String? = null,

    // --- Estado de la UI ---
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val listoParaFirmar: Boolean = false
)