package hu.bme.aut.android.cityofguilds.feature.auth.loading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.feature.list.ListViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadScreen (
    onNoUserFound:()->Unit,
    onUserFound:()->Unit,
    viewModel: LoadScreenViewModel = hiltViewModel()
) {
    val foundUser = viewModel.booleanResult.collectAsState()

    // State to control the button visibility after 5 seconds
    var showButton by remember { mutableStateOf(false) }

    // Infinite rotation state
    var rotationDegreesTarget by remember { mutableFloatStateOf(360f) }
    val rotationDegreesAnimate by animateFloatAsState(
        targetValue = rotationDegreesTarget,
        keyframes {
            durationMillis = 5000
            rotationDegreesTarget * 0.7f at 3500 using LinearEasing
            rotationDegreesTarget * 1f at 5000 using FastOutLinearInEasing

        }, label = "rotation"
    )

    LaunchedEffect(rotationDegreesAnimate) {
        if (rotationDegreesAnimate == rotationDegreesTarget) {
            // Reset the target value to 0 to create a looping effect
            rotationDegreesTarget = 0f
            // Set back to 360f after resetting to restart the cycle
            delay(10L) // Tiny delay to ensure smooth restart
            rotationDegreesTarget = 360f
        }
    }

    // LaunchedEffect to show the button after 5 seconds
    LaunchedEffect(Unit) {
        delay(5000L)
        showButton = true
    }

    if(foundUser.value == true){
        onUserFound()
    }


    // Layout for the composable
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,

    ) {
        //Try And find user
        viewModel.UserAlreadyLoggedIn()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Rotating CircularProgressIndicator around the logo
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(350.dp) // size of the progress circle
                        .rotate(rotationDegreesAnimate), // continuous rotation effect
                    strokeWidth = 8.dp
                )

                // Application logo in the center
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_while_load), // Change to your app's logo
                    contentDescription = "App Logo",
                    modifier = Modifier.size(300.dp) // Size of the logo
                )
            }

            // Text under the rotating circle
            Spacer(modifier = Modifier.height(24.dp)) // Space between circle and text
            Text(text = stringResource(R.string.loading_please_wait), style = MaterialTheme.typography.bodyLarge)

            // Button becomes visible after 5 seconds
            Spacer(modifier = Modifier.height(24.dp)) // Space between text and button
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn()

            ) {
                Button(
                    onClick = { onNoUserFound() },
                    shape = CircleShape
                ) {
                    Text(text = stringResource(R.string.continue_to_login))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingScreenWithButton() {
    LoadScreen(
        onUserFound = {},
        onNoUserFound = {}
    )
}