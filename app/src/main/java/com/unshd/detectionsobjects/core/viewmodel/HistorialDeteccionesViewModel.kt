package com.unshd.detectionsobjects.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HistorialDeteccionesViewModel@Inject constructor(private val dao: TextBlockDao): ViewModel() {

    fun loadHistorialDetecciones(){
        viewModelScope.launch {
            val lista = dao.getAll()
            lista.forEach {
                Log.d("historial_detecciones",it.toString())
            }
        }
    }

    fun insertHistorialDetecciones(deteccion:TextBlockEntity){
        viewModelScope.launch {
            dao.insert(deteccion)
            loadHistorialDetecciones()
        }

    }

}