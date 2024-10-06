package hu.bme.aut.android.cityofguilds.feature.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.GuildApplication
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.usecases.IsEmailValidUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.PasswordsMatchUseCase
import hu.bme.aut.android.cityofguilds.feature.auth.UserDataCheatSheet
import hu.bme.aut.android.cityofguilds.ui.model.UiText
import hu.bme.aut.android.cityofguilds.ui.model.toUiText
import hu.bme.aut.android.cityofguilds.ui.util.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import hu.bme.aut.android.cityofguilds.R.string as StringResources


@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val pointService: PointService,
    private val authService: AuthService,
    private val isEmailValid: IsEmailValidUseCase,
    private val passwordsMatch: PasswordsMatchUseCase
): ViewModel() {
    
    private val _state = MutableStateFlow(RegisterUserState())
    val state = _state.asStateFlow()

    private val email get() = state.value.email

    private val password get() = state.value.password

    private val confirmPassword get() = state.value.confirmPassword

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: RegisterUserEvent) {
        when(event) {
            is RegisterUserEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }
            is RegisterUserEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(password = newPassword) }
            }
            is RegisterUserEvent.ConfirmPasswordChanged -> {
                val newConfirmPassword = event.password.trim()
                _state.update { it.copy(confirmPassword = newConfirmPassword) }
            }
            RegisterUserEvent.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }
            RegisterUserEvent.ConfirmPasswordVisibilityChanged -> {
                _state.update { it.copy(confirmPasswordVisibility = !state.value.confirmPasswordVisibility) }
            }
            RegisterUserEvent.SignUp -> {
                onSignUp()
            }
        }
    }

    private fun onSignUp() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(StringResources.some_error_message))
                    )
                } else {
                    if (!passwordsMatch(password,confirmPassword) && password.isNotBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(StringResources.some_error_message))
                        )
                    } else {
                        authService.signUp(email, password)
                        if(authService.hasUser){
                            pointService.addNewUser(authService.currentUserId!!)

                        }

                        UserDataCheatSheet.currentUser = pointService.getUser(authService.currentUserId?:"")
                        Log.i("UserCheatSheet", "Curr user: ${UserDataCheatSheet.currentUser}")

                        _uiEvent.send(UiEvent.Success)
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    /*
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val authService = GuildApplication.authService
                val isEmailValidUseCase = IsEmailValidUseCase()
                val passwordsMatchUseCase = PasswordsMatchUseCase()
                RegisterUserViewModel(
                    authService,
                    isEmailValidUseCase,
                    passwordsMatchUseCase
                )
            }
        }
    }
    */

}


data class RegisterUserState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisibility: Boolean = false,
    val confirmPasswordVisibility: Boolean = false
)

sealed class RegisterUserEvent {
    data class EmailChanged(val email: String): RegisterUserEvent()
    data class PasswordChanged(val password: String): RegisterUserEvent()
    data class ConfirmPasswordChanged(val password: String): RegisterUserEvent()
    object PasswordVisibilityChanged: RegisterUserEvent()
    object ConfirmPasswordVisibilityChanged: RegisterUserEvent()
    object SignUp: RegisterUserEvent()
}