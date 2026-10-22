package cl.duoc.myapplication.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*


@Composable

fun Login(){
    var username by remember { mutableStateOf( value = "") }
    var password by remember { mutableStateOf( value = "")}
    var modal by remember {mutableStateOf( value = false)}


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(text = "Username/Correo")

        OutlinedTextField(
            value = username,
            onValueChange = {nuevoTexto -> username = nuevoTexto},
            modifier = Modifier.fillMaxWidth()

        )

    }

}