package com.unshd.detectionsobjects.navigation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.viewmodel.PermisosViewModel
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun PermisosScreen(vm:PermisosViewModel,onPermisosOk: () -> Unit){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            HeadPermisos()
            BodyPermisos(vm)
        }
    }

}

@Composable
fun BodyPermisos(vm: PermisosViewModel) {
    val allPermisionGranted by vm.allPermisionGranted.observeAsState()
    val listPermisos by vm.listPermisos.observeAsState()
    val listSize = listPermisos?.size ?: 0
    Column (modifier=Modifier.fillMaxWidth()){
        LazyColumn {
            items(listSize){ permiso ->
                CardPermiso()
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
@Preview(showBackground = true)
fun CardPermiso(){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxSize().padding(5.dp)
    ){
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp).border(2.dp, Color.Black),
            colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(bitmap = ImageBitmap.imageResource(id = R.mipmap.camara),
                    tint = Color.Unspecified,contentDescription = "",
                    modifier = Modifier.size(50.dp))
                Column (){
                    Text(text = "Camara ")
                    Text(text = "Necesario")
                }



            }
        }
    }
}