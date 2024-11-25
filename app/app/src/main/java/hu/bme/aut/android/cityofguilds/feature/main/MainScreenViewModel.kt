package hu.bme.aut.android.cityofguilds.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val currentUser get() = runBlocking { authService.currentUser.first() }

    fun signOutAndClearCache(){
        viewModelScope.launch{
            authService.signOut()
        }
    }

}