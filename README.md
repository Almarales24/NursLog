<h1 align="center">NursLog</h1>

<p align="center">
  <img src="https://github.com/user-attachments/assets/9cc4517d-973e-4c5c-be42-34dc697fa427" width="120" alt="NursLog Logo" />
</p>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Licencia](https://img.shields.io/badge/Licencia-MIT-blue.svg)](LICENSE)

NursLog es una aplicación Android para estudiantes y auxiliares de enfermería. Integra la administración de medicamentos mediante un **MAR** (*Medication Administration Record*) y un historial clínico simplificado para registrar, consultar y dar seguimiento a la información asistencial.

## Capturas de pantalla

<p align="center">
  <img src="https://github.com/user-attachments/assets/05a06e25-d64d-4ba2-a0d1-fd838d72363d" width="240" alt="Captura 1" />
  <img src="https://github.com/user-attachments/assets/45410dd4-6a70-421e-9b6b-4b3753d22786" width="240" alt="Captura 2" />
  <img src="https://github.com/user-attachments/assets/90eba79e-3cf5-4967-af89-eaaefd64ab48" width="240" alt="Captura 3" />
  <img src="https://github.com/user-attachments/assets/a47568e7-b063-434a-bb9e-862ba38c120d" width="240" alt="Captura 4" />
  <img src="https://github.com/user-attachments/assets/e5440dbe-07f0-4f5c-81e4-3e7b11f481b3" width="240" alt="Captura 5" />
  <img src="https://github.com/user-attachments/assets/ea412178-72fb-433d-bdb2-dc173f8cf3ad" width="240" alt="Captura 6" />
</p>

## Características principales

- Registro de administración de medicamentos (MAR) con lista de verificación de los cinco correctos.
- Gestión de pacientes: creación, búsqueda, edición y eliminación.
- Historial clínico por paciente con diagnósticos, alergias y notas de enfermería.
- Diferenciación entre notas generales y recomendaciones del personal de salud.
- Consulta y normalización de medicamentos mediante RxNorm; recomendaciones y advertencias mediante OpenFDA.
- Registro estandarizado de diagnósticos con ICD-10 y traducción de contenido clínico al español.
- Recordatorios locales de medicación mediante WorkManager y notificaciones Android.
- Tema claro/oscuro y precarga de datos de ejemplo en la primera ejecución.

## Stack técnico

| Tecnología | Uso en NursLog |
| --- | --- |
| Kotlin | Lenguaje principal de desarrollo. |
| Jetpack Compose | Interfaz declarativa y componentes reutilizables. |
| Room | Persistencia local y consultas tipadas. |
| MVVM | Separación entre UI, estado de presentación y acceso a datos. |
| Retrofit | Consumo de APIs HTTP externas. |
| WorkManager | Recordatorios periódicos de medicación. |
| Coroutines / Flow | Operaciones asíncronas y observación reactiva de datos. |
| Navigation Compose | Navegación entre pantallas. |

## Arquitectura

NursLog utiliza **MVVM** con una organización compacta adecuada para su alcance académico:

- **`data`**: entidades Room, DAOs, base de datos, repositorios y clientes Retrofit. Es la capa responsable del almacenamiento local y la comunicación con servicios externos.
- **`ui`**: pantallas Compose, componentes, navegación y `ViewModel` por módulo. Los ViewModels exponen estado mediante `StateFlow` y delegan la obtención o modificación de datos a los repositorios.
- **`notifications`**: trabajo en segundo plano y notificaciones de recordatorio, separado de la interfaz.

No se usa una capa `domain` independiente: la lógica de coordinación reside en los repositorios y ViewModels. La UI observa estado, sin acceder directamente a Room ni a la red.

## Integraciones externas

| Servicio | Uso en NursLog |
| --- | --- |
| [RxNorm](https://lhncbc.nlm.nih.gov/RxNav/APIs/index.html) | Sugiere nombres normalizados de medicamentos al crear registros. |
| [OpenFDA](https://open.fda.gov/apis/) | Consulta recomendaciones de dosificación y advertencias de medicamentos. |
| [ICD-10 Clinical Tables](https://clinicaltables.nlm.nih.gov/) | Autocompleta códigos y descripciones diagnósticas. |
| [MyMemory Translation](https://mymemory.translated.net/doc/spec.php) | Traduce al español resultados clínicos obtenidos en inglés. |

## Instalación y configuración

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Almarales24/NursLog.git
   cd NursLog
   ```
2. Abre el proyecto en **Android Studio**.

3. Ejecuta **Sync Project with Gradle Files**.

4. Inicia un emulador o conecta un dispositivo con Android API 26 o superior.

5. Selecciona la configuración `app` y presiona **Run**.

Las APIs indicadas son de acceso público. Verifica sus límites de uso y condiciones vigentes antes de distribuir la aplicación.

## Estructura del proyecto

```text
NursLog/
└── app/src/main/java/com/nurslog/app/
    ├── MainActivity.kt                 # Punto de entrada de la UI
    ├── NursLogApplication.kt           # Inicialización global de la app
    ├── data/
    │   ├── entity/                     # Entidades Room
    │   ├── local/                      # Base de datos Room
    │   ├── remote/                     # APIs Retrofit
    │   └── repository/                 # Acceso unificado a datos
    ├── dao/                            # Interfaces DAO de Room
    ├── notifications/                  # WorkManager y notificaciones
    └── ui/
        ├── paciente/                   # Gestión de pacientes
        ├── historial/                  # Historial clínico
        ├── medicacion/                 # Administración de medicamentos
        ├── components/                 # Composables reutilizables
        ├── navigation/                 # Grafo de navegación
        └── theme/                      # Colores y tipografía
```
### Modelo de datos

| Archivo | Propósito | Campos principales |
| --- | --- | --- |
| `data/entity/Paciente.kt` | Entidad central a la que se vincula la información clínica. | `id`, `nombre`, `cama`, `sala`, `edad` |
| `data/entity/Diagnostico.kt` | Diagnóstico clínico asociado a un paciente. | `id`, `pacienteId`, `descripcion`, `estado` |
| `data/entity/Alergia.kt` | Alergia registrada para un paciente. | `id`, `pacienteId`, `sustancia`, `severidad` |
| `data/entity/NotaEnfermeria.kt` | Nota u observación de enfermería con trazabilidad. | `id`, `pacienteId`, `texto`, `autor`, `tipo`, `fechaHora` |
| `data/entity/Medicamento.kt` | Catálogo de medicamentos programables. | `id`, `nombre`, `dosis`, `via`, `recomendacion` |
| `data/entity/Horario.kt` | Programación de un medicamento para un paciente. | `id`, `medicamentoId`, `pacienteId`, `hora` |
| `data/entity/RegistroAdministracion.kt` | Registro de la administración de una dosis. | `id`, `horarioId`, `estado`, `horaReal`, `enfermero`, `nota` |

`data/local/NursLogDatabase.kt` extiende `RoomDatabase`, declara las siete entidades y expone sus DAOs. Su callback de inicialización precarga datos de ejemplo en la primera creación de la base de datos.

### Acceso local y repositorios

Los DAOs (`PacienteDao`, `DiagnosticoDao`, `AlergiaDao`, `NotaEnfermeriaDao`, `MedicamentoDao`, `HorarioDao` y `RegistroAdministracionDao`) implementan operaciones CRUD y consultas reactivas con `Flow`. Las operaciones de una sola ejecución se exponen como funciones `suspend` para su uso desde tareas en segundo plano.

| Archivo | Responsabilidad |
| --- | --- |
| `data/repository/PacienteRepository.kt` | CRUD y búsqueda de pacientes por nombre. |
| `data/repository/HistorialRepository.kt` | Gestión de diagnósticos, alergias y notas; búsqueda ICD-10 y traducción al español. |
| `data/repository/MedicacionRepository.kt` | Programación de medicamentos, consulta RxNorm/OpenFDA y registro de administración. |

### Servicios remotos

| Archivo | Función principal |
| --- | --- |
| `data/remote/icd10/Icd10Api.kt` | `buscarDiagnosticos(terminos)` consulta códigos y descripciones ICD-10. |
| `data/remote/openfda/OpenFdaApi.kt` | `buscarEtiqueta(busqueda)` obtiene dosificación y advertencias. |
| `data/remote/rxnorm/RxNormApi.kt` | `buscarMedicamentos(nombre)` devuelve nombres de medicamentos normalizados. |
| `data/remote/translate/TranslateApi.kt` | `traducir(texto, langpair)` traduce contenido clínico. |
| `data/remote/RetrofitInstance.kt` | Centraliza la creación de los clientes Retrofit. |

### Interfaz y presentación

| Módulo | ViewModel | Pantallas y componentes principales |
| --- | --- | --- |
| `ui/paciente/` | `PacienteViewModel` | `SeleccionPacienteScreen`, `AgregarPacienteDialog`; alta, edición, eliminación y búsqueda con *debounce*. |
| `ui/historial/` | `HistorialViewModel` | `HistorialScreen`, `DiagnosticoTab`, `AlergiaTab`, `NotaTab`, `DiagnosticoDialog`, `AlergiaDialog`. |
| `ui/medicacion/` | `MedicacionViewModel` | `MedicacionScreen`, `MedicamentoDialog`, `ChecklistDialog`; combina datos en `MedicacionUiItem`. |

Cada ViewModel usa su propia *factory* (`PacienteViewModelFactory`, `HistorialViewModelFactory` y `MedicacionViewModelFactory`) para recibir el repositorio correspondiente; el proyecto no incorpora un framework de inyección de dependencias.
Los componentes reutilizables incluyen `PacienteCard.kt`, `MedicamentoItem.kt`, `EstadoBadge.kt`, `SwipeToDeleteItem.kt` y `AnimatedButton.kt`. `ui/navigation/NavGraph.kt` define las rutas `seleccion_paciente`, `historial/{pacienteId}` y `medicacion/{pacienteId}`. El tema se configura en `ui/theme/Color.kt`, `Theme.kt` y `Type.kt`.

### Recordatorios

- `notifications/MedicacionReminderWorker.kt`: `CoroutineWorker` que revisa cada 15 minutos los horarios pendientes, respeta el mínimo de WorkManager y evita duplicados mediante `SharedPreferences`.
- `notifications/NotificationHelper.kt`: crea el canal de notificaciones y publica los recordatorios locales.

## Metodología de desarrollo

Se aplica un enfoque de **Scrum individual**: priorización de historias de usuario, implementación incremental por módulo, revisión funcional y commits por historia o unidad funcional para mantener trazabilidad en Git.

## Historias de usuario implementadas

| ID | Historia | Estado |
| --- | --- | --- |
| HU-01 | Registrar notas de enfermería por procedimiento para mantener la trazabilidad del cuidado. | ✅ |
| HU-03 | Mostrar el propósito de los datos solicitados para facilitar su comprensión. | ✅ |
| HU-04 | Registrar recomendaciones del personal de salud dirigidas al paciente. | ✅ |
| HU-05 | Asociar notas de enfermería a cada administración de medicación. | ✅ |
| HU-06 | Vincular el historial clínico con alertas del paciente para apoyar una administración segura. | ✅ |
| HU-07 | Diferenciar vista de paciente y de enfermero. Reemplazada por recordatorios automáticos, pues la app es de uso exclusivo del personal de enfermería. | No aplica |
| HU-08 | Acceder a la clasificación ICD-10 para estandarizar diagnósticos. | ✅ |
| HU-09 | Mantener una navegación visualmente consistente entre pestañas. | ✅ |
| HU-10 | Conservar un checklist de verificación simple durante la administración. | ✅ |

## Autor y créditos

**https://github.com/Almarales24**

## Licencia

Distribuido bajo la [Licencia MIT](LICENSE). Consulta el archivo `LICENSE` para conocer sus términos. 

