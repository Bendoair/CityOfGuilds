package hu.bme.aut.android.cityofguilds.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.User
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
import hu.bme.aut.android.cityofguilds.ui.model.CapturePointUi
import hu.bme.aut.android.cityofguilds.ui.model.asCapturePointUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val pointOperations:GuildUseCases,
    private val repository:PointService,
    private val auth:AuthService
):ViewModel(){
    private val _state = MutableStateFlow(PointsState())
    val state = _state.asStateFlow()
    init {
        load()

    }





    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val points = pointOperations.listOwnUseCase.invoke(repository).map { it.asCapturePointUi() }
                    val user = auth.currentUser.first()?:User(id = "You")
                    val leaderboardUsers = repository.getLeaderboardUsers()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            points = points,
                            leaderboardUsers = leaderboardUsers,
                            currentUser = user
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
    val points: List<CapturePointUi> = emptyList(),
    val currentUser:User? = null,
    val leaderboardUsers:List<User> = emptyList(),
)