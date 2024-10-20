package hu.bme.aut.android.cityofguilds.feature.games.multilock

import android.content.Context
import android.graphics.drawable.Icon
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.feature.games.multilock.draw.drawBackground
import hu.bme.aut.android.cityofguilds.feature.games.multilock.draw.drawLocks
import kotlin.random.Random

@Composable
fun MultilockGameScreen(
    numberOfLocks:Int = 0,
    @ApplicationContext context:Context,

    ) {

    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput("dragging") {
                detectDragGestures { change, dragAmount ->
                    pointerOffset += dragAmount

                }
            }
            .onSizeChanged {
                pointerOffset = Offset(it.width / 2f, it.height / 2f)
            }
            .drawWithContent {
                drawContent()
                // draws a fully black area with a small keyhole at pointerOffset that’ll show part of the UI.
                drawRect(
                    Brush.radialGradient(
                        listOf(Color.Transparent, Color.Black),
                        center = pointerOffset,
                        radius = 100.dp.toPx(),
                    )
                )
            }
    ) {
        // Your composables here
        gameContent(context, numberOfLocks)

    }




}

@Composable
fun gameContent(context: Context, numberOfLocks: Int){

    // State to hold the size of the canvas
    var canvasSize by remember { mutableStateOf(Offset(context.resources.displayMetrics.widthPixels.toFloat(),context.resources.displayMetrics.heightPixels.toFloat() )) }
    var locks by remember {
        mutableStateOf(
            generateLocks(context, numberOfLocks, canvasSize, 20.dp, 40.dp)
        )
    }

    var heldLocks by remember { mutableIntStateOf(0)}
    val closedLockBitmap = ImageBitmap.imageResource(R.drawable.closed_green_lock_icon)
    val openLockBitmap = ImageBitmap.imageResource(R.drawable.open_red_lock_icon)

    var loaded = false

    //Game content
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    // Track all pointers in this gesture (multi-touch)
                    val pointers = mutableMapOf<Long, Offset>()

                    // Process all pointer events (touch down, move, up)
                    while (true) {
                        Log.i("Locks", "Number of locks, held = ${locks.count { it.isHeld }}")
                        heldLocks = locks.count { it.isHeld }
                        val event = awaitPointerEvent()
                        event.changes.forEach { pointerInputChange ->
                            val pointerId = pointerInputChange.id.value
                            val currentPosition = pointerInputChange.position

                            // On touch down or move, update the position
                            if (pointerInputChange.pressed) {
                                pointers[pointerId] = currentPosition

                                // Highlight the circle that contains the current touch point
                                locks = locks.map {
                                    if (it.contains(currentPosition)) {
                                        it.apply { toggleHeld(true) }
                                    } else it
                                }

                                //DONT
                                // Consume the touch event to avoid further processing
                                //pointerInputChange.consume()
                            } else {
                                // On touch release, remove pointer from map and reset circles
                                pointers.remove(pointerId)

                                // Reset all circles that are no longer held
                                locks = locks.map { circle ->
                                    if (pointers.values.none { circle.contains(it) }) {
                                        circle.apply { toggleHeld(false) }
                                    } else circle
                                }
                            }

                        }

                        // Exit loop if no pointers are active
                        if (event.changes.none { it.pressed }) break
                    }


                }

            }

    ) {
        drawBackground(size)
        drawLocks(
            lockStates = locks,
            openLockIcon = openLockBitmap,
            closedLockIcon = closedLockBitmap)
        if(heldLocks >= numberOfLocks){
            Toast.makeText(context, "Oyy, you did it", Toast.LENGTH_SHORT).show()
        }



    }

}



fun generateLocks(context: Context,numberOfCircles: Int, canvasSize: Offset, minRadius: Dp, maxRadius: Dp): List<LockState> {
    Log.i("Locks","Generating locks ${numberOfCircles}, this many")
    val locks = mutableListOf<LockState>()

    val random = Random.Default
    for (i in 0 until numberOfCircles) {
        // Generate random radius within the specified range
        val radius = random.nextFloat() * (maxRadius.toPx(context) - minRadius.toPx(context)) + minRadius.toPx(context)

        // Generate random position ensuring the circle stays within canvas bounds
        val x = random.nextFloat() * (canvasSize.x - 2 * radius) + radius
        val y = random.nextFloat() * (canvasSize.y - 2 * radius) + radius


        // Add the generated circle to the list
        locks.add(
            LockState(
                center = Offset(x, y),
                radius = radius,
                color = Color.LightGray,
                isHeld = false
            )
        )
    }
    /*
    Debugging
    locks.add(
        LockState(
            center = Offset(0f, 0f),
            radius = 100f,
            color = Color.LightGray,
            isHeld = false
        )
    )
    locks.add(
        LockState(
            center = Offset(canvasSize.x, 0f),
            radius = 100f,
            color = Color.LightGray,
            isHeld = false
        )
    )

    locks.add(
        LockState(
            center = Offset(canvasSize.x/2, canvasSize.y/2),
            radius = 100f,
            color = Color.LightGray,
            isHeld = false
        )
    )
    locks.add(
        LockState(
            center = Offset(0f, canvasSize.y),
            radius = 100f,
            color = Color.LightGray,
            isHeld = false
        )
    )
    locks.add(
        LockState(
            center = Offset(canvasSize.x, canvasSize.y),
            radius = 100f,
            color = Color.LightGray,
            isHeld = false
        )
    )*/
    locks.forEach{
        Log.i("generateLocks", "Lock position = ${it.center}")
    }
    return locks
}

// Extension function to convert Dp to Px
fun Dp.toPx(context: Context): Float {
    return this.value * context.resources.displayMetrics.density
}