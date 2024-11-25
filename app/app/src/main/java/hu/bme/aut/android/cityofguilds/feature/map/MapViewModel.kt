package hu.bme.aut.android.cityofguilds.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
import hu.bme.aut.android.cityofguilds.feature.list.PointsState
import hu.bme.aut.android.cityofguilds.ui.model.CapturePointUi
import hu.bme.aut.android.cityofguilds.ui.model.asCapturePointUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val pointOperations: GuildUseCases,
    private val repository: PointService
): ViewModel(){

    private val _state = MutableStateFlow(MapAndPointsState())
    val state = _state.asStateFlow()


    init {
        loadPoints()
    }
    fun getCameraPositionUpdate(points:List<CapturePointUi>): LatLng {
        val boundsBuilder = LatLngBounds.Builder()
        points.forEach { point -> boundsBuilder.include(LatLng(point.coordinateX.toDouble(), point.coordinateY.toDouble())) }
        val bounds = boundsBuilder.build()
        val center = bounds.center

        return center
    }

    private fun loadPoints() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                try {
                    val points = pointOperations.listAllUseCase.invoke(repository)
                        .map { it.asCapturePointUi() }
                    if (points.isEmpty()) {
                        throw Exception("There are no points on the map!")
                    }
                    val update = getCameraPositionUpdate(points)
                    _state.value.cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(update, 10f)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            points = points,
                        )
                    }

                } catch (e:Exception){
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e
                        )
                    }
                }
            }
        }
    }
}

data class MapAndPointsState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val points: List<CapturePointUi> = emptyList(),
    var cameraPositionState: CameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(LatLng(47.593631744384766, 19.0290851), 10f))
)