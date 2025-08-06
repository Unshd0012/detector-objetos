package com.unshd.detectionsobjects.core.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.unshd.detectionsobjects.CoroutineModule
import com.unshd.detectionsobjects.TextRecognitionModule.TextRecognitionHilt
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DeteccionesViewModel@Inject constructor(@ApplicationContext private val context: Context,
                                              private val dao: TextBlockDao,
                                              @CoroutineModule.IoDispatcher private val io: CoroutineDispatcher,
                                              @TextRecognitionHilt private val recognizer: TextRecognizer
): ViewModel() {


    private val _imagenUri = MutableLiveData<Uri?>()
    val imagenUri: LiveData<Uri?> = _imagenUri

    fun setImagenUri(uri: Uri) {
        _imagenUri.value = uri
        processImageText()
    }

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun setText(text: String) {
        _text.postValue( text)
    }


    fun insertText(entity: TextBlockEntity) {
    viewModelScope.launch(io) {
        dao.insert(entity)
    }
    }

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
                            0,
                            UUID.randomUUID().toString(),
                            fullText.take(20),
                            fullText,
                            uri.toString(),
                            System.currentTimeMillis()
                        )
                        viewModelScope.launch(io) { dao.insert(entity) }
                    }
                    .addOnFailureListener { e ->
                        Log.e("TextRecognition", e.message ?: "error")
                    }
            } catch (e: Exception) {
                Log.e("TextRecognition", "InputImage error: ${e.message}")
            }
        }
    }



}