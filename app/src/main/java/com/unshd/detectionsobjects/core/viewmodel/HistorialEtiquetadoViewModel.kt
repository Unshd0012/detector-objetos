package com.unshd.detectionsobjects.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unshd.detectionsobjects.CoroutineModule
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.label.ImageLabelEntity
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HistorialEtiquetadoViewModel@Inject constructor(private val dao: ImageLabelDao,
                                                      @CoroutineModule.IoDispatcher private val io: CoroutineDispatcher
): ViewModel() {

    val listaEtiquetados: StateFlow<List<ImageLabelEntity>> =
        dao.getAllFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _showBottomBar = MutableLiveData(false)
    val showBottomBar: LiveData<Boolean> = _showBottomBar


    fun setShowBottomBar(show: Boolean) {
        _showBottomBar.value = show
    }

    suspend fun getEtiquetado(sessionId: String): List<ImageLabelEntity> {
        return withContext(io) {
            dao.getBySession(sessionId)
        }
    }


        fun deleteEtiquetado(label: ImageLabelEntity) {
            viewModelScope.launch {
                withContext(io) {
                    dao.deleteBySession(label.sessionId)
                }
            }

        }
    }

