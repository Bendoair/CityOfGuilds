package hu.bme.aut.android.cityofguilds.feature.capture

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.cityofguilds.R


@SuppressLint("ResourceAsColor")
@Composable
fun CaptureScreen (
    viewModel: NfcHandlerViewModel = hiltViewModel(),
    onDismiss:()-> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold (
        modifier = Modifier.fillMaxSize()
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
                            viewModel.captureButtonOnClick()
                        }) {
                            Text(text = stringResource(id = R.string.take_guild))
                        }
                    }
                }
            }

        }

    }
}