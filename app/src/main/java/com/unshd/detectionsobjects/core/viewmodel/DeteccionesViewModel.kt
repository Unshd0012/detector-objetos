package com.unshd.detectionsobjects.core.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.InputStream

@HiltViewModel
class DeteccionesViewModel@Inject constructor(@ApplicationContext private val context: Context,
                                              private val dao: TextBlockDao): ViewModel() {


    private val _imagenUri = MutableLiveData<Uri>()
    val imagenUri: LiveData<Uri> = _imagenUri
    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

    fun setImagenUri(uri: Uri) {
        _imagenUri.value = uri
        setBitmap(getBitmapFromUri(uri)!!)
    }

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun setText(text: String) {
        _text.value = text
    }
    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
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