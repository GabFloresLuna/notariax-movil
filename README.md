# NotariaX Móvil

Aplicación Android desarrollada como MVP académico para la asignatura **DSY1105 Desarrollo de Aplicaciones Móviles** de Duoc UC. Responde al caso **NotariaX**: mejorar la experiencia móvil en la gestión y el seguimiento de trámites notariales digitales.

> Proyecto académico. Trabaja solo con datos ficticios y no debe usarse para realizar trámites notariales reales.

## Funcionalidades

- **Splash** con animación de aparición.
- **Inicio de sesión** con sesión persistente: el usuario sigue conectado aunque se cierre la app.
- **Cerrar sesión** desde el ícono de perfil del menú principal.
- **Creación de usuario** y **recuperación de contraseña** con validación por campo.
- **Ingreso de trámite** para persona natural o empresa, con carga de uno o varios documentos.
- **Firma simple** (firma dibujada en pantalla) y **firma avanzada** simulada (e-token o certificado digital).
- **Ver estado** de los trámites en curso.
- **Historial** de trámites finalizados (aprobados o rechazados).

## Tecnologías

| Tecnología | Uso |
| --- | --- |
| Kotlin + Jetpack Compose | Interfaz declarativa |
| Material 3 | Componentes visuales y tema |
| Navigation Compose 2.9.8 | Navegación entre pantallas con `NavHost` |
| Lifecycle ViewModel Compose 2.8.7 | Patrón MVVM |
| Room 2.8.4 + KSP 2.3.6 | Persistencia local (SQLite) de usuarios y sesión |
| Activity Result API | Recurso nativo: selector de archivos del sistema |

## Arquitectura

El proyecto sigue el patrón **MVVM** (Model–View–ViewModel):

```
app/src/main/java/cl/duoc/myapplication/
├── data/
│   ├── local/         # Room: UsuarioEntity, UsuarioDao, AppDatabase
│   └── repository/    # Interfaces e implementaciones de datos
├── model/             # Clases de datos y estados de pantalla (UiState)
├── navigation/        # Routes y grafo de navegación (AppNavigation)
├── ui/
│   ├── components/    # Componentes reutilizables (CustomButton, CustomTextField, AppHeader, ProcedureCard)
│   ├── screen/        # Pantallas
│   └── theme/         # Colores, tipografía y tema
├── validation/        # Reglas de validación centralizadas (Validators)
├── viewmodel/         # Lógica de cada pantalla
└── MainActivity.kt    # Punto de entrada; inicia la base de datos y la navegación
```

## Requisitos

- Android Studio (versión reciente, compatible con AGP 9)
- JDK 21
- Android SDK con API 37
- Dispositivo o emulador con Android 8.0 (API 26) o superior

## Instalación y ejecución

1. Clonar el repositorio:
   ```
   git clone https://github.com/GabFloresLuna/notariax-movil.git
   ```
2. Abrir la carpeta del proyecto en Android Studio.
3. Esperar la sincronización de Gradle (**Sync Now**).
4. Seleccionar un emulador o dispositivo y presionar **Run**.

## Usuarios de prueba

| Correo | Contraseña |
| --- | --- |
| usuario@gmail.com | 1234 |
| admin@notaria.cl | 123456 |

También se puede crear un usuario nuevo desde la pantalla de registro.

## Equipo

| Integrante | Rama |
| --- | --- |
| Gabriel Flores | `gabo` |
| Daniel Rangel | `rangel` |
| Javier Herrera | `javier` |

Docente: Álvaro Maurelia

## Organización

- **Control de versiones:** ramas por integrante, integradas a `main` mediante Pull Requests.
- **Gestión de tareas:** [tablero de Trello](https://trello.com/invite/b/6ab2c63ef0608de7d0555359/ATTIc47aa4f011b04f162c9220c8a5732f3947094E64/notariax-movil).
- **Documentación del caso:** carpeta [`docs/`](docs/).
