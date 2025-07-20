package com.unshd.detectionsobjects.core.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(@ApplicationContext private val context: Context): ViewModel()   {

    private val _textoBienvenida = MutableLiveData<String>("Detecta objetos en vivo usando la camara de tu dispositvo o desde una imagen de tu galeria")
    val textoBienvenida:LiveData<String> = _textoBienvenida

    fun setTextoBienvenida(texto:String){
        _textoBienvenida.postValue(texto)
    }
    fun getTextoBienvenida():String{
        return _textoBienvenida.value!!
    }

}