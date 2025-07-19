package com.unshd.detectionsobjects.core.viewmodel


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.model.PermisoApp
import jakarta.inject.Inject

class PermisosViewModel@Inject constructor(): ViewModel() {

    private val _listPermisos = MutableLiveData<List<PermisoApp>>(emptyList())
    val listPermisos: LiveData<List<PermisoApp>> = _listPermisos

    private val _allPermisionGranted  =MutableLiveData<Boolean>(false)
    val allPermisionGranted:LiveData<Boolean> = _allPermisionGranted
    private val _camaraPermissionGranted  =MutableLiveData<Boolean>(false)
    val camaraPermissionGranted:LiveData<Boolean> = _camaraPermissionGranted
    private val _galeriaPermissionGranted  =MutableLiveData<Boolean>(false)
    val galeriaPermissionGranted:LiveData<Boolean> = _galeriaPermissionGranted
    private val _ubicacionPermissionGranted  =MutableLiveData<Boolean>(false)
    val ubicacionPermissionGranted:LiveData<Boolean> = _ubicacionPermissionGranted


    fun setAllPermisionGranted(allPermisionGranted:Boolean){
        _allPermisionGranted.value = allPermisionGranted
    }
    fun setCamaraPermissionGranted(camaraPermissionGranted:Boolean){
        _camaraPermissionGranted.postValue(camaraPermissionGranted)
    }
    fun setGaleriaPermissionGranted(galeriaPermissionGranted:Boolean){
        _galeriaPermissionGranted.postValue(galeriaPermissionGranted)
    }
    fun setUbicacionPermissionGranted(ubicacionPermissionGranted:Boolean){
        _ubicacionPermissionGranted.postValue(ubicacionPermissionGranted)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun addAllPermisosApi33(){
        _listPermisos.postValue(
            listOf(
                PermisoApp("Camara","Este permiso es indispensable para el correcto funcionamiento de la app","Necesario",_camaraPermissionGranted.value!!,Manifest.permission.CAMERA,R.mipmap.camara),
                PermisoApp("Galeria","Este permiso es esencial para el correcto funcionamiento de la app","Necsario",_galeriaPermissionGranted.value!!,Manifest.permission.READ_MEDIA_IMAGES,R.mipmap.galeria),
                PermisoApp("Ubicacion","Permiso para usar la ubicacion","Opcional",_ubicacionPermissionGranted.value!!,Manifest.permission.ACCESS_FINE_LOCATION,R.mipmap.ubicacion),
            )
        )

    }
    fun addAllPermisos(){
        _listPermisos.postValue(
            listOf(
                PermisoApp("Camara","Este permiso es indispensable para el correcto funcionamiento de la app","Necesario",_camaraPermissionGranted.value!!,Manifest.permission.CAMERA,R.mipmap.camara),
                //Importante aqui: En permiso galeria se agrega para leer archivos, no media IMAGE
                PermisoApp("Galeria","Este permiso es esencial para el correcto funcionamiento de la app","Necesario",_galeriaPermissionGranted.value!!,Manifest.permission.READ_EXTERNAL_STORAGE,R.mipmap.galeria),
                PermisoApp("Ubicacion","Permiso para usar la ubicacion","Opcional",_ubicacionPermissionGranted.value!!,Manifest.permission.ACCESS_FINE_LOCATION,R.mipmap.ubicacion))
        )
    }
    //Revia los permisos en base al numero de version
    fun checkPermisos(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            listPermisos.value?.forEach{
                p ->
                p.autorizado = isPermissionGranted(context,p.permiso)
                when(p.nombre){
                    "Camara" -> {
                        _camaraPermissionGranted.value=isPermissionGranted(context,p.permiso)
                    }
                    "Galeria" -> {
                        _galeriaPermissionGranted.value=isPermissionGranted(context,p.permiso)
                    }
                    "Ubicacion" -> {
                        _ubicacionPermissionGranted.value=isPermissionGranted(context,p.permiso)
                    }
                }
            }
            checkAllPermisionsApi33()
        }else{
            checkAllPermisions()
            listPermisos.value?.forEach{
                    p ->
                p.autorizado = isPermissionGranted(context,p.permiso)
                when(p.nombre){
                    "Camara" -> {
                        _camaraPermissionGranted.value=isPermissionGranted(context,p.permiso)
                    }
                    "Galeria" -> {
                        _galeriaPermissionGranted.value=isPermissionGranted(context,p.permiso)
                    }
                    "Ubicacion" -> {
                        _ubicacionPermissionGranted.value=isPermissionGranted(context,p.permiso)
                    }
                }
                Log.i("permisos", "checkPermisos: $p")
            }
            checkAllPermisions()
        }
    }

    fun checkAllPermisionsApi33(){
         setAllPermisionGranted( camaraPermissionGranted.value==true &&
                galeriaPermissionGranted.value==true)
    }
    fun checkAllPermisions(){
         setAllPermisionGranted( camaraPermissionGranted.value==true &&
                galeriaPermissionGranted.value==true)
    }

    fun isPermissionGranted(context: Context, permiso: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permiso
        ) == PackageManager.PERMISSION_GRANTED
    }




}