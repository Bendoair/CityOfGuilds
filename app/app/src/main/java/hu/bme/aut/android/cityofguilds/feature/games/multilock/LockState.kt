package hu.bme.aut.android.cityofguilds.feature.games.multilock

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

data class LockState(
    var center: Offset,
    var radius: Float,
    var color: Color,
    var isHeld: Boolean = false
){
    // Check if a touch point is inside the circle
    fun contains(point: Offset): Boolean {
        val distance = sqrt((point.x - center.x).pow(2) + (point.y - center.y).pow(2))
        return distance <= radius
    }

    // Toggle held state and change color accordingly
    fun toggleHeld(held: Boolean) {
        isHeld = held
    }

}
