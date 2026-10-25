package cl.duoc.myapplication.model

/**
 * Representa un documento adjunto a un trámite (por ejemplo, la hoja del trámite).
 *
 * @param uri Ubicación del archivo elegido en el dispositivo.
 * @param nombre Nombre del archivo, para mostrarlo en la lista.
 */
data class Documento(
    val uri: String,
    val nombre: String
)