package cl.duoc.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.myapplication.ui.theme.NotariaxmovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotariaxmovilTheme {
                var nombre by remember {mutableStateOf("")}
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Formulario")
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(text = "Debes llenar tus datos")
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {nombre = it},
                            label = {Text("ingresa nombre")}
                        )
                        Spacer(modifier = Modifier.height(60.dp))
                        Button(onClick = {}) {
                            Text(text="Crear Usuario")
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Nombre ingresado: $nombre",
                            modifier = Modifier.background(Red)
                        )
                    }
                }
            }
        }
    }
}