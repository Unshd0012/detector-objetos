package com.unshd.detectionsobjects.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unshd.detectionsobjects.CoroutineModule
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistorialDeteccionesViewModel@Inject constructor(private val dao: TextBlockDao, @CoroutineModule.IoDispatcher private val io: CoroutineDispatcher): ViewModel() {


    val listaDeteccionesText: StateFlow<List<TextBlockEntity>> =
        dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())




    fun insertHistorialDetecciones(deteccion:TextBlockEntity){
        viewModelScope.launch (io){
            dao.insert(deteccion)

        }

    }

    suspend fun getDeteccion(sessionId: String): List<TextBlockEntity> {
        return withContext(io) {
            dao.getBySession(sessionId)
        }
    }

    fun deleteDeteccion(deteccion: TextBlockEntity){
        viewModelScope.launch (io){
            dao.delete(deteccion)

        }
    }


}