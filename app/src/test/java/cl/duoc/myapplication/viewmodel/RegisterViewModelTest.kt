// app/src/test/java/cl/duoc/myapplication/viewmodel/RegisterViewModelTest.kt
package cl.duoc.myapplication.viewmodel

import cl.duoc.myapplication.data.repository.AuthRepository
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class RegisterViewModelTest {

    private val viewModel = RegisterViewModel(mockk<AuthRepository>())

    @Test
    fun onCrearUsuarioClick_rutConDigitoMalo_muestraErrorDeRut() {
        viewModel.onNombreChange("Ana")
        viewModel.onApellidosChange("Soto")
        viewModel.onRutChange("12345678-9")
        viewModel.onCorreoChange("a@a.cl")
        viewModel.onPasswordChange("clave1234")

        viewModel.onCrearUsuarioClick()

        assertEquals("El dígito verificador no corresponde", viewModel.uiState.rutError)
        assertNull(viewModel.uiState.nombreError)
        assertFalse(viewModel.uiState.isLoading)
    }
}
