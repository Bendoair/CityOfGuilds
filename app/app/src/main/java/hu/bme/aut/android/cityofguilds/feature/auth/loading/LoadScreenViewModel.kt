package hu.bme.aut.android.cityofguilds.feature.auth.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadScreenViewModel @Inject constructor(
    private val authService: AuthService,
) :ViewModel(){

    private val _booleanResult = MutableStateFlow<Boolean?>(null) // Initial value is false
    val booleanResult: StateFlow<Boolean?> = _booleanResult

    fun userAlreadyLoggedIn(){
        viewModelScope.launch {
            authService.tryGetTokenFromStore()
            delay(200)
            _booleanResult.value = authService.hasUser
        }
    }
}