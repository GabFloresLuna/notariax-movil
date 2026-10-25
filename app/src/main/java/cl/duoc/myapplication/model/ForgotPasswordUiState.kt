package cl.duoc.myapplication.model

data class ForgotPasswordUiState(
    val correo: String = "",
    val correoError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val mensaje: String? = null
)