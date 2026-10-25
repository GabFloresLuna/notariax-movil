package cl.duoc.myapplication.validation

/**
 * Reglas de validación centralizadas para los formularios de la app.
 * No depende de la interfaz: cada función devuelve el mensaje de error,
 * o null si el valor es válido.
 */
object Validators {

    private const val CAMPO_OBLIGATORIO = "Campo obligatorio"

    private val soloLetras = Regex("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$")
    private val formatoRut = Regex("^\\d{7,8}[0-9K]$")
    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    private const val LARGO_MINIMO_PASSWORD = 6
    private const val LARGO_MINIMO_RAZON_SOCIAL = 2
    private const val LARGO_MINIMO_DIRECCION = 5

    // Sirve para nombre, apellido y apellidos
    fun validarNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> CAMPO_OBLIGATORIO
            nombre.any { it.isDigit() } -> "No puede contener números"
            !soloLetras.matches(nombre) -> "Solo puede contener letras"
            else -> null
        }
    }

    // Acepta el RUT con o sin puntos y con o sin guión (12.345.678-5, 12345678-5 o 123456785)
    fun validarRut(rut: String): String? {
        if (rut.isBlank()) return CAMPO_OBLIGATORIO

        val rutLimpio = rut.replace(".", "").replace("-", "").replace(" ", "").uppercase()

        if (!formatoRut.matches(rutLimpio)) return "RUT inválido. Ejemplo: 12345678-5"

        val cuerpo = rutLimpio.dropLast(1)
        val digitoIngresado = rutLimpio.last()

        return if (calcularDigitoVerificador(cuerpo) == digitoIngresado) {
            null
        } else {
            "El dígito verificador no corresponde"
        }
    }

    fun validarCorreo(correo: String): String? {
        return when {
            correo.isBlank() -> CAMPO_OBLIGATORIO
            !formatoCorreo.matches(correo) -> "Correo inválido"
            else -> null
        }
    }

    fun validarPassword(password: String): String? {
        return if (password.length < LARGO_MINIMO_PASSWORD) {
            "Mínimo $LARGO_MINIMO_PASSWORD caracteres"
        } else {
            null
        }
    }

    fun validarRazonSocial(razonSocial: String): String? {
        return when {
            razonSocial.isBlank() -> CAMPO_OBLIGATORIO
            razonSocial.trim().length < LARGO_MINIMO_RAZON_SOCIAL -> "Mínimo $LARGO_MINIMO_RAZON_SOCIAL caracteres"
            else -> null
        }
    }

    fun validarDireccion(direccion: String): String? {
        return when {
            direccion.isBlank() -> CAMPO_OBLIGATORIO
            direccion.trim().length < LARGO_MINIMO_DIRECCION -> "Dirección demasiado corta"
            else -> null
        }
    }

    // Recibe la cantidad de documentos adjuntos
    fun validarDocumentos(cantidad: Int): String? {
        return if (cantidad == 0) "Adjunta al menos un documento" else null
    }

    // Algoritmo módulo 11 del RUT chileno
    private fun calcularDigitoVerificador(cuerpo: String): Char {
        var suma = 0
        var multiplicador = 2

        for (digito in cuerpo.reversed()) {
            suma += digito.digitToInt() * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        return when (val resultado = 11 - (suma % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> resultado.digitToChar()
        }
    }
}