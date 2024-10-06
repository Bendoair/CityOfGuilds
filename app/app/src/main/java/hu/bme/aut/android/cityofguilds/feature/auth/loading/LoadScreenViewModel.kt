package hu.bme.aut.android.cityofguilds.feature.auth.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitPointServiceImpl
import hu.bme.aut.android.cityofguilds.feature.auth.UserDataCheatSheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadScreenViewModel @Inject constructor(
    private val authService: AuthService,
    private val pointService: PointService,
) :ViewModel(){

    private val _booleanResult = MutableStateFlow<Boolean?>(null) // Initial value is false
    val booleanResult: StateFlow<Boolean?> = _booleanResult

    fun UserAlreadyLoggedIn(){
        viewModelScope.launch {
            if (authService.hasUser){
                UserDataCheatSheet.currentUser = pointService.getUser(authService.currentUserId?:"")
                _booleanResult.value = true
            }
        }
    }
}