package com.unshd.detectionsobjects.core.viewmodel


import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
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
    private val _filesPermissionGranted  =MutableLiveData<Boolean>(false)
    val filesPermissionGranted:LiveData<Boolean> = _filesPermissionGranted


    fun setAllPermisionGranted(allPermisionGranted:Boolean){
        _allPermisionGranted.postValue(allPermisionGranted)
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
    fun setFilesPermissionGranted(filesPermissionGranted:Boolean){
        _filesPermissionGranted.postValue(filesPermissionGranted)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun addAllPermisosApi33(){
        _listPermisos.postValue(
            listOf(
                PermisoApp("Camara","Permiso para usar la camara","Necesario",Manifest.permission.CAMERA,R.mipmap.camara),
                PermisoApp("Galeria","Permiso para usar la galeria","Necesario",Manifest.permission.READ_MEDIA_IMAGES,R.mipmap.galeria),
                PermisoApp("Ubicacion","Permiso para usar la ubicacion","Opcional",Manifest.permission.ACCESS_FINE_LOCATION,R.mipmap.ubicacion),
            )
        )

    }
    fun addAllPermisos(){
        _listPermisos.postValue(
            listOf(
                PermisoApp("Camara","Permiso para usar la camara","Necesario",Manifest.permission.CAMERA,R.mipmap.camara),
                PermisoApp("Galeria","Permiso para usar la galeria","Necesario",Manifest.permission.READ_EXTERNAL_STORAGE,R.mipmap.galeria),
                PermisoApp("Ubicacion","Permiso para usar la ubicacion","Opcional",Manifest.permission.ACCESS_FINE_LOCATION,R.mipmap.ubicacion))
        )
    }

    fun checkAllPermisionsApi33(){
        _allPermisionGranted.postValue(camaraPermissionGranted.value==true &&
                galeriaPermissionGranted.value==true &&
                filesPermissionGranted.value==true)
    }
    fun checkAllPermisions(){
        _allPermisionGranted.postValue(camaraPermissionGranted.value==true &&
                galeriaPermissionGranted.value==true &&
                filesPermissionGranted.value==true)
    }



}