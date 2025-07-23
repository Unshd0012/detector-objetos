package com.unshd.detectionsobjects.core.viewmodel

import androidx.lifecycle.ViewModel
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class HistorialEtiquetadoViewModel@Inject constructor(private val dao: ImageLabelDao): ViewModel() {
}