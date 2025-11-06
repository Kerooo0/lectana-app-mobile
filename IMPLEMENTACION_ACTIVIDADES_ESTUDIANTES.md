# Implementación Completa del Sistema de Actividades para Estudiantes

## Resumen de Implementación

Se ha implementado el sistema completo de actividades para estudiantes, aprovechando los nuevos endpoints disponibles en el backend (branch `main-desarrollo`). Además, se modificó el perfil del estudiante para permitir únicamente el cambio de contraseña.

---

## 1. Modelos de Datos Actualizados/Creados

### Modelos Actualizados

#### `PreguntaActividad.java`
- **Cambio principal**: Campo `respuestas` → `respuesta_actividad`
- **Nuevos campos locales**:
  - `respuestaUsuario` (String)
  - `respuestaSeleccionadaId` (Integer)
- **Nuevos métodos**:
  - `tieneRespuestas()` - Verifica si tiene opciones de respuesta
  - `getTotalRespuestas()` - Cuenta las respuestas disponibles
  - `getRespuestaCorrecta()` - Obtiene la respuesta correcta

#### `RespuestaActividad.java`
- **Cambios de campos**:
  - `respuesta` → `respuestas`
  - `es_correcta` → `respuesta_correcta`
- **Métodos actualizados**:
  - `isEsCorrecta()` → `isRespuestaCorrecta()`

### Modelos Nuevos

#### `ActividadCompleta.java` (140 líneas)
Modelo completo para actividades con todos sus detalles.

**Campos principales**:
- `id_actividad` (int)
- `descripcion` (String)
- `tipo` (String)
- `fecha_inicio`, `fecha_fin` (String)
- `cuento` (CuentoApi) - Objeto anidado del cuento relacionado
- `pregunta_actividad` (List<PreguntaActividad>) - Lista de preguntas
- `deleted_at` (String) - Para soft delete

**Métodos utilitarios**:
- `getTipoDisplay()` - Formatea el tipo para mostrar
- `getTotalPreguntas()` - Cuenta las preguntas
- `tienePreguntas()` - Verifica si tiene preguntas
- `tieneCuento()` - Verifica si tiene cuento asociado

**Endpoint asociado**: `GET /api/actividades/actividadCompleta/:idActividad`

#### `ActividadesPorAulaResponse.java` (24 líneas)
Wrapper para la respuesta del endpoint de actividades por aula.

**Campos**:
- `actividades` (List<Actividad>)

**Endpoint asociado**: `GET /api/actividades/actividadesPorAula/:id_aula`

#### `ActividadCompletaResponse.java` (23 líneas)
Wrapper para la respuesta del endpoint de actividad completa.

**Campos**:
- `actividad` (ActividadCompleta)

---

## 2. Servicios API Actualizados/Creados

### `ActividadesApiService.java` (Actualizado)
Se agregó una nueva sección para endpoints accesibles por alumnos.

**Nuevos endpoints**:
```java
// ENDPOINTS PARA ALUMNOS
@GET("actividades/actividadesPorAula/{id_aula}")
Call<ApiResponse<ActividadesPorAulaResponse>> getActividadesPorAula(
    @Header("Authorization") String token,
    @Path("id_aula") int idAula
);

@GET("actividades/actividadCompleta/{idActividad}")
Call<ApiResponse<ActividadCompletaResponse>> getActividadCompleta(
    @Header("Authorization") String token,
    @Path("idActividad") int idActividad
);
```

**Organización**: Los endpoints de docentes se mantuvieron en una sección separada.

### `AlumnoApiService.java` (Nuevo - 219 líneas)
Servicio dedicado a operaciones de estudiantes.

**Métodos principales**:

1. **responderPregunta()**
   - Endpoint: `POST /api/alumnos/responder-pregunta/{id_pregunta}`
   - Permite enviar respuesta a una pregunta
   - Request: `{ "respuesta": "texto o ID de opción" }`
   - Response: `{ idRespuesta, mensaje, esCorrecta }`

2. **obtenerAulaAlumno()**
   - Endpoint: `GET /api/alumnos/obtenerAula`
   - Obtiene información del aula del estudiante
   - Response: `{ AulaInfo { idAula, nombreAula, codigoAula } }`

3. **cambiarPassword()** (Preparado para futuro)
   - Endpoint: `PUT /api/alumnos/cambiar-password`
   - Request: `{ passwordActual, passwordNueva }`
   - Response: `{ mensaje }`
   - **Nota**: Endpoint preparado, requiere implementación en backend

**Clases internas** (Request/Response):
- `ResponderPreguntaRequest`
- `RespuestaPreguntaResponse`
- `AulaAlumnoResponse` + `AulaInfo`
- `CambiarPasswordRequest`
- `CambioPasswordResponse`

### `ApiClient.java` (Actualizado)
Se agregó el getter para `AlumnoApiService`:

```java
public static AlumnoApiService getAlumnoApiService() {
    if (alumnoApiService == null) {
        alumnoApiService = getRetrofitInstance().create(AlumnoApiService.class);
    }
    return alumnoApiService;
}
```

---

## 3. Fragmentos Actualizados

### `ActividadesEstudianteFragment.java`
**Cambios principales**:

1. **Endpoint actualizado**: Ahora usa `getActividadesPorAula()` en lugar del antiguo endpoint que devolvía 403
2. **Eliminado manejo de 403**: Ya no se necesita el mensaje de "funcionalidad no disponible"
3. **Tipo de respuesta**: Cambiado de `ApiResponse<List<Actividad>>` a `ApiResponse<ActividadesPorAulaResponse>`
4. **Navegación mejorada**: Al hacer clic en una actividad, abre `DetalleActividadEstudianteActivity`

**Código clave**:
```java
actividadesApiService.getActividadesPorAula(token, aulaId)
    .enqueue(new Callback<ApiResponse<ActividadesPorAulaResponse>>() {
        @Override
        public void onResponse(...) {
            ActividadesPorAulaResponse data = response.body().getData();
            List<Actividad> actividades = data != null ? data.getActividades() : null;
            // ...
        }
    });
```

### `PerfilFragment.java`
**Cambio principal**: Se ocultó la opción "Editar Datos Personales"

```java
// Editar Datos Personales - REMOVIDO según requisito del cliente
// Solo se permite cambiar la contraseña, no los datos personales
opcionEditarDatos.setVisibility(View.GONE);
```

**Justificación**: El cliente solicitó que los estudiantes solo puedan cambiar su contraseña, no sus datos personales (nombre, apellido, fecha de nacimiento).

---

## 4. Nuevas Actividades

### `DetalleActividadEstudianteActivity.java` (280 líneas)
Pantalla para visualizar los detalles completos de una actividad.

**Funcionalidades**:
- Muestra título, descripción, tipo y fecha de la actividad
- Si hay cuento asociado: muestra tarjeta con botón "Leer Cuento"
- Lista de preguntas en modo preview (solo lectura)
- Contador de preguntas
- Botón "Comenzar Actividad" para iniciar respuestas

**Flujo**:
1. Recibe `actividad_id` por Intent
2. Llama a `getActividadCompleta(token, idActividad)`
3. Muestra información completa
4. Al hacer clic en "Comenzar Actividad" → abre `ResponderActividadActivity`

**Layout**: `activity_detalle_actividad_estudiante.xml`

**Componentes clave**:
- RecyclerView con adapter interno para preview de preguntas
- MaterialCardView para cuento (visible solo si existe)
- Barra de progreso durante carga

### `ResponderActividadActivity.java` (450 líneas)
Pantalla interactiva para responder las preguntas de una actividad.

**Funcionalidades**:
- Navegación entre preguntas (Anterior/Siguiente)
- Barra de progreso visual (porcentaje y barra)
- Soporte para dos tipos de preguntas:
  - **Opción múltiple**: RecyclerView con opciones seleccionables
  - **Respuesta abierta**: TextInputEditText multilinea
- Envío de respuestas al backend
- Feedback inmediato (correcto/incorrecto)
- Diálogo de confirmación al finalizar
- Diálogo de confirmación al salir

**Flujo detallado**:
1. Carga actividad completa con `getActividadCompleta()`
2. Muestra pregunta actual
3. Usuario selecciona/escribe respuesta
4. Al hacer clic en "Siguiente":
   - Si es opción múltiple: envía respuesta inmediatamente
   - Si es abierta: espera a que escriba
5. Backend responde si es correcta/incorrecta
6. Muestra diálogo con resultado
7. Avanza a siguiente pregunta o finaliza

**Layout**: `activity_responder_actividad.xml`

**Adapter personalizado**: `OpcionesRespuestaAdapter`
- Muestra opciones A, B, C, D...
- Resalta opción seleccionada con borde de color
- Callback al hacer clic

---

## 5. Layouts Creados

### `activity_detalle_actividad_estudiante.xml`
- AppBar con Toolbar
- NestedScrollView para contenido scrolleable
- Card de información de actividad (título, tipo, descripción, fecha)
- Card de cuento (oculto si no hay cuento)
- Card de preguntas con RecyclerView
- Botón "Comenzar Actividad"

### `activity_responder_actividad.xml`
- Toolbar en la parte superior
- Indicador de progreso (texto + barra)
- Card de pregunta actual
- RecyclerView para opciones múltiples (visible según tipo)
- TextInputLayout para respuesta abierta (visible según tipo)
- Footer fijo con botones Anterior/Siguiente

### `item_pregunta_preview.xml`
Item simple para preview de preguntas en DetalleActividadEstudianteActivity:
- Número de pregunta en círculo
- Texto de la pregunta

### `item_opcion_respuesta.xml`
Item para opciones de respuesta en ResponderActividadActivity:
- Letra de opción (A, B, C, D) en círculo
- Texto de la opción
- Card seleccionable con borde de color

---

## 6. Configuración del Manifest

Se agregaron las nuevas actividades al `AndroidManifest.xml`:

```xml
<activity
    android:name=".estudiante.DetalleActividadEstudianteActivity"
    android:exported="false"
    android:screenOrientation="portrait" />
<activity
    android:name=".estudiante.ResponderActividadActivity"
    android:exported="false"
    android:screenOrientation="portrait" />
```

Ambas configuradas con orientación portrait para mejor experiencia de usuario.

---

## 7. Preparación para el Futuro

### `CambiarPasswordEstudianteActivity.java`
Se agregó comentario TODO indicando la migración futura:

```java
// TODO: Migrar a AlumnoApiService.cambiarPassword cuando el backend implemente PUT /api/alumnos/cambiar-password
// Por ahora usa EstudiantesApiService como fallback
```

**Razón**: El endpoint `PUT /api/alumnos/cambiar-password` está preparado en `AlumnoApiService` pero el backend aún no lo implementa. Cuando esté listo, solo se necesita:
1. Cambiar `apiService` de `EstudiantesApiService` a `AlumnoApiService`
2. Actualizar la llamada al método
3. Mínimos cambios de enrutamiento

---

## 8. Endpoints del Backend Utilizados

### Endpoints YA implementados y funcionales:
1. `GET /api/actividades/actividadesPorAula/:id_aula`
   - Rol: alumno, docente, administrador
   - Devuelve lista de actividades del aula

2. `GET /api/actividades/actividadCompleta/:idActividad`
   - Rol: alumno, docente, administrador
   - Devuelve actividad con cuento, preguntas y respuestas

3. `POST /api/alumnos/responder-pregunta/:id_pregunta`
   - Rol: alumno
   - Envía respuesta del estudiante
   - Devuelve si es correcta/incorrecta

4. `GET /api/alumnos/obtenerAula`
   - Rol: alumno
   - Obtiene aula del estudiante

### Endpoint PREPARADO (pendiente en backend):
- `PUT /api/alumnos/cambiar-password`
  - Código listo en `AlumnoApiService`
  - Implementación en activity comentada para migración futura

---

## 9. Flujo Completo de Usuario (Estudiante)

### 1. Ver Actividades
- Estudiante ingresa a la pestaña "Actividades"
- `ActividadesEstudianteFragment` carga actividades del aula
- Se muestran actividades pendientes y completadas
- Contador de actividades visible

### 2. Ver Detalles de Actividad
- Estudiante hace clic en una actividad
- Se abre `DetalleActividadEstudianteActivity`
- Ve: título, descripción, tipo, fecha
- Si hay cuento: puede hacer clic en "Leer Cuento"
- Ve lista de preguntas (preview)
- Contador de preguntas

### 3. Comenzar Actividad
- Estudiante hace clic en "Comenzar Actividad"
- Se abre `ResponderActividadActivity`
- Ve primera pregunta con indicador de progreso

### 4. Responder Preguntas
**Si es opción múltiple**:
- Ve opciones A, B, C, D
- Selecciona una opción
- Opción se resalta con borde azul
- Sistema envía respuesta automáticamente
- Muestra diálogo: "¡Correcto!" o "Incorrecto"
- Avanza a siguiente pregunta

**Si es respuesta abierta**:
- Ve campo de texto multilinea
- Escribe su respuesta
- Hace clic en "Siguiente"
- Sistema envía respuesta
- Muestra confirmación
- Avanza o finaliza

### 5. Finalizar Actividad
- En última pregunta, botón cambia a "Finalizar"
- Muestra diálogo de confirmación
- Estudiante confirma
- Actividad se marca como completada
- Regresa a lista de actividades

### 6. Configuración de Perfil
- Estudiante va a pestaña "Perfil"
- **YA NO ve** opción "Editar Datos Personales" (oculta)
- **SÍ ve** opción "Cambiar Contraseña"
- Puede cambiar su contraseña de forma segura

---

## 10. Archivos Modificados

### Modelos (app/src/main/java/com/example/lectana/modelos/)
- ✅ `PreguntaActividad.java` (actualizado)
- ✅ `RespuestaActividad.java` (actualizado)
- ✅ `ActividadCompleta.java` (nuevo)
- ✅ `ActividadesPorAulaResponse.java` (nuevo)
- ✅ `ActividadCompletaResponse.java` (nuevo)

### Servicios (app/src/main/java/com/example/lectana/services/)
- ✅ `ActividadesApiService.java` (actualizado - endpoints de alumno)
- ✅ `AlumnoApiService.java` (nuevo - 219 líneas)
- ✅ `ApiClient.java` (actualizado - getter de AlumnoApiService)

### Fragmentos (app/src/main/java/com/example/lectana/estudiante/fragments/)
- ✅ `ActividadesEstudianteFragment.java` (actualizado - nuevo endpoint)
- ✅ `PerfilFragment.java` (actualizado - ocultar editar datos)

### Actividades (app/src/main/java/com/example/lectana/estudiante/)
- ✅ `DetalleActividadEstudianteActivity.java` (nuevo - 280 líneas)
- ✅ `ResponderActividadActivity.java` (nuevo - 450 líneas)

### Otros
- ✅ `CambiarPasswordEstudianteActivity.java` (comentario TODO agregado)
- ✅ `AndroidManifest.xml` (registradas nuevas actividades)

### Layouts (app/src/main/res/layout/)
- ✅ `activity_detalle_actividad_estudiante.xml` (nuevo)
- ✅ `activity_responder_actividad.xml` (nuevo)
- ✅ `item_pregunta_preview.xml` (nuevo)
- ✅ `item_opcion_respuesta.xml` (nuevo)

---

## 11. Características Destacadas

### 🎯 Arquitectura Limpia
- Separación clara entre servicios (AlumnoApiService vs ActividadesApiService)
- Modelos específicos para cada endpoint
- Adapters reutilizables

### 🔄 Gestión de Estados
- ProgressBar durante carga
- Estados de habilitación en botones
- Feedback visual (opciones seleccionadas)

### 💬 Feedback al Usuario
- Toasts informativos
- Diálogos de confirmación
- Resultados inmediatos (correcto/incorrecto)

### 🛡️ Validaciones
- Verificación de IDs en Intents
- Manejo de errores de red
- Validación de campos vacíos

### 📱 Experiencia de Usuario
- Navegación intuitiva (Anterior/Siguiente)
- Progreso visual claro
- Diseño Material Design
- Orientación portrait forzada

### 🔮 Preparado para el Futuro
- Endpoint de cambio de contraseña listo
- Código comentado para migración mínima
- Arquitectura escalable

---

## 12. Próximos Pasos (Recomendaciones)

1. **Testing Completo**
   - Probar flujo completo: listar → detalle → responder
   - Verificar respuestas correctas/incorrectas
   - Validar navegación entre preguntas

2. **Implementación Backend**
   - Implementar `PUT /api/alumnos/cambiar-password`
   - Migrar CambiarPasswordEstudianteActivity

3. **Mejoras Futuras**
   - Sistema de puntuación
   - Badges por actividades completadas
   - Historial de respuestas
   - Estadísticas de progreso

4. **Optimizaciones**
   - Caché de actividades
   - Guardado local de progreso
   - Modo offline parcial

---

## 13. Notas Técnicas

### Convenciones de Nombres
- Actividades: `DetalleActividadEstudianteActivity` (sufijo Estudiante para diferenciar de versión docente)
- Layouts: `activity_nombre_estudiante.xml`
- Items: `item_nombre_componente.xml`

### Gestión de Token
Todas las llamadas usan:
```java
String token = "Bearer " + sessionManager.getToken();
```

### Logging
Se incluye logging detallado:
```java
Log.d(TAG, "Mensaje informativo");
Log.e(TAG, "Mensaje de error", exception);
```

### Manejo de Errores
Patrón consistente:
```java
if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
    // Success
} else {
    // Error
}
```

---

## 14. Conclusión

Se ha implementado exitosamente el sistema completo de actividades para estudiantes, cumpliendo con todos los requisitos:

✅ **Lista de actividades** - Usando nuevo endpoint accesible para alumnos  
✅ **Detalle de actividad** - Con cuento, preguntas y toda la información  
✅ **Responder preguntas** - Soporte para opción múltiple y abierta  
✅ **Feedback inmediato** - Correcto/incorrecto después de cada respuesta  
✅ **Perfil simplificado** - Solo cambio de contraseña, sin edición de datos personales  
✅ **Código preparado para futuro** - Endpoint de cambio de contraseña listo  

La implementación sigue las mejores prácticas de Android, usa Material Design, y está lista para ser probada con el backend actualizado.

---

**Fecha de implementación**: 2024  
**Backend branch**: main-desarrollo  
**Backend URL**: https://lectana-backend.onrender.com/api/
