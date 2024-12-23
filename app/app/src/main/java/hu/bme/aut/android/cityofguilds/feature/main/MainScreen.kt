package hu.bme.aut.android.cityofguilds.feature.main

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.feature.capture.CaptureScreen
import hu.bme.aut.android.cityofguilds.feature.devtools.DevToolsScreen
import hu.bme.aut.android.cityofguilds.feature.list.ListScreen
import hu.bme.aut.android.cityofguilds.feature.map.MapScreen
import hu.bme.aut.android.cityofguilds.navigation.ContentDestinations

import hu.bme.aut.android.cityofguilds.ui.common.GuildAppBar
import hu.bme.aut.android.cityofguilds.ui.util.TestTags


import hu.bme.aut.android.cityofguilds.R.string as StringResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen (
    logout: () -> Unit,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val customNavSuiteType = with(adaptiveInfo) {
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
    }
    var currentDestination by rememberSaveable { mutableStateOf(ContentDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            ContentDestinations.entries.forEach{
                when(it){
                    ContentDestinations.DEVTOOLS -> {
                        //Kicsit hack
                        if(mainScreenViewModel.currentUser?.isDeveloper == true){
                            item(
                                icon = {
                                    Icon(
                                        it.icon,
                                        contentDescription = stringResource(it.contentDescription)
                                    )
                                },
                                label = { Text(stringResource(it.label)) },
                                selected = it == currentDestination,
                                onClick = { currentDestination = it }
                            )
                        }
                        Log.i("DEVTOOLS", "Account is developer: ${mainScreenViewModel.currentUser?.isDeveloper}")
                    }



                    else -> item(
                            icon = {
                                Icon(
                                    it.icon,
                                    contentDescription = stringResource(it.contentDescription)
                                )
                            },
                            label = { Text(stringResource(it.label)) },
                            selected = it == currentDestination,
                            onClick = { currentDestination = it }
                        )
                }

            }
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag(TestTags.MAIN_SCREEN)
    ){
        when(currentDestination){
            ContentDestinations.HOME -> HomeScreen(logout = {
                //If we don't sign out via the Auth Service we might run into lots of problems
                mainScreenViewModel.signOutAndClearCache()
                logout()
            })
            ContentDestinations.MAP -> MapScreen()
            ContentDestinations.LIST -> ListScreen()
            ContentDestinations.CAPTURE ->
                CaptureScreen(){
                    //On dismissing the alert dialog, that you have to turn on NFC return to home screen
                    currentDestination = ContentDestinations.HOME
                    //onSuccessInGame,
                    //onFailInGame,
                }
            ContentDestinations.DEVTOOLS -> DevToolsScreen()
            else -> HomeScreen(logout = {
                //If we don't sign out via the Auth Service we might run into lots of problems
                mainScreenViewModel.signOutAndClearCache()
                logout()
            })

        }

    }




}

@Preview
@Composable
fun mainscreenpreview(){
    MainScreen(
        logout = {}
    )
        

}