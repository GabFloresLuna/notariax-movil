# Endpoints Xano – NotariaX (Tarea 2)

Base: `https://x8ki-letl-twmt.n7.xano.io/api:m4gqBliW/`
Todos los endpoints con 🔒 llevan `Authorization: Bearer <authToken>` y activan **Authentication → tabla `user`** en el endpoint.

## Tablas (Database → Add Table)

**procedure** (además de `id` y `created_at`, que Xano crea solo):
| Campo | Tipo |
|---|---|
| user_id | Table Reference → `user` |
| rut | text |
| nombre_completo | text |
| tipo_persona | text |
| tipo_firma | text |
| estado | text (valor por defecto `PENDIENTE`) |
| direccion | text (opcional) |
| representante | text (opcional) |
| mecanismo_firma | text (opcional) |

**documento**:
| Campo | Tipo |
|---|---|
| procedure_id | Table Reference → `procedure` |
| nombre | text |
| uri | text |

(Solo se guarda nombre y uri del archivo; no se sube el archivo a Xano.)

## Lista de endpoints

| # | Método | Ruta | Auth |
|---|---|---|---|
| 1 | GET | auth/me | 🔒 |
| 2 | POST | auth/recover | no |
| 3 | GET | procedure | 🔒 |
| 4 | POST | procedure | 🔒 |
| 5 | GET | procedure/{procedure_id} | 🔒 |
| 6 | PATCH | procedure/{procedure_id} | 🔒 |
| 7 | DELETE | procedure/{procedure_id} | 🔒 |
| 8 | POST | documento | 🔒 |
| 9 | GET | documento | 🔒 |
| 10 | DELETE | documento/{documento_id} | 🔒 |

## Pasos (en el API Group existente: API → grupo de auth/login → Add API Endpoint)

Para cada uno: Add API Endpoint → elegir verbo → escribir la ruta → armar el function stack → **Run & Debug** para probar → **Publish**.

1. **GET auth/me**: activar Authentication (tabla `user`). Stack: `Get Record` de `user` con `id = $auth.id` → Response = ese registro. Quitar `password` del response (Response → Remove field o `Add field` seleccionando solo name, email, apellidos, rut, role).
2. **POST auth/recover**: sin Authentication. Input: `email` (email). Stack: `Get Record` en `user` con `email = $input.email`. Response: `{ "mensaje": "Si el correo existe, recibirás instrucciones" }` (igual exista o no). Nota: Xano no envía correos por sí solo; esto solo valida y responde. Enviar correo real exigiría un servicio externo; no lo he verificado.
3. **GET procedure**: Authentication activa. Stack: `Query All Records` de `procedure`, filtro `user_id = $auth.id`, orden `created_at` descendente. Response = lista.
4. **POST procedure**: Authentication activa. Inputs: rut, nombre_completo, tipo_persona, tipo_firma, direccion, representante, mecanismo_firma (text; los 3 últimos opcionales). Stack: `Add Record` en `procedure` con esos campos más `user_id = $auth.id` y `estado = "PENDIENTE"`. Response = registro creado.
5. **GET procedure/{procedure_id}**: Authentication activa. Input `procedure_id` (integer, Path). Stack: `Get Record` en `procedure` con `id = $input.procedure_id` **y** `user_id = $auth.id` (campo "Additional filter"). Si no existe, Xano devuelve 404.
6. **PATCH procedure/{procedure_id}**: igual al 5 para encontrar el registro, luego `Edit Record` (o Patch Record) con los campos que se quieran cambiar (por ejemplo `estado`). Mismo filtro por `user_id`.
7. **DELETE procedure/{procedure_id}**: mismo filtro por dueño; `Delete Record`. Antes, `Query All Records` de `documento` con `procedure_id` y borrarlos (o se quedan huérfanos).
8. **POST documento**: Authentication activa. Inputs: procedure_id (integer), nombre, uri. Stack: primero comprobar que el trámite es del usuario (`Get Record` en `procedure` con id y `user_id = $auth.id`), luego `Add Record` en `documento`.
9. **GET documento**: Input `procedure_id` (query). Verificar dueño igual que en 8, luego `Query All Records` de `documento` con `procedure_id`.
10. **DELETE documento/{documento_id}**: verificar que el trámite del documento es del usuario, luego `Delete Record`.

## Cómo probar en Xano (sin app)
1. En el endpoint → **Run & Debug**. Para los 🔒 pegar el token en el campo Authorization (`Bearer ...`) o usar el que devuelve `POST auth/login`.
2. Orden sugerido: login → auth/me → POST procedure → GET procedure → GET procedure/{id} → PATCH → DELETE.
3. Probar que con el token de OTRO usuario, GET procedure/{id} del primero devuelve 404 (aislamiento por usuario).

---

# Estado final (lo que quedó hecho)

## Xano (publicado)
- Tablas creadas: `procedure` y `documento`.
- Los 10 endpoints de la lista están creados y publicados en el grupo `api:m4gqBliW`. Se verificó sin token: `auth/recover` responde 200 y los demás responden 401.
- `auth/login`, `auth/signup` y la tabla `user` no se modificaron.

## App (consume 7 endpoints)
| # | Método | Ruta | Dónde se usa |
|---|---|---|---|
| 1 | GET | auth/me | `AuthRepositoryImpl.getUsuarioLogueado` (valida el token; si da 401 cierra la sesión) |
| 2 | POST | auth/recover | `AuthRepositoryImpl.recoverPassword` |
| 3 | GET | procedure | `ProcedureRepositoryImpl.getProcedures` (pantallas Estado e Historial) |
| 4 | POST | procedure | `ProcedureRepositoryImpl.createProcedure` |
| 5 | GET | procedure/{id} | `ProcedureRepositoryImpl.getProcedure`, llamado al final de `createProcedure` para devolver el trámite guardado |
| 6 | POST | documento | `ProcedureRepositoryImpl.createProcedure` (un documento por llamada) |
| 7 | GET | documento | `ProcedureRepositoryImpl.getProcedures` y `getProcedure` |

Los endpoints PATCH y DELETE de `procedure`, y DELETE de `documento`, existen en Xano pero la app aún no los usa.

## Token y sesión
- `authToken` se guarda en Room (columna `token` de `UsuarioEntity`, base de datos versión 2).
- Se envía en cada llamada protegida como `Authorization: Bearer <authToken>`.
- Errores manejados: HTTP, sin internet y 401 (cierra la sesión guardada).

## Pruebas unitarias
- `ValidatorsTest` y `ProcedureRepositoryImplTest` (JUnit + MockK 1.14.12).
- Ejecutar: `./gradlew testDebugUnitTest`.
