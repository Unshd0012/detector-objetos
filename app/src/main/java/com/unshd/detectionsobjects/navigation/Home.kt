package com.unshd.detectionsobjects.navigation

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.viewmodel.HomeViewModel
import kotlinx.coroutines.delay


@Composable
fun HomeScreen( vm:HomeViewModel,onOpciones: () -> Unit ){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            Home(vm,onOpciones)
        }
    }
}
@Composable
fun Home(vm: HomeViewModel,onOpciones: () -> Unit ){
    Column (modifier = Modifier.fillMaxSize()){
        HeadHome()
        BodyHome(vm)
        BottomHome(onOpciones)

    }
}

@Composable fun HeadHome(){
    Row (verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth().padding(10.dp)){
        Image(painter = painterResource(R.mipmap.cerrar), contentDescription = "", modifier = Modifier.size(40.dp)
            .clickable {
                System.exit(0)
            })
        Spacer(modifier = Modifier.width(20.dp))
        Text("DETECTOR DE OBJETOS", fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}
@Composable fun BodyHome(vm : HomeViewModel){
    val textoBienvenida = vm.getTextoBienvenida()
    Column (verticalArrangement = Arrangement.Center,modifier=Modifier.fillMaxWidth()){
        Image(painter = painterResource(R.drawable.coverrobot), contentDescription = "",
            modifier = Modifier.size(400.dp))
        Text(text = textoBienvenida,
            fontSize = 30.sp, textAlign = TextAlign.Justify)
    }
}

@Composable fun BottomHome(onOpciones: () -> Unit) {
    Column (modifier=Modifier.padding(10.dp)){
        LaunchedEffect(Unit) {
            delay(10000)
          onOpciones()
        }
        Button(onClick = {onOpciones()}, modifier=Modifier.fillMaxWidth()) {
            Text("> Empezar")
        }
    }
}

