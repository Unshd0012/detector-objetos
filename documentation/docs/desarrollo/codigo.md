# Procesamiento de Imágenes con ML Kit

En esta sección se muestran partes clave del código de la aplicación y su función. Solo se incluyen los elementos más importantes.

---

## Dependencias usadas

### CameraX
- `androidx.camera.core`: Núcleo de la cámara.  
- `androidx.camera.camera2`: Soporte para Camera2.  
- `androidx.camera.lifecycle`: Cámara ligada al ciclo de vida.  
- `androidx.camera.video`: Grabación de video.  
- `androidx.camera.view`: Preview y controles de cámara.  
- `androidx.camera.mlkit`: Puente cámara ↔ ML Kit.

### ML Kit
- `google.mlkit.objects.detection`: Detección de objetos.  
- `google.mlkit.image.labeling`: Etiquetado de imágenes.  
- `google.mlkit.text.recognition`: Reconocimiento de texto (OCR).

### Room
- `androidx.room.runtime`: Motor de base de datos local.  
- `androidx.room.ktx`: Extensiones Kotlin para Room.  
- `androidx.room.compiler` (kapt): Genera DAOs y entidades.

### Hilt / Dagger
- `hilt.android`: Inyección de dependencias.  
- `hilt.compiler` (kapt): Genera el código de Hilt.  
- `androidx.hilt.navigation.compose`: Hilt con Compose/Navegación.

### Kotlin Coroutines
- `kotlinx.coroutines.core`: Corrutinas base.  
- `kotlinx.coroutines.android`: Corrutinas en Android (Looper, Main).

### ViewModel
- `androidx.lifecycle.viewmodel.ktx`: Extensiones Kotlin para ViewModel.  
- `androidx.lifecycle.viewmodel.compose`: ViewModel en Compose.

### Navigation (Compose)
- `androidx.navigation.compose`: Navegación entre pantallas Compose.

### Serialization
- `kotlinx.serialization.json`: Serializar/parsear JSON.

### Coil
- `coil`: Carga de imágenes ligera para Compose/Android.

---

## Navegación

**Descripción:** Navegación declarativa en Jetpack Compose bajo **arquitectura de una sola Activity** (Single-Activity Architecture), usando rutas tipadas y Navigation Component.

- `NavHost`: mapa de la app; define todas las pantallas.  
- `composable`: cada pantalla (Permisos, Home, Opciones, etc.).  
- `navController`: guía de navegación; cambia de pantalla con `navigate(...)`.  
- Todo dentro de `MainActivity` ⇒ **single-activity**.

---

# Flujo de permisos en Compose

Esta secuencia muestra dónde se **prepara**, **dispara** y **muestra** el estado de los permisos.

## 1) Donde se “prepara” la solicitud del permiso

Se crea el **lanzador** que abre el diálogo del sistema.  
Usa `rememberLauncherForActivityResult` con `ActivityResultContracts.RequestPermission`.

```kotlin
val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted ->
    when (permiso.nombre) {
        "Camara" -> { vm.setCamaraPermissionGranted(isGranted); vm.checkPermisos() }
        "Galeria" -> { vm.setGaleriaPermissionGranted(isGranted); vm.checkPermisos() }
        "Ubicacion" -> { vm.setUbicacionPermissionGranted(isGranted); vm.checkPermisos() }
    }
}
```

## 2) Donde se dispara la solicitud

Al tocar un control, se ejecuta `permissionLauncher.launch(permiso.permiso)`.

```kotlin
modifier = Modifier.clickable {
    if (permiso.autorizado) {
        Toast.makeText(context, "Permiso ya autorizado", Toast.LENGTH_LONG).show()
    } else {
        // Dispara el diálogo del sistema
        permissionLauncher.launch(permiso.permiso)
    }
}
```

## 3) Donde se muestra el estado del permiso

Se decide qué texto mostrar según el estado observado del ViewModel.

```kotlin
Text(
    text = when (permiso.nombre) {
        "Camara"     -> if (permisoCamara == true) "Autorizado" else "Solicitar Permiso"
        "Galeria"    -> if (permisoGaleria == true) "Autorizado" else "Solicitar Permiso"
        "Ubicacion"  -> if (permisoUbicacion == true) "Autorizado" else "Solicitar Permiso"
        else -> ""
    },
    fontSize = 20.sp,
    color = Color.Blue,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.clickable { /* dispara solicitud en el paso 2 */ }
)
```

---

## LiveCameraScreen 

**Objetivo:** Mostrar la cámara en vivo con CameraX y superponer resultados de detección.

### 1) Preparar vistas
- `PreviewView`: muestra la cámara.
- `ObjectOverlayView`: dibuja lo detectado (cajas, textos).
- Se superponen en un `Box`.

```kotlin
val overlayView = remember { ObjectOverlayView(context) }
val previewView = remember { PreviewView(context) }

Box(modifier = Modifier.fillMaxSize()) {
    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
    AndroidView(factory = { overlayView }, modifier = Modifier.fillMaxSize())
}
```

### 2) Configurar CameraX
- `ProcessCameraProvider`: controla la cámara.
- `Preview`: envía imagen a PreviewView.
- `ImageAnalysis`: procesa fotogramas con ObjectDetectionAnalyzer.

```kotlin
val preview = Preview.Builder()
    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
    .build()
    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

val analysisUseCase = ImageAnalysis.Builder()
    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
    .build()
    .also {
        it.setAnalyzer(cameraExecutor, ObjectDetectionAnalyzer(overlayView))
    }
```

### 3) Vincular al ciclo de vida
```kotlin
cameraProvider.bindToLifecycle(
    context as LifecycleOwner,
    CameraSelector.DEFAULT_BACK_CAMERA,
    preview,
    analysisUseCase
)
```

### 4) Liberar recursos
```kotlin
onDispose {
    cameraExecutor.shutdown()
}
```

---

## Detecciones

### 1) Pantalla principal
Orquesta la UI y conecta con el ViewModel.

```kotlin
@Composable
fun DeteccionesScreen(vm: DeteccionesViewModel){
    Scaffold { innerPadding ->
        Column {
            HeadDetecciones(vm) { vm.setImagenUri(it) }  // seleccionar imagen
            BodyDetecciones(vm)                          // mostrar imagen
            BottomDetecciones(vm)                        // mostrar/accionar texto
        }
    }
}
```

- `HeadDetecciones`: elige imagen y guarda su Uri.
- `BodyDetecciones`: presenta la imagen elegida.
- `BottomDetecciones`: muestra el texto reconocido y acciones (copiar/compartir).

### 2) Seleccionar imagen (Photo Picker)
```kotlin
val pickMedia = rememberLauncherForActivityResult(
    ActivityResultContracts.PickVisualMedia()
) { uri ->
    if (uri != null) {
        context.contentResolver.takePersistableUriPermission(
            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        onSelectImage(uri) // -> vm.setImagenUri(uri)
    }
}
```

```kotlin
Image(
    painter = painterResource(R.mipmap.selectimagen),
    contentDescription = null,
    modifier = Modifier.clickable {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
)
```

Abre el Photo Picker, obtiene un Uri y lo pasa al VM.  
Se necesita el Uri para leer y procesar la imagen.

### 3) Mostrar imagen elegida
```kotlin
val uri by vm.imagenUri.observeAsState()
AsyncImage(
    model = uri,
    contentDescription = "Imagen",
    modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
)
```

Renderiza la vista previa.

### 4) Mostrar texto reconocido + acciones
```kotlin
val text by vm.text.observeAsState()
Text(text ?: "")
Row {
    text?.takeIf { it.isNotEmpty() }?.let {
        CopyText(it)   // copiar al portapapeles
        ShareText(it)  // compartir con Intent
    }
}
```

**Copiar:**
```kotlin
val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
clipboard.setPrimaryClip(ClipData.newPlainText("Texto copiado", textToShare))
```

**Compartir:**
```kotlin
val sendIntent = Intent(Intent.ACTION_SEND).apply {
    putExtra(Intent.EXTRA_TEXT, textToShare)
    type = "text/plain"
}
context.startActivity(Intent.createChooser(sendIntent, "Compartir texto"))
```

### 5) ViewModel: estado y flujo principal
Mantiene Uri y texto como LiveData. Al fijar el Uri, dispara el reconocimiento.

```kotlin
@HiltViewModel
class DeteccionesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: TextBlockDao,                       
    @CoroutineModule.IoDispatcher private val io: CoroutineDispatcher,
    @TextRecognitionHilt private val recognizer: TextRecognizer 
) : ViewModel() {

    private val _imagenUri = MutableLiveData<Uri?>()
    val imagenUri: LiveData<Uri?> = _imagenUri

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun setImagenUri(uri: Uri) {
        _imagenUri.value = uri
        processImageText() // dispara procesamiento
    }
}
```

Quarda Uri y dispara el análisis.

### 6) Reconocimiento de texto (ML Kit) +  Room
```kotlin
fun processImageText() {
    viewModelScope.launch(io) {
        val uri = imagenUri.value ?: return@launch
        try {
            val image = InputImage.fromFilePath(context, uri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val fullText = visionText.text.orEmpty()
                    _text.postValue(fullText)

                    val entity = TextBlockEntity(
                        id = 0,
                        uuid = UUID.randomUUID().toString(),
                        title = fullText.take(20),
                        content = fullText,
                        imageUri = uri.toString(),
                        createdAt = System.currentTimeMillis()
                    )
                    viewModelScope.launch(io) { dao.insert(entity) } // Room
                }
                .addOnFailureListener { e ->
                    Log.e("TextRecognition", e.message ?: "error")
                }
        } catch (e: Exception) {
            Log.e("TextRecognition", "InputImage error: ${e.message}")
        }
    }
}
```

- `InputImage`: prepara la imagen para ML Kit.
- `TextRecognizer`: obtiene visionText.text.
- `_text.postValue`: expone texto a la UI.
- `dao.insert`: persiste el resultado (historial).

### 7) Inyección y concurrencia (esencial)
```kotlin
@HiltViewModel
class DeteccionesViewModel @Inject constructor(
    /* ... , @TextRecognitionHilt private val recognizer: TextRecognizer */
)
viewModelScope.launch(io) { /* ... */ }
```

- Hilt inyecta Context, TextBlockDao (Room) y TextRecognizer.
- Dispatcher IO para trabajo pesado fuera del hilo principal.

**Resumen**  
- Elegir imagen → obtener Uri.  
- VM.setImagenUri → llama a processImageText.  
- ML Kit reconoce texto → actualiza LiveData y guarda en Room.  
- UI muestra imagen, texto y acciones copiar/compartir.

---

## Etiquetados

### 1) Pantalla EtiquetadosScreen
Coordina el flujo “seleccionar imagen → cargar bitmap → procesar etiquetas → mostrar resultados”.

```kotlin
fun EtiquetadosScreen(vm: EtiquetadosViewModel) {
    val uriImagen by vm.uriImagen.observeAsState()
    val imagen by vm.imagen.observeAsState()

    Scaffold {
        Column {
            HeadEtiquetados()                
            BodyEtiquetados { uri ->         
                vm.setUriImagen(uri)
                vm.loadBitmap(uri)           
            }
            BottomEtiquetados(vm, uriImagen) 

            if (imagen != null) {
                LaunchedEffect(imagen) {
                    vm.processBitmap(imagen!!) 
                }
            }
        }
    }
}
```

### 2) Selector de imagen (Photo Picker)
```kotlin
val pickMedia = rememberLauncherForActivityResult(
    ActivityResultContracts.PickVisualMedia()
) { uri ->
    if (uri != null) {
        context.contentResolver.takePersistableUriPermission(
            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        onSelectImage(uri)
    }
}
```

Obtener Uri y persistir permiso.

### 3) Cuerpo inferior
```kotlin
AsyncImage(model = uri, contentDescription = null)

LazyColumn {
    items(listaEtiquetados.size) { i ->
        val label = listaEtiquetados[i]
        // Card con: label.label, label.confidence, label.index
    }
}
```

### 4) Estado del ViewModel
```kotlin
val uriImagen: LiveData<Uri>
val imagen: LiveData<Bitmap?>
val listaEtiquetados: LiveData<List<ImageLabelEntity>>
val title: LiveData<String>
val sessionId: LiveData<String>
```

### 5) Cargar Bitmap desde Uri
```kotlin
fun loadBitmap(uri: Uri) {
    viewModelScope.launch(io) {
        val bmp = getBitmapFromUri(uri)
        withContext(Dispatchers.Main) { setImagen(bmp) }
    }
}
```

### 6) Etiquetado con ML Kit
```kotlin
fun processBitmap(bitmap: Bitmap) {
    viewModelScope.launch {
        val image = InputImage.fromBitmap(bitmap, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        setSessionId(UUID.randomUUID().toString())

        labeler.process(image)
            .addOnSuccessListener { labels ->
                setTitle(labels.firstOrNull()?.text ?: "Sin titulo")
                val lista = labels.map { l ->
                    ImageLabelEntity(
                        id = 0,
                        sessionId = sessionId.value!!,
                        title = title.value!!,
                        titlePorcentaje = /* % del primero */,
                        imageConfig = bitmap.config?.name ?: "Sin URL",
                        imageUri = uriImagen.value?.toString() ?: "",
                        source = "Galería",
                        label = l.text,
                        confidence = (if (l.confidence >= 1f) 100 else (l.confidence * 100).toInt()).toFloat(),
                        index = l.index,
                        createdAt = System.currentTimeMillis()
                    ).also { insertEtiquetado(it) }
                }
                setListaEtiquetados(lista)
            }
    }
}
```

### 7) Persistencia (Room) y concurrencia
```kotlin
fun insertEtiquetado(e: ImageLabelEntity) {
    viewModelScope.launch { withContext(io) { dao.insert(e) } }
}
```

### 8) Inyección con Hilt (dependencias esenciales)
```kotlin
@HiltViewModel
class EtiquetadosViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: ImageLabelDao,
    @CoroutineModule.IoDispatcher private val io: CoroutineDispatcher
) : ViewModel()
```

**Resumen**  
- Elegir imagen → Uri.  
- Cargar bitmap (IO) → imagen.  
- Etiquetar con ML Kit → lista de ImageLabelEntity.  
- Guardar en Room y mostrar en UI (LazyColumn + confianza/índice).
