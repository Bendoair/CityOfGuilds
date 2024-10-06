package hu.bme.aut.android.cityofguilds.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
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
class ListViewModel @Inject constructor(
    private val pointOperations:GuildUseCases,
    private val repository:PointService
):ViewModel(){
    private val _state = MutableStateFlow(PointsState())
    val state = _state.asStateFlow()
    init {
        loadPoints()
    }

    private fun loadPoints() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val points = pointOperations.listOwnUseCase.invoke(repository).map { it.asCapturePointUi() }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            points = points
                        )
                    }
                }
            }catch (e:Exception){
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

data class PointsState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val points: List<CapturePointUi> = emptyList()
)