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
