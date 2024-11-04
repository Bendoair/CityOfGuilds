package hu.bme.aut.android.cityofguilds.feature.map

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.ui.model.CapturePointListItem
import hu.bme.aut.android.cityofguilds.ui.model.toUiText
import hu.bme.aut.android.cityofguilds.ui.util.TestTags


@SuppressLint("ResourceAsColor")
@Composable
fun MapScreen(
    viewModel:MapViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold (
        modifier = Modifier.fillMaxSize().testTag(TestTags.MAP_SCREEN)
    ){padding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .paint(
                    painter = painterResource(id = R.drawable.parchment_background),
                    contentScale = ContentScale.Crop
                )

        ){
            if(state.isLoading){
                CircularProgressIndicator(
                    color = Color(R.color.grey_brown_parchment),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }else if(state.isError){
                Text(
                    text = state.error?.toUiText()?.asString(context)
                        ?: stringResource(id = R.string.some_error_message)
                )
            }else{
                if(state.points.isEmpty()){
                    Text(text = stringResource(id = R.string.text_empty_points_list),
                        modifier = Modifier
                            .align(Alignment.Center))
                }else{
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        cameraPositionState = state.cameraPositionState,
                        properties = MapProperties(
                            mapType = MapType.NORMAL,
                            isTrafficEnabled = false,
                            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle)
                        )
                    ){
                        state.points.forEach { point ->
                            Marker(
                                state = MarkerState(position = LatLng(point.coordinateX.toDouble(),point.coordinateY.toDouble())),
                                title = point.guildname,
                                draggable = false,

                            )
                        }
                    }
                }
            }
        }
    }
}

