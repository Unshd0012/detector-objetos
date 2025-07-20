package com.unshd.detectionsobjects.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.unshd.detectionsobjects.core.viewmodel.OpcionesViewModel

@Composable
fun OpcionesScreen( vm:OpcionesViewModel,onOpcionSeleccionada: (String) -> Unit) {

    vm.loadOpciones()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            HeadOpciones()
            BodyOpciones(vm)
            BottomOpciones()
        }
    }
}

@Composable
fun BodyOpciones(vm: OpcionesViewModel) {
    val context = LocalContext.current
    val lista by vm.listaOpciones.observeAsState()
    val listaSize = lista?.size ?: 0
Column (modifier=Modifier.fillMaxWidth()){
    LazyColumn {
        items(listaSize){ index ->
            val opcion = lista!![index]
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxSize().padding(5.dp)
            ){
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp).clickable {
                    },
                    colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Icon(bitmap = ImageBitmap.imageResource(id = opcion.imagen),
                            tint = Color.Unspecified,contentDescription = "",
                            modifier = Modifier.size(50.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(text = opcion.nombre, fontSize = 18.sp)
                        Spacer(Modifier.width(5.dp))
                        Icon(bitmap = ImageBitmap.imageResource(id = R.mipmap.informacion),contentDescription = "",
                            tint = Color.Unspecified, modifier = Modifier.size(20.dp).clickable {
                                Toast.makeText(context,opcion.descripcion, Toast.LENGTH_LONG).show()
                            })


                    }
                }
            }
        }
    }


}
}

@Composable
fun BottomOpciones() {
    Column (horizontalAlignment = Alignment.CenterHorizontally,modifier=Modifier.padding(10.dp).fillMaxWidth()) {
        Image(painter = painterResource(R.drawable.opcionesrobot), contentDescription = "",)
    }
}

@Composable
    @Preview(showBackground = true)
    fun HeadOpciones(){
        val activity = LocalContext.current as? Activity
         Column (verticalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth().padding(10.dp)){
            Text("OPCIONES DISPONIBLES", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
    }
