package cl.duoc.myapplication.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.myapplication.model.TipoFirma
import cl.duoc.myapplication.ui.screen.AdvancedSignatureScreen
import cl.duoc.myapplication.ui.screen.SimpleSignatureScreen
import cl.duoc.myapplication.ui.screen.LoginScreen
import cl.duoc.myapplication.ui.screen.NewProcedureScreen
import cl.duoc.myapplication.ui.screen.OldStatusScreen
import cl.duoc.myapplication.ui.screen.RegisterScreen
import cl.duoc.myapplication.ui.screen.StatusScreen
import cl.duoc.myapplication.ui.screen.mainMenuScreen
import cl.duoc.myapplication.ui.screen.splashScreen
import cl.duoc.myapplication.viewmodel.NewProcedureViewModel

/**
 * Rutas de todas las pantallas de la app.
 */
object Routes {
    const val SPLASH = "splash"
    const val MAIN_MENU = "main_menu"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val NEW_PROCEDURE = "new_procedure"
    const val STATUS = "status"
    const val OLD_STATUS = "old_status"
    const val SIMPLE_SIGNATURE = "simple_signature"
    const val ADVANCED_SIGNATURE = "advanced_signature"
}

private const val DURACION_TRANSICION = 300

/**
 * Grafo de navegación de la app. Aquí se decide a qué pantalla va cada botón;
 * las pantallas solo avisan que se tocó algo mediante sus callbacks.
 */
@Composable
fun appNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(DURACION_TRANSICION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(DURACION_TRANSICION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(DURACION_TRANSICION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(DURACION_TRANSICION)
            )
        }
    ) {
        composable(Routes.SPLASH) {
            splashScreen(
                onTimeout = {
                    // Se saca el splash del historial para no volver a él con "atrás"
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN_MENU) {
            mainMenuScreen(
                onNavigateToLoginScreen = { navController.navigate(Routes.LOGIN) },
                onNavigateToNewProcedureScreen = { navController.navigate(Routes.NEW_PROCEDURE) },
                onNavigateToStatusScreen = { navController.navigate(Routes.STATUS) },
                onNavigateToOldStatusScreen = { navController.navigate(Routes.OLD_STATUS) }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToRegisterScreen = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { navController.popBackStack(Routes.MAIN_MENU, inclusive = false) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.popBackStack() }
            )
        }

        composable(Routes.NEW_PROCEDURE) {
            NewProcedureScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSignature = { tipoFirma ->
                    val signatureRoute = if (tipoFirma == TipoFirma.AVANZADA) {
                        Routes.ADVANCED_SIGNATURE
                    } else {
                        Routes.SIMPLE_SIGNATURE
                    }
                    navController.navigate(signatureRoute)
                }
            )
        }

        composable(Routes.SIMPLE_SIGNATURE) { backStackEntry ->
            val procedureViewModel: NewProcedureViewModel = viewModel(
                viewModelStoreOwner = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.NEW_PROCEDURE)
                }
            )
            SimpleSignatureScreen(
                procedureViewModel = procedureViewModel,
                onNavigateBack = { navController.popBackStack() },
                onProcedureCreated = { navController.popBackStack(Routes.MAIN_MENU, inclusive = false) }
            )
        }

        composable(Routes.ADVANCED_SIGNATURE) { backStackEntry ->
            val procedureViewModel: NewProcedureViewModel = viewModel(
                viewModelStoreOwner = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.NEW_PROCEDURE)
                }
            )
            AdvancedSignatureScreen(
                procedureViewModel = procedureViewModel,
                onNavigateBack = { navController.popBackStack() },
                onProcedureCreated = { navController.popBackStack(Routes.MAIN_MENU, inclusive = false) }
            )
        }

        composable(Routes.STATUS) {
            StatusScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.OLD_STATUS) {
            OldStatusScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}