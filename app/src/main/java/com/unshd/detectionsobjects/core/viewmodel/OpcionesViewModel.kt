package com.unshd.detectionsobjects.core.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.model.OpcionesApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

@HiltViewModel
class OpcionesViewModel@Inject constructor(@ApplicationContext private val context: Context): ViewModel() {

    private val _listaOpciones = MutableLiveData<List<OpcionesApp>>()
    val listaOpciones: LiveData<List<OpcionesApp>> = _listaOpciones

    fun loadOpciones(){
        val lista = listOf(
            OpcionesApp("Camara en Vivo", R.mipmap.camaraenvivo,"Detecta objetos en vivo usando la camara",true),
            OpcionesApp("Text Detector Galeria", R.mipmap.galeriaxx,"Detecta objetos en imagenes de la galeria",true),
            OpcionesApp("Etiquetado desde Galeria", R.mipmap.galeriax,"Etiqueta imagenes desde la galeria",true),
            OpcionesApp("Historial de Detecciones", R.mipmap.historial,"Ve tu historial de detecciones",true),
            OpcionesApp("Historial de Etiquetados", R.mipmap.historialetiquetas,"Ve tu historial de etiquetados",true),
        OpcionesApp("Permisos", R.mipmap.file,"Permisos de la app",true))

        _listaOpciones.value = lista
    }


    fun settingsAppPermisos() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

}