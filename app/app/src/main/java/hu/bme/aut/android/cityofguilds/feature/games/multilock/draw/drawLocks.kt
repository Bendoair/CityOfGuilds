package hu.bme.aut.android.cityofguilds.feature.games.multilock.draw

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.google.common.collect.ImmutableList
import hu.bme.aut.android.cityofguilds.feature.games.multilock.LockState

fun DrawScope.drawLocks(
    lockStates : List<LockState>,
    openLockIcon: ImageBitmap, // Passed as a parameter from a @Composable context
    closedLockIcon: ImageBitmap // Passed as a parameter from a @Composable context
) {
    
    lockStates.forEach {
        drawCircle(
            color = it.color,
            radius = it.radius,
            center = it.center
        )

        val icon = if(it.isHeld) openLockIcon else closedLockIcon
        val scalefactor = it.radius*2 / icon.height.toFloat()
        withTransform({
            scale(
                scale = scalefactor,
                pivot = Offset(it.center.x-it.radius, it.center.y-it.radius))
        }){
            drawImage(
                image = icon,
                topLeft = Offset(it.center.x-it.radius/2, it.center.y-it.radius))
        }

    }
}