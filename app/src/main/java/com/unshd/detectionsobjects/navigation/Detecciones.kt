package com.unshd.detectionsobjects.navigation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.viewmodel.DeteccionesViewModel

@Composable
fun DeteccionesScreen(vm:DeteccionesViewModel){
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)){
                HeadDetecciones(vm){
                    vm.setImagenUri(it)
                }
                BodyDetecciones(vm)
                BottomDetecciones(vm)
            }
        }
}


@Composable
fun HeadDetecciones(vm: DeteccionesViewModel, onSelectImage: (Uri) -> Unit) {
    Column (verticalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Text("Text Detector", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Row(){
            val context = LocalContext.current
            //

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

                         onSelectImage(uri )
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
        }
    }
}

@Composable
fun BodyDetecciones(vm: DeteccionesViewModel) {
    val uri by vm.imagenUri.observeAsState()
    AsyncImage(
        model = uri,
        contentDescription = "Imagen",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
    val bitmap by vm.bitmap.observeAsState()
    if (bitmap != null) {
        processImageText(vm,bitmap!!)
    }

}

@Composable
fun BottomDetecciones(vm: DeteccionesViewModel) {
    val text by vm.text.observeAsState()
    val scrollState = rememberScrollState()
    Column (horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)){
        Text(text?:"", fontSize = 15.sp)
        Row(horizontalArrangement = Arrangement.End,modifier = Modifier.fillMaxWidth().padding(5.dp)){
            text?.takeIf { it.isNotEmpty() }?.let {
                CopyText(it)
                Spacer(modifier = Modifier.width(10.dp))
                ShareText(it)
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}

fun processImageText(vm: DeteccionesViewModel,bitmap: Bitmap){
    val recognizer = com.google.mlkit.vision.text.TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromBitmap(bitmap,0)
    val result = recognizer.process(image)
        .addOnSuccessListener { visionText ->
            Log.i("TextRecognition",visionText.text)
            vm.setText(visionText.text)
        }
        .addOnFailureListener { e ->
            Log.e("TextRecognition",e.message.toString())
        }

}

@Composable
fun CopyText(textToShare: String) {
    val context = LocalContext.current
    Image(painter = painterResource(R.mipmap.copiar), contentDescription = "",modifier=Modifier.size(30.dp).clickable{

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Texto copiado", textToShare)
        clipboard.setPrimaryClip(clip)

    })
}

@Composable
fun ShareText(textToShare: String) {
    val context = LocalContext.current
    Image(painter = painterResource(R.mipmap.shared), contentDescription = "",modifier=Modifier.size(30.dp).clickable {

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Compartir texto")
        context.startActivity(shareIntent)
    })
}
