package cl.duoc.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cl.duoc.myapplication.ui.screen.LoginScreen
import cl.duoc.myapplication.ui.screen.mainMenuScreen
import cl.duoc.myapplication.ui.screen.splashScreen
import cl.duoc.myapplication.ui.theme.NotariaxmovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotariaxmovilTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> splashScreen(
                        onTimeout = { currentScreen = "main_menu" }
                    )

                    "main_menu" -> mainMenuScreen(
                        onNavigateToLoginScreen = { currentScreen = "login" }
                    )

                    "login" -> LoginScreen()
                }
            }
        }
    }
}