package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import cl.duoc.myapplication.R
import cl.duoc.myapplication.ui.components.AppHeader
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.myapplication.ui.components.ButtonType
import cl.duoc.myapplication.ui.components.CustomButton
import cl.duoc.myapplication.viewmodel.MainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun mainMenuScreen (
    viewModel: MainViewModel = viewModel(),
    onNavigateToLoginScreen: () -> Unit = {},
    onNavigateToNewProcedureScreen: () -> Unit = {},
    onNavigateToStatusScreen: () -> Unit = {},
    onNavigateToOldStatusScreen: () -> Unit = {}
) {
    val uiState = viewModel.uiState
    var menuAbierto by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarSesion()
    }

    Scaffold(
        topBar = {
            AppHeader(
                "Digi Notas",
                onBackClick = null,
                actions = {
                    Box {
                        IconButton(onClick = {
                            if (uiState.estaLogueado) menuAbierto = true
                            else onNavigateToLoginScreen()
                        }){
                            Image(
                                painter = painterResource(id=R.drawable.icono_login),
                                contentDescription = "Perfil / Login",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        DropdownMenu(
                            expanded = menuAbierto,
                            onDismissRequest = { menuAbierto = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(uiState.nombreUsuario) },
                                onClick = {},
                                enabled = false
                            )
                            DropdownMenuItem(
                                text = { Text("Cerrar sesión") },
                                onClick = {
                                    menuAbierto = false
                                    viewModel.onLogoutClick()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id=R.drawable.logo_editado),
                contentDescription = "Logo NotariaX",
                modifier = Modifier.size(300.dp)
            )
            CustomButton(
                text = "Ingresar tramite",
                type = ButtonType.PRIMARY,
                onClick = onNavigateToNewProcedureScreen
            )
            CustomButton(
                text = "Ver estado de tramite",
                type = ButtonType.PRIMARY,
                onClick = onNavigateToStatusScreen
            )
            CustomButton(
                text = "Historial",
                type = ButtonType.PRIMARY,
                onClick = onNavigateToOldStatusScreen
            )
        }

    }
}

