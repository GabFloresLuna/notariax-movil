package cl.duoc.myapplication.model

/**
 * Estado de la pantalla de firma avanzada. Los mecanismos se simulan en este MVP.
 *
 * @param mecanismo Mecanismo elegido (e-token, certificado digital o adquirir firma).
 * @param isVerificando Indica si la verificación simulada está en curso.
 * @param verificacionExitosa Indica si el mecanismo elegido ya fue verificado.
 * @param errorMessage Mensaje de error si intenta firmar sin verificar.
 */
data class AdvancedSignatureUiState(
    val mecanismo: String = MecanismoFirma.E_TOKEN,
    val isVerificando: Boolean = false,
    val verificacionExitosa: Boolean = false,
    val errorMessage: String? = null
)