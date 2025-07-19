package com.unshd.detectionsobjects.core.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(application: Application): AndroidViewModel(application)   {

    private val _textoBienvenida = MutableLiveData<String>("Detecta objetos en vivo usando la camara de tu dispositvo o desde una imagen de tu galeria")
    val textoBienvenida:LiveData<String> = _textoBienvenida

    fun setTextoBienvenida(texto:String){
        _textoBienvenida.postValue(texto)
    }
    fun getTextoBienvenida():String{
        return _textoBienvenida.value!!
    }

    private fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }
}