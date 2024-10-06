package hu.bme.aut.android.cityofguilds.ui.util

import android.content.Context

sealed class CaptureEvent {
    data class StartLooking(val context: Context):CaptureEvent()
    data class FoundTag(val context: Context, val id:String):CaptureEvent()
}