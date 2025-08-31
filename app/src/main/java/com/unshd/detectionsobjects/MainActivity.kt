package com.unshd.detectionsobjects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unshd.detectionsobjects.core.viewmodel.DeteccionesViewModel
import com.unshd.detectionsobjects.core.viewmodel.EtiquetadosViewModel
import com.unshd.detectionsobjects.core.viewmodel.HistorialDeteccionesViewModel
import com.unshd.detectionsobjects.core.viewmodel.HistorialEtiquetadoViewModel
import com.unshd.detectionsobjects.core.viewmodel.HomeViewModel
import com.unshd.detectionsobjects.core.viewmodel.LiveCameraViewModel
import com.unshd.detectionsobjects.core.viewmodel.OpcionesViewModel
import com.unshd.detectionsobjects.core.viewmodel.PermisosViewModel
import com.unshd.detectionsobjects.navigation.Detecciones
import com.unshd.detectionsobjects.navigation.DeteccionesScreen
import com.unshd.detectionsobjects.navigation.Etiquetados
import com.unshd.detectionsobjects.navigation.EtiquetadosScreen
import com.unshd.detectionsobjects.navigation.HistorialDetecciones
import com.unshd.detectionsobjects.navigation.HistorialDeteccionesScreen
import com.unshd.detectionsobjects.navigation.HistorialEtiquetado
import com.unshd.detectionsobjects.navigation.HistorialEtiquetadoScreen
import com.unshd.detectionsobjects.navigation.Home
import com.unshd.detectionsobjects.navigation.HomeScreen
import com.unshd.detectionsobjects.navigation.LiveCamera
import com.unshd.detectionsobjects.navigation.LiveCameraScreen
import com.unshd.detectionsobjects.navigation.Opciones
import com.unshd.detectionsobjects.navigation.OpcionesScreen
import com.unshd.detectionsobjects.navigation.Permisos
import com.unshd.detectionsobjects.navigation.PermisosScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = Permisos) {
                composable<Permisos> {
                    val vm: PermisosViewModel = hiltViewModel()
                    PermisosScreen(
                        vm,
                        onPermisosOk = { navController.navigate(Home) }
                    )
                }
                composable<Home> {
                    val vm: HomeViewModel = hiltViewModel()
                    HomeScreen(vm) { navController.navigate(Opciones) }

                }
                composable<Opciones> {
                    val vm: OpcionesViewModel = hiltViewModel()
                    OpcionesScreen(vm) { destino ->
                        navController.navigate(destino)  }
                }
                composable<HistorialDetecciones> {
                    val vm: HistorialDeteccionesViewModel = hiltViewModel()
                    HistorialDeteccionesScreen(vm, navController)
                }
                composable<HistorialEtiquetado> {
                    val vm: HistorialEtiquetadoViewModel = hiltViewModel()
                    HistorialEtiquetadoScreen(vm,navController)
                }
                composable<LiveCamera> {
                    val vm: LiveCameraViewModel = hiltViewModel()
                    LiveCameraScreen(vm)
                }
                composable<Detecciones> {
                    val vm: DeteccionesViewModel = hiltViewModel()
                    DeteccionesScreen(vm)
                }
                composable<Etiquetados> {
                    val vm: EtiquetadosViewModel = hiltViewModel()
                    EtiquetadosScreen(vm)
                }
            }
        }
        }
    }




