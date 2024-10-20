package hu.bme.aut.android.cityofguilds.feature.games.multilock.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawBackground(
    backGroundSize: Size
) {
    drawRect(
        color = Color.DarkGray,
        topLeft = Offset(0f,0f),
        size = backGroundSize
    )
}