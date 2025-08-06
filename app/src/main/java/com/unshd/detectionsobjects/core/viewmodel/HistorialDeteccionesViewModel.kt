package com.unshd.detectionsobjects.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.unshd.detectionsobjects.CoroutineModule
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.label.ImageLabelEntity
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HistorialDeteccionesViewModel@Inject constructor(private val dao: TextBlockDao,@CoroutineModule.IoDispatcher private val io: CoroutineDispatcher): ViewModel() {


    val listaDeteccionesText: StateFlow<List<TextBlockEntity>> =
        dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    fun loadHistorialDetecciones(){
        viewModelScope.launch {
            val lista = dao.getAll()
            /*lista.forEach {
                Log.d("historial_detecciones",it.toString())
            }*/
        }
    }



    fun insertHistorialDetecciones(deteccion:TextBlockEntity){
        viewModelScope.launch {
            dao.insert(deteccion)
            loadHistorialDetecciones()
        }

    }

    suspend fun getDeteccion(sessionId: String): List<TextBlockEntity> {
        return withContext(io) {
            dao.getBySession(sessionId)
        }
    }

    fun deleteDeteccion(deteccion: TextBlockEntity){
        viewModelScope.launch {
            dao.delete(deteccion)

        }
    }


}