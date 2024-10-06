package hu.bme.aut.android.cityofguilds.ui.util

import hu.bme.aut.android.cityofguilds.ui.model.UiText

sealed class UiEvent {
    object Success: UiEvent()
    data class Failure(val message: UiText): UiEvent()
}