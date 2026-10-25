package cl.duoc.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.myapplication.navigation.appNavigation
import cl.duoc.myapplication.ui.theme.NotariaxmovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotariaxmovilTheme {
                appNavigation()
            }
        }
    }
}