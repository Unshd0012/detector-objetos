package com.unshd.detectionsobjects.navigation

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.db.label.ImageLabelEntity
import com.unshd.detectionsobjects.core.viewmodel.HistorialEtiquetadoViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistorialEtiquetadoScreen(vm:HistorialEtiquetadoViewModel, navController: NavController) {


    val showBottomBar by vm.showBottomBar.observeAsState(false)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)){

            HeadHistorialEtiquetados()
            BodyHistorialEtiquetado(vm)

            if (showBottomBar) {
                BottomHistorialEtiquetado(navController)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HeadHistorialEtiquetados() {
    Column (verticalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Text("HISTORIAL DE ETIQUETAS", fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BodyHistorialEtiquetado(vm:HistorialEtiquetadoViewModel) {
    var itemSeleccionado by remember { mutableStateOf<ImageLabelEntity?>(null) }
    val lista by vm.listaEtiquetados.collectAsState()
    val listaFiltrada = lista?.distinctBy { it.sessionId }
    var texto by remember { mutableStateOf("") }
    val context = LocalContext.current
    if (itemSeleccionado != null) {
        DetalleEtiquetaView(vm,
            label = itemSeleccionado!!,
            onCerrar = { itemSeleccionado = null }
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



        val listaSize = listaFiltrada?.size ?: 0
        if (listaSize == 0) {
            vm.setShowBottomBar(true)
        }else{
            vm.setShowBottomBar(false)
        }
        // Lista de elementos
        Column (modifier = Modifier.fillMaxWidth()) {
            LazyColumn {
                items(listaSize){ index ->
                    val label = listaFiltrada!!.get(index)
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
                                AsyncImage(model = label.uri.toUri(),
                                    contentDescription = "",
                                    modifier = Modifier.size(50.dp))

                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(text = label.title, fontSize = 21.sp)
                                    val instant = Instant.ofEpochMilli(label.timestamp)
                                    val zonaHoraria = ZoneId.systemDefault() // Usará la zona del dispositivo
                                    val fechaHora = instant.atZone(zonaHoraria)
                                    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a",
                                        Locale("es", "MX")
                                    )
                                    val fechaFormateada = fechaHora.format(formatter)
                                    Text(text = fechaFormateada, fontSize = 15.sp)
                                    Text(text = label.origen, fontSize = 15.sp)
                                }
                                Spacer(Modifier.width(5.dp))
                                Text(text = label.titlePorcentaje+"%", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(5.dp))
                                var expanded by remember { mutableStateOf(false) }

                                Box {
                                    Icon(
                                        bitmap = ImageBitmap.imageResource(id = R.mipmap.menudetecciones),
                                        contentDescription = "",
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { expanded = true }
                                    )

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Ver detalles") },
                                            onClick = {
                                                expanded = false
                                                itemSeleccionado = label
                                           }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Eliminar") },
                                            onClick = {
                                                expanded = false
                                                Toast.makeText(context, "Eliminar ${label.title}", Toast.LENGTH_SHORT).show()
                                            vm.deleteEtiquetado(label)
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
fun BottomHistorialEtiquetado(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(bitmap = ImageBitmap.imageResource(id = R.drawable.robotetiquetas), tint = Color.Unspecified, contentDescription = "",
                modifier = Modifier.size(150.dp))
            Column (modifier = Modifier){
                Text(text = "Aun no tienes imagenes etiquetadas", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Text(text = "Seleccionar Imagen", fontSize = 15.sp,modifier = Modifier.clickable{
                    navController.navigate(Etiquetados)
                })



            }


        }

    }
}

@Composable
fun DetalleEtiquetaView(vm: HistorialEtiquetadoViewModel,label: ImageLabelEntity, onCerrar: () -> Unit) {

    var etiquetado by remember { mutableStateOf<List<ImageLabelEntity>?>(null) }

    LaunchedEffect(label.sessionId) {
        etiquetado = vm.getEtiquetado(label.sessionId)
    }

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
                model = label.uri.toUri(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)

                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(Modifier.height(16.dp))


                etiquetado?.forEach { eti ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Título: ${eti.label}")
                            Text("Origen: ${eti.origen}")
                            val fecha = Instant.ofEpochMilli(eti.timestamp)
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a", Locale("es", "MX")))
                            Text("Fecha: $fecha")
                            Text("Confianza: ${eti.confidence}%")
                        }
                    }
                    Spacer(Modifier.height(10.dp))
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
