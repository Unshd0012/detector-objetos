package com.unshd.detectionsobjects.navigation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.viewmodel.DeteccionesViewModel
import com.unshd.detectionsobjects.core.viewmodel.EtiquetadosViewModel

@Composable
fun DeteccionesScreen(vm:DeteccionesViewModel){

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)){

                HeadDetecciones()
                BodyDetecciones()
                BottomDetecciones()
            }
        }
}


@Composable
@Preview(showBackground = true)
fun HeadDetecciones() {
    Column (verticalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Text("Text Detector", fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
@Preview(showBackground = true)
fun BodyDetecciones() {
    val context = LocalContext.current
    //onSelectImage: (Uri) -> Unit

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")

                try {
                    // 👇 Requiere FLAG_GRANT_READ_URI_PERMISSION para ser persistido
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    Log.d("PhotoPicker", "Permiso persistente concedido")
                } catch (e: SecurityException) {
                    Log.e("PhotoPicker", "Error al conceder permiso persistente: ${e.message}")
                }

               // onSelectImage(uri )
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    Column (horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth()){
        Image(painter = painterResource(R.mipmap.selectimagen), contentDescription = "",
            modifier = Modifier.padding(5.dp).clickable{
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            })
    }

    AsyncImage(
        model = ,
        contentDescription = "Imagen",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )

}

@Composable
@Preview(showBackground = true)
fun BottomDetecciones() {

}