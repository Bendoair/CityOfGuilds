package hu.bme.aut.android.cityofguilds.feature.devtools

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.feature.capture.NfcHandlerViewModel
import hu.bme.aut.android.cityofguilds.feature.capture.NfcState
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

@Composable
fun DevToolsScreen(
    nfcViewModel: NfcHandlerViewModel = hiltViewModel(),
) {
    var isToggled by remember { mutableStateOf(false) }  // Toggle state
    var showDialog by remember { mutableStateOf(false) }  // AlertDialog visibility state
    var guildName by remember { mutableStateOf("") }
    //put base coordinates somewhere in budapest
    var coordX by remember{ mutableStateOf(47.497913f) }
    var coordY by remember{ mutableStateOf(19.040236f) }


    var isLocationFresh by remember { mutableStateOf(false) }

    var isLocationGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isLocationGranted = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Location permission is denied", Toast.LENGTH_SHORT).show()
        }
    }


    fun updateLocation() {
        // Check if permission is already granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            isLocationGranted = true
            Log.i("Location","We got here, somehow")

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location: Location? ->
                    if(location != null){
                        isLocationFresh = true
                        coordX = location.latitude.toFloat()
                        coordY = location.longitude.toFloat()
                    }else{
                        Log.e("Location", "Got null Location for some 1 reason")
                    }

                }
                .addOnFailureListener{
                    Log.e("Location", "Failed to get Location :((")
                }
            Log.i("Location","We got here tooooooo, somehow")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if(location != null){
                        isLocationFresh = true
                        coordX = location.latitude.toFloat()
                        coordY = location.longitude.toFloat()
                    }else{
                        Log.e("Location", "Got null Location for some 2 reason")
                    }

                }
                .addOnFailureListener{
                    Log.e("Location", "Failed to get Location :((")
                }
            Log.i("Location","We got here THREEEEEEEEEE, somehow")

        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        // Toggle (Switch)
        Switch(
            checked = isToggled,
            onCheckedChange = {
                isToggled = it
                if(isToggled){
                    nfcViewModel.changeMode(NfcState.NfcModes.WRITE)
                }else{
                    nfcViewModel.changeMode(NfcState.NfcModes.READ)
                }
            }
        )

        // Button that triggers the alert dialog
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Edit Point Data to be Written")
        }

        // Alert Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            isLocationFresh = false
                            nfcViewModel.updatePointInfo(
                                Point(
                                    guildname = guildName,
                                    coordinateX = coordX,
                                    coordinateY = coordY,
                                    captureDate = java.time.LocalDateTime.now().toKotlinLocalDateTime() //Durva barbarizmus
                                )
                            )



                        },
                        enabled = isLocationFresh
                    ) {
                        Text("Set Point Data")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = {
                    Text(text = "Point Deteails")
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,

                    ) {
                        TextField(
                            value = guildName,
                            onValueChange = {guildName = it},
                            label = { Text("New Guild's Name")},
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Current Latitude, Longitude:\n $coordX, $coordY",
                            textAlign = TextAlign.Center
                        )
                        Row (
                            modifier = Modifier.fillMaxWidth()
                        ){
                            TextField(
                                value = coordX.toString(),
                                onValueChange = {coordX = it.toFloat()},
                                label = { Text("CoordX")},
                                modifier = Modifier.weight(1f),

                            )
                            TextField(
                                value = coordY.toString(),
                                onValueChange = {coordY = it.toFloat()},
                                label = { Text("CoordY")},
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Button(
                            shape = CircleShape,
                            onClick = {
                                //TODO fix current location via fusedLocationProvider
                                //isLocationFresh = true
                                updateLocation()
                            },
                        ) { Text("Update Current Location")}
                    }
                }
            )
        }
    }

}

@Composable
@Preview
fun previewdevtoolscreen(){
    DevToolsScreen()
}


