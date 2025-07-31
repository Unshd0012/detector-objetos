package com.unshd.detectionsobjects.core.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.unshd.detectionsobjects.CoroutineModule
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.label.ImageLabelEntity
import com.unshd.detectionsobjects.core.model.LabelingGoogle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.UUID

@HiltViewModel
class EtiquetadosViewModel@Inject constructor(@ApplicationContext private val context: Context,
                                              private val dao: ImageLabelDao,
                                              @CoroutineModule.IoDispatcher private val io: CoroutineDispatcher): ViewModel() {


    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String> = _sessionId
    private val _title = MutableLiveData<String>("Sin titulo")
    val title: LiveData<String> = _title

    private val _listaEtiquetados = MutableLiveData<List<ImageLabelEntity>>(emptyList())
    val listaEtiquetados: LiveData<List<ImageLabelEntity>> = _listaEtiquetados
    private val uriImagenSelected = MutableLiveData<Uri>()
    val uriImagen: LiveData<Uri> = uriImagenSelected
    private val imagenSelected = MutableLiveData<Bitmap?>()
    val imagen: LiveData<Bitmap?> = imagenSelected

    fun setSessionId(sessionId: String) {
        _sessionId.postValue(sessionId)
    }
    fun setTitle(title: String) {
        _title.postValue(title)
    }
    fun setListaEtiquetados(lista: List<ImageLabelEntity>) {
        _listaEtiquetados.postValue(lista)
    }
    fun clearListaEtiquetados() {
        _listaEtiquetados.postValue(emptyList())
    }
    fun setUriImagen(uri:Uri){
        uriImagenSelected.postValue(uri)

    }
    fun setImagen(bitmap: Bitmap?){
        imagenSelected.postValue(bitmap)
    }

    fun getTitle():String{
        return title.value!!
    }



    fun insertEtiquetado(deteccion: ImageLabelEntity){
        viewModelScope.launch {
            withContext(io) {
                dao.insert(deteccion)
            }
        }
    }


    fun processBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            // Solo ejecutar cuando el bitmap cambie
            val image = InputImage.fromBitmap(bitmap, 0)
            val listaEtiquetas = mutableListOf<ImageLabelEntity>()
            setListaEtiquetados(emptyList())
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            setSessionId(UUID.randomUUID().toString())



            labeler.process(image)
                .addOnSuccessListener { labels ->
                    setTitle(labels.firstOrNull()?.text ?: "Sin titulo")
                    val title = labels.firstOrNull()?.text ?: "Sin titulo"
                    val porcentaje: Double = labels.firstOrNull()?.confidence!!.toDouble() ?: 0.0

                    val titlePorcentaje: String = if (porcentaje >= 1.0) {
                        "100"
                    } else {
                        String.format("%02d", (porcentaje * 100).toInt())
                    }
                    for (label in labels) {



                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        val porcentaje = if (confidence >= 1.0) "100" else String.format("%02d", (confidence * 100).toInt())
                        val entity = ImageLabelEntity(0,
                            sessionId.value!!,
                            title,
                            titlePorcentaje,
                             bitmap.config?.name ?: "Sin URL",
                            uriImagen.value?.toString() ?: "",
                            "Galería",text,porcentaje.toFloat(),index,System.currentTimeMillis())
                        listaEtiquetas.add(entity)
                        Log.d("Etiquetado", "Text: $text, Confidence: $confidence, Index: $index")
                        insertEtiquetado(entity)
                    }

                    setListaEtiquetados(listaEtiquetas)

                }
                .addOnFailureListener {
                    Log.d("Etiquetado", "Error: ${it.message}")
                }

        }
    }
    fun loadBitmap(uri: Uri) {
        viewModelScope.launch(io) {
            val bmp = getBitmapFromUri(uri)
            withContext(Dispatchers.Main) { setImagen(bmp) }
        }
    }


    fun getBitmapFromUri( uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val contentResolver: ContentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}