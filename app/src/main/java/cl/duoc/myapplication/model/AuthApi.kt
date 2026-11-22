package cl.duoc.myapplication.model

// Cuerpo que se envía a POST /auth/login
data class LoginRequest(
    val email: String,
    val password: String
)

// Cuerpo que se envía a POST /auth/signup
data class SignupRequest(
    val name: String,
    val apellidos: String,
    val rut: String,
    val email: String,
    val password: String
)

// Respuesta de login y signup: Xano devuelve el token de sesión
data class AuthResponse(
    val authToken: String
)

// Cuerpo que se envía a POST /auth/recover
data class RecoverRequest(
    val email: String
)

// Respuesta de POST /auth/recover: un mensaje de confirmación
data class RecoverResponse(
    val mensaje: String?
)

// Respuesta de GET /auth/me: datos del usuario dueño del token
data class MeResponse(
    val email: String?
)

// Cuerpo de error que devuelve Xano (por ejemplo en un 400)
data class ApiError(
    val message: String?
)
