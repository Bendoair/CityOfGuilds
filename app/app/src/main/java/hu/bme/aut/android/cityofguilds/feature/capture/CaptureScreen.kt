package hu.bme.aut.android.cityofguilds.feature.capture

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.feature.games.multilock.MultilockGameScreen
import hu.bme.aut.android.cityofguilds.ui.util.TestTags


@SuppressLint("ResourceAsColor")
@Composable
fun CaptureScreen (
    viewModel: NfcHandlerViewModel = hiltViewModel(),
    onDismiss:()-> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var gameStarted by remember { mutableStateOf(false) }


    AnimatedVisibility (
        visible = gameStarted,
        enter = scaleIn(animationSpec = tween(durationMillis = 1000)),
        exit = scaleOut(animationSpec = tween(durationMillis = 1500)),
        modifier = Modifier.testTag("GameStarted")
    ) {
        MultilockGameScreen(
            numberOfLocks = 4,
            context = context,
            onGameWon = {
                gameStarted = false
                viewModel.captureButtonOnClick()
                Log.i("Capture", "Game won, thing captured")
            },
            onDismiss = onDismiss,
        )
    }

    if(!gameStarted){
        Scaffold (
            modifier = Modifier
                        .fillMaxSize()
                        .testTag(TestTags.CAPTURE_SCREEN)
        ){padding->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ){
                viewModel.checkNfcStatus()
                if(!state.nfcEnabled){
                    AlertDialog(
                        onDismissRequest = { onDismiss() },
                        title = { Text(text = stringResource(id = R.string.NFC_disabled))},
                        text = { Text(text = stringResource(id = R.string.NFC_rationel))},
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.enableNFC(context)
                                }) {
                                Text(text = stringResource(id = R.string.enable_NFC))
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                viewModel.checkNfcStatus()
                            }) {
                                Text(text = stringResource(id = R.string.enabled_NFC))
                            }
                        })
                }else{
                    if(!state.newInfo){
                        Text(
                            text = stringResource(id = R.string.no_guild_detected),
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }else{
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ){
                            Text(text = stringResource(id = R.string.guild_ready_to_take))
                            HorizontalDivider()
                            Button(onClick = {
                                gameStarted = true
                            }) {
                                Text(text = stringResource(id = R.string.take_guild))
                            }
                        }
                    }
                }

            }

        }
    }

}