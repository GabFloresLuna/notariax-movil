package cl.duoc.myapplication.model
/**
 * Estado de la interfaz para el Menú Principal.
 */
data class MainMenuUiState(
    val nombreUsuario: String = "", // Correo del usuario logueado
    val estaLogueado: Boolean = false, // Para mostrar "Cerrar sesión" en el perfil
    val isLoading: Boolean = false
)
