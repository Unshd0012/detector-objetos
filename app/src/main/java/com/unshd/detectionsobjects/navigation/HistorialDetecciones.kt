package com.unshd.detectionsobjects.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.db.label.ImageLabelEntity
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity
import com.unshd.detectionsobjects.core.viewmodel.HistorialDeteccionesViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

@Composable
fun HistorialDeteccionesScreen(vm:HistorialDeteccionesViewModel, navController: NavController){

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            HeadHistorialDetecciones()
            BodyHistorialDetecciones(vm)
            BottomHistorialDetecciones(vm,navController)
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
fun BodyHistorialDetecciones(vm:HistorialDeteccionesViewModel) {


    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current
    val lista by vm.listaDeteccionesText.collectAsState()
    var item by remember { mutableStateOf<TextBlockEntity?>(null) }

    if (item != null) {
        DetalleDeteccionView(vm,
            deteccion = item!!,
            onCerrar = { item = null }
        )
    }
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


        val listaSize = lista.size
        // Lista de elementos
        Column (modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(listaSize){ index ->
                    val opcion = lista[index]
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxSize().padding(5.dp)
                    ){
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Row(horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {

                                AsyncImage(model = opcion.imageUri,contentDescription = "",modifier = Modifier.size(80.dp))
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(text = opcion.title, fontSize = 16.sp)
                                    val instant = Instant.ofEpochMilli(opcion.timestamp)
                                    val zonaHoraria = ZoneId.systemDefault() // Usará la zona del dispositivo
                                    val fechaHora = instant.atZone(zonaHoraria)
                                    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a",
                                        Locale("es", "MX")
                                    )
                                    val fechaFormateada = fechaHora.format(formatter)
                                    Text(text = fechaFormateada, fontSize = 10.sp)
                                }
                                Spacer(Modifier.width(5.dp))
                                Text(text = opcion.fullText,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(2f))
                                Spacer(Modifier.width(5.dp))
                                var expandir by remember { mutableStateOf(false) }
                                Icon(bitmap = ImageBitmap.imageResource(id = R.mipmap.menudetecciones),contentDescription = "",
                                    tint = Color.Unspecified, modifier = Modifier.size(20.dp).clickable {
                                        expandir = true
                                    }.weight(1f))

                                Box() {
                                    DropdownMenu(
                                        expanded = expandir,
                                        onDismissRequest = { expandir = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Ver detalles") },
                                            onClick = {
                                                expandir = false
                                                item = opcion
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Eliminar") },
                                            onClick = {
                                                expandir = false
                                                Toast.makeText(
                                                    context,
                                                    "Eliminar ${opcion.title}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                vm.deleteDeteccion(opcion)
                                            }
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun BottomHistorialDetecciones(vm:HistorialDeteccionesViewModel,navController: NavController) {

    val lista by vm.listaDeteccionesText.collectAsState()
    if(lista.isEmpty()){
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

}


@Composable
fun DetalleDeteccionView(vm: HistorialDeteccionesViewModel,
                         deteccion: TextBlockEntity,
                         onCerrar: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x56FFFFFF)) // semi-transparente
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Detalle del etiquetado", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(8.dp))

            AsyncImage(
                model = deteccion.imageUri?.toUri(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)

                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.height(16.dp))


                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Título: ${deteccion.title}")
                        Text("Session ID: ${deteccion.sessionId}")
                        val fecha = Instant.ofEpochMilli(deteccion.timestamp)
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a", Locale("es", "MX")))
                        Text("Fecha: $fecha")

                    }
                }
                Spacer(Modifier.height(10.dp))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(deteccion.fullText)
                    Row(horizontalArrangement = Arrangement.End,modifier = Modifier.fillMaxWidth().padding(5.dp)){
                        deteccion.fullText?.takeIf { it.isNotEmpty() }?.let {
                            CopyText(it)
                            Spacer(modifier = Modifier.width(10.dp))
                            ShareText(it)
                        }
                    }
                }
            }


            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onCerrar,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cerrar")
            }
        }
    }

}
