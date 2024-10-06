package hu.bme.aut.android.cityofguilds.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.feature.auth.UserDataCheatSheet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    fun signOutAndClearCache(){
        viewModelScope.launch{
            authService.signOut()
            UserDataCheatSheet.currentUser = null
        }
    }
}