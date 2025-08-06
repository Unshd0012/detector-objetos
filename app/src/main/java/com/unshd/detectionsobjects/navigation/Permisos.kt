package com.unshd.detectionsobjects.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator


import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.viewmodel.PermisosViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.unshd.detectionsobjects.core.model.PermisoApp

@Composable
fun PermisosScreen(vm:PermisosViewModel,onPermisosOk: () -> Unit){

    val context = LocalContext.current
    val listPermisos by vm.listPermisos.observeAsState()
    val allPermisionGranted by vm.allPermisionGranted.observeAsState()
    vm.checkPermisos()


    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        listPermisos?.let { vm.addAllPermisosApi33() }
    }else{
        listPermisos?.let { vm.addAllPermisos() }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color.White)
        ) {
            CircularProgressIndicator(modifier = Modifier.size(100.dp))
        }
    }
    Log.i("permisos", "PermisosScreen: $allPermisionGranted")
    if(allPermisionGranted==true){
        onPermisosOk()
    }else {


        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color.White)
            ) {
                HeadPermisos()
                BodyPermisos(vm)
                BottomPermisos()
            }
        }
    }
}

@Composable
fun BottomPermisos() {
    Column (modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Image(painter = painterResource(R.drawable.permisosrobot),contentDescription = "")
    }
}

@Composable
fun BodyPermisos(vm: PermisosViewModel) {
    val listPermisos by vm.listPermisos.observeAsState()
    val listSize = listPermisos?.size ?: 0
    Column (modifier=Modifier.fillMaxWidth().background(Color.White)){
        LazyColumn {
            items(listSize){ permiso ->
                CardPermiso(vm,listPermisos!![permiso])
            }
        }

    }

}

@Composable
fun HeadPermisos() {
    val activity = LocalContext.current as? Activity
    Row (verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Image(painter = painterResource(R.mipmap.cerrar), contentDescription = "", modifier = Modifier.size(40.dp)
            .clickable {
                activity?.finish()
            })
        Spacer(modifier = Modifier.width(20.dp))
        Text("PERMISOS", fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CardPermiso(vm:PermisosViewModel,permiso: PermisoApp) {

    val context = LocalContext.current
    val permisoCamara by vm.camaraPermissionGranted.observeAsState()
    val permisoGaleria by vm.galeriaPermissionGranted.observeAsState()
    val permisoUbicacion by vm.ubicacionPermissionGranted.observeAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when (permiso.nombre) {
            "Camara" -> {vm.setCamaraPermissionGranted(isGranted)
            vm.checkPermisos()}
            "Galeria" -> {vm.setGaleriaPermissionGranted(isGranted)
                vm.checkPermisos()}
            "Ubicacion" -> {vm.setUbicacionPermissionGranted(isGranted)
                vm.checkPermisos()}
        }

    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxSize().padding(5.dp)
    ){
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(bitmap = ImageBitmap.imageResource(id = permiso.imagen),
                    tint = Color.Unspecified,contentDescription = "",
                    modifier = Modifier.size(50.dp))
                Column (){
                    Text(text = permiso.nombre, fontSize = 20.sp)
                    Text(text = permiso.tipo, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Icon(bitmap = ImageBitmap.imageResource(id = R.mipmap.informacion),
                    tint = Color.Unspecified,contentDescription = "",
                    modifier = Modifier.size(30.dp).clickable {
                        Toast.makeText(context,permiso.descripcion, Toast.LENGTH_LONG).show()
                    })

                Text(
                    text =
                    when(permiso.nombre){
                        "Camara" -> {
                            if(permisoCamara!!) "Autorizado" else "Solicitar Permiso"}
                        "Galeria" -> {
                            if(permisoGaleria!!) "Autorizado" else "Solicitar Permiso"}
                        "Ubicacion" -> {
                            if(permisoUbicacion!!) "Autorizado" else "Solicitar Permiso"}
                        else -> ""
                    },
                    fontSize = 20.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        if(permiso.autorizado) {
                            Toast.makeText(context, "Permiso ya autorizado", Toast.LENGTH_LONG).show()
                        }else{
                            permissionLauncher.launch(permiso.permiso)

                        }
                    }
                )
            }
        }
    }
}


