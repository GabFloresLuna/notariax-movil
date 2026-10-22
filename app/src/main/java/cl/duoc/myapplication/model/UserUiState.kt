package cl.duoc.myapplication.model

/**
 * Representa el estado de la interfaz de usuario para la pantalla "Creación de usuario".
 * 
 * @param nombre Texto ingresado en el campo Nombre.
 * @param apellidos Texto ingresado en el campo Apellidos.
 * @param rut Texto ingresado en el campo RUT.
 * @param correo Texto ingresado en el campo Correo.
 * @param password Texto ingresado en el campo Contraseña.
 * 
 * @param nombreError Mensaje de error si el nombre es inválido (null si es correcto).
 * @param apellidosError Mensaje de error si el apellido es inválido.
 * @param rutError Mensaje de error si el RUT es inválido.
 * @param correoError Mensaje de error si el correo es inválido.
 * @param passwordError Mensaje de error si la contraseña no cumple los requisitos.
 * 
 * @param isLoading Indica si se está procesando el registro (para mostrar el loader en el botón).
 * @param isSuccess Indica si el registro fue exitoso (para gatillar la navegación al Login).
 */
data class UserUiState(
    // --- Datos del formulario ---
    val nombre: String = "",
    val apellidos: String = "",
    val rut: String = "",
    val correo: String = "",
    val password: String = "",

    // --- Manejo de errores (Validaciones) ---
    val nombreError: String? = null,
    val apellidosError: String? = null,
    val rutError: String? = null,
    val correoError: String? = null,
    val passwordError: String? = null,

    // --- Estado de la UI ---
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false // Se pone en true cuando el usuario se crea correctamente
)