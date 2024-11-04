package hu.bme.aut.android.cityofguilds.navigation

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.components.ServiceComponent
import hu.bme.aut.android.cityofguilds.GuildApplication
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.di.ServiceModule
import hu.bme.aut.android.cityofguilds.feature.auth.loading.LoadScreen
import hu.bme.aut.android.cityofguilds.feature.auth.login.LoginScreen
import hu.bme.aut.android.cityofguilds.feature.auth.register.RegisterScreen
import hu.bme.aut.android.cityofguilds.feature.capture.CaptureScreen
import hu.bme.aut.android.cityofguilds.feature.list.ListScreen
import hu.bme.aut.android.cityofguilds.feature.main.MainScreen
import hu.bme.aut.android.cityofguilds.feature.map.MapScreen
import hu.bme.aut.android.cityofguilds.navigation.Screen.*
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Load.route
    ) {
        composable(Screen.Load.route){
            LoadScreen(
                onNoUserFound = { navController.navigate(Screen.Login.route) },
                onUserFound = {navController.navigate(Screen.Main.route)}
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Screen.Main.route)
                    Log.i("Login", "Login button pressed")
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route){
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = Screen.Login.route,
                        inclusive = true
                    )
                    navController.navigate(Screen.Login.route)
                },
                onSuccess = {
                    navController.navigate(Screen.Main.route)
                }
            )
        }
        composable(Screen.Main.route){
            MainScreen(
                logout = {

                    navController.popBackStack(
                        route = Screen.Login.route,
                        inclusive = true
                    )

                    navController.navigate(Screen.Login.route)
                }
            )
        }

    }
}