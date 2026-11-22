package cl.duoc.myapplication.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidatorsTest {

    @Test
    fun validarRut_conRutCorrecto_devuelveNull() {
        assertNull(Validators.validarRut("12.345.678-5"))
        assertNull(Validators.validarRut("123456785"))
    }

    @Test
    fun validarRut_conDigitoVerificadorMalo_devuelveError() {
        assertEquals("El dígito verificador no corresponde", Validators.validarRut("12345678-9"))
    }

    @Test
    fun validarCorreo_validaFormato() {
        assertNull(Validators.validarCorreo("gabriel@duoc.cl"))
        assertEquals("Correo inválido", Validators.validarCorreo("gabriel@duoc"))
        assertEquals("Campo obligatorio", Validators.validarCorreo(""))
    }
}
