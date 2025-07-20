package com.unshd.detectionsobjects.navigation

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity
import com.unshd.detectionsobjects.core.viewmodel.HistorialDeteccionesViewModel
import java.util.UUID

@Composable
fun HistorialDeteccionesScreen(vm:HistorialDeteccionesViewModel, navController: NavController){

    /*vm.insertHistorialDetecciones(TextBlockEntity(0, UUID.randomUUID().toString(),"prueba",
        "Descripcion","prueba",System.currentTimeMillis()))*/
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            HeadHistorialDetecciones()
            BodyHistorialDetecciones()
            BottomHistorialDetecciones(navController)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HeadHistorialDetecciones() {
    Column (verticalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Text("HISTORIAL DE DETECCIONES", fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }

}

@Composable
@Preview(showBackground = true)
fun BodyHistorialDetecciones() {

    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current


    Column (modifier =Modifier.fillMaxWidth()) {
        Column (Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Buscar") },
                placeholder = { Text("Ingrese informacion") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Row (horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Todo")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Hoy")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Ayer")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Semana")
                }
            }
        }


        val listaSize = 5
        // Lista de elementos
        Column (modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(listaSize){ index ->
                    //val opcion = lista!![index]
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
                                Icon(bitmap = ImageBitmap.imageResource(id = R.mipmap.camara),
                                    tint = Color.Unspecified,contentDescription = "",
                                    modifier = Modifier.size(50.dp))
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(text = "Nombre", fontSize = 16.sp)
                                    Text(text = "19/07/2025 09:53 pm", fontSize = 10.sp)
                                }
                                Spacer(Modifier.width(5.dp))
                                Text(text = "Texto de prueba, este texto es de prueba y es el texto que se encuentra en la descripcion",
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(2f))
                                Spacer(Modifier.width(5.dp))
                                Icon(bitmap = ImageBitmap.imageResource(id = R.mipmap.menudetecciones),contentDescription = "",
                                    tint = Color.Unspecified, modifier = Modifier.size(20.dp).clickable {
                                        Toast.makeText(context,"opcion.descripcion", Toast.LENGTH_LONG).show()
                                    }.weight(1f))


                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomHistorialDetecciones(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()){
        Row (verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceBetween,){
            Icon(bitmap = ImageBitmap.imageResource(id = R.drawable.robothistorialdetecciones), tint = Color.Unspecified, contentDescription = "")
            Column (modifier = Modifier.weight(2f)){
                Text(text = "Aqui veras tus detecciones recientes", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text(text = "Detectar Ahora", fontSize = 15.sp)
            }
            Button(onClick = { navController.navigate(Detecciones) }) {
                Icon(bitmap = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add), tint = Color.Unspecified, contentDescription = "")

            }
        }

    }
}
