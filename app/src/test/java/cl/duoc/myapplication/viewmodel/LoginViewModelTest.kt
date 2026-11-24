// app/src/test/java/cl/duoc/myapplication/viewmodel/LoginViewModelTest.kt
package cl.duoc.myapplication.viewmodel

import cl.duoc.myapplication.data.repository.AuthRepository
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class LoginViewModelTest {

    // Repositorio simulado sin respuestas: si el ViewModel lo llamara, la prueba fallaría
    private val viewModel = LoginViewModel(mockk<AuthRepository>())

    @Test
    fun onLoginClick_camposVacios_muestraErrores() {
        viewModel.onLoginClick()

        assertEquals("El correo es obligatorio", viewModel.uiState.correoError)
        assertEquals("La contraseña es obligatoria", viewModel.uiState.passwordError)
        assertFalse(viewModel.uiState.isLoading)
    }
}
