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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unshd.detectionsobjects.R
import com.unshd.detectionsobjects.core.viewmodel.HomeViewModel


@Composable
fun HomeScreen( vm:HomeViewModel,onNavOpciones: (String) -> Unit ){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            Home()
        }
    }
}
@Composable
@Preview(showBackground = true)
fun Home(){
    Column (modifier = Modifier.fillMaxSize()){
        HeadHome()
        BodyHome()
        BottomHome()

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
@Composable fun BodyHome(){
    Column (verticalArrangement = Arrangement.Center,modifier=Modifier.fillMaxWidth()){
        Image(painter = painterResource(R.drawable.coverrobot), contentDescription = "",
            modifier = Modifier.size(400.dp))
        Text("Detecta objetos en vivo usando la camara de tu dispositvo o desde una imagen de tu galeria",
            fontSize = 30.sp, textAlign = TextAlign.Justify)
    }
}

@Composable fun BottomHome(){
    Column (modifier=Modifier.padding(10.dp)){
        Button(onClick = {}, modifier=Modifier.fillMaxWidth()) {
            Text("> Empezar")
        }
    }
}

