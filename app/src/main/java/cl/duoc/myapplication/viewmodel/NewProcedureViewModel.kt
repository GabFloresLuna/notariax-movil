package cl.duoc.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.myapplication.data.repository.ProcedureRepository
import cl.duoc.myapplication.data.repository.ProcedureRepositoryImpl
import cl.duoc.myapplication.model.Documento
import cl.duoc.myapplication.model.ProcedureUiState
import cl.duoc.myapplication.model.TipoPersona
import cl.duoc.myapplication.validation.Validators
import kotlinx.coroutines.launch

class NewProcedureViewModel(
    // Inyectamos el repositorio. Si no se pasa uno, usa el impl por defecto.
    private val repository: ProcedureRepository = ProcedureRepositoryImpl()
) : ViewModel() {

    // Aquí vive el estado
    var uiState by mutableStateOf(ProcedureUiState())
        private set

    // Funciones para actualizar los campos
    fun onTipoPersonaChange(tipo: String) {
        if (tipo == uiState.tipoPersona) return

        // Los campos de una persona natural y de una empresa son distintos,
        // por eso se limpia el formulario al cambiar de tipo.
        // La firma y los documentos adjuntos se conservan.
        uiState = ProcedureUiState(
            tipoPersona = tipo,
            tipoFirma = uiState.tipoFirma,
            documentos = uiState.documentos
        )
    }

    fun onRutChange(rut: String) {
        uiState = uiState.copy(rut = rut, rutError = null) // Limpiamos el error al escribir
    }

    fun onNombreChange(nombre: String) {
        uiState = uiState.copy(nombre = nombre, nombreError = null)
    }

    fun onApellidoChange(apellido: String) {
        uiState = uiState.copy(apellido = apellido, apellidoError = null)
    }

    fun onRazonSocialChange(razonSocial: String) {
        uiState = uiState.copy(razonSocial = razonSocial, razonSocialError = null)
    }

    fun onDireccionChange(direccion: String) {
        uiState = uiState.copy(direccion = direccion, direccionError = null)
    }

    fun onRepresentanteChange(representante: String) {
        uiState = uiState.copy(representante = representante, representanteError = null)
    }

    fun onTipoFirmaChange(firma: String) {
        uiState = uiState.copy(tipoFirma = firma)
    }

    // Agrega los documentos elegidos, sin repetir los que ya estaban
    fun onDocumentosSeleccionados(nuevosDocumentos: List<Documento>) {
        val documentosActuales = uiState.documentos
        val documentosAgregados = nuevosDocumentos.filter { nuevo ->
            documentosActuales.none { it.uri == nuevo.uri }
        }

        uiState = uiState.copy(
            documentos = documentosActuales + documentosAgregados,
            documentosError = null
        )
    }

    fun onDocumentoQuitado(documento: Documento) {
        uiState = uiState.copy(documentos = uiState.documentos - documento)
    }

    // Lógica al presionar "Iniciar"
    fun onIniciarClick() {
        val esEmpresa = uiState.tipoPersona == TipoPersona.EMPRESA

        // 1. Validamos solo los campos que corresponden al tipo de persona
        //    (las reglas viven en Validators)
        val estadoConDocumentos = uiState.copy(
            documentosError = Validators.validarDocumentos(uiState.documentos.size)
        )

        val newState = if (esEmpresa) {
            estadoConDocumentos.copy(
                rutError = Validators.validarRut(uiState.rut),
                razonSocialError = Validators.validarRazonSocial(uiState.razonSocial),
                direccionError = Validators.validarDireccion(uiState.direccion),
                representanteError = Validators.validarNombre(uiState.representante)
            )
        } else {
            estadoConDocumentos.copy(
                rutError = Validators.validarRut(uiState.rut),
                nombreError = Validators.validarNombre(uiState.nombre),
                apellidoError = Validators.validarNombre(uiState.apellido)
            )
        }

        val hasError = listOf(
            newState.rutError,
            newState.nombreError,
            newState.apellidoError,
            newState.razonSocialError,
            newState.direccionError,
            newState.representanteError,
            newState.documentosError
        ).any { it != null }

        // Si hay errores, actualizamos la UI y cortamos la ejecución
        if (hasError) {
            uiState = newState
            return
        }

        // 2. Si todo está bien, creamos el trámite a través del repositorio
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val nombreCompleto = if (esEmpresa) {
                uiState.razonSocial
            } else {
                "${uiState.nombre} ${uiState.apellido}"
            }

            val result = repository.createProcedure(
                rut = uiState.rut,
                nombreCompleto = nombreCompleto,
                tipoPersona = uiState.tipoPersona,
                tipoFirma = uiState.tipoFirma,
                direccion = uiState.direccion,
                representante = uiState.representante,
                documentos = uiState.documentos
            )

            if (result.isSuccess) {
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Error al crear el trámite"
                )
            }
        }
    }

    // Limpia el formulario una vez confirmado el trámite, para que no reaparezca el modal
    fun onTramiteConfirmado() {
        uiState = ProcedureUiState()
    }
}