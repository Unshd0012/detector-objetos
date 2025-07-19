package com.unshd.detectionsobjects.core.model

data class PermisoApp(val nombre:String, val descripcion:String, val tipo:String,
                      var autorizado:Boolean, val permiso:String, val imagen:Int)
