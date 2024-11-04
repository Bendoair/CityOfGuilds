package hu.bme.aut.android.cityofguilds.feature.games.multilock

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.feature.games.multilock.draw.drawBackground
import hu.bme.aut.android.cityofguilds.feature.games.multilock.draw.drawLocks
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultilockGameScreen(
    numberOfLocks:Int = 0,
    @ApplicationContext context:Context,
    onGameWon : () ->Unit,
    onDismiss : () ->Unit,

    viewModel: MultilockGameViewModel = hiltViewModel(),
    ) {

    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    var gameWonDialogVisible by remember { mutableStateOf(false) }

    if(gameWonDialogVisible){
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clickable {
                        onGameWon()
                    },
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.you_won_click_this_to_claim_guild),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
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

        gameContent(
            context =  context,
            viewModel =  viewModel,
            onGameWon = {gameWonDialogVisible = true},
            onDismiss = onDismiss,
            numberOfLocks =  numberOfLocks)


    }










}

@Composable
fun gameContent(
    context: Context,
    viewModel: MultilockGameViewModel,
    onGameWon : () ->Unit,
    onDismiss : () ->Unit,

    numberOfLocks:Int

){
    // State to hold the size of the canvas
    var canvasSize by remember { mutableStateOf(Offset(context.resources.displayMetrics.widthPixels.toFloat(),context.resources.displayMetrics.heightPixels.toFloat() )) }

    val state = viewModel.state.collectAsStateWithLifecycle()

    /*
    LaunchedEffect(Unit) {
        viewModel.init(context, canvasSize, numberOfLocks)
    }
    */



    val closedLockBitmap = ImageBitmap.imageResource(R.drawable.closed_green_lock_icon)
    val openLockBitmap = ImageBitmap.imageResource(R.drawable.open_red_lock_icon)


    Box(
        modifier = Modifier.fillMaxSize()
    ){
        //Game content
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    if (!state.value.initialized) {
                        canvasSize = Offset(size.width.toFloat(), size.height.toFloat())
                        viewModel.init(context, canvasSize, numberOfLocks, onGameWon)
                    }
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        // Track all pointers in this gesture (multi-touch)
                        val pointers = mutableMapOf<Long, Offset>()

                        // Process all pointer events (touch down, move, up)
                        while (true) {
                            val locks = state.value.locks
                            val event = awaitPointerEvent()
                            event.changes.forEach { pointerInputChange ->
                                val pointerId = pointerInputChange.id.value
                                val currentPosition = pointerInputChange.position

                                // On touch down or move, update the position
                                if (pointerInputChange.pressed) {
                                    pointers[pointerId] = currentPosition

                                    // Highlight the circle that contains the current touch point
                                    val updatedLocks = locks.map {
                                        if (it.contains(currentPosition)) {
                                            it.apply { toggleHeld(true) }
                                        } else it
                                    }
                                    viewModel.refreshLocks(updatedLocks)

                                    //DONT
                                    // Consume the touch event to avoid further processing
                                    //pointerInputChange.consume()
                                } else {
                                    // On touch release, remove pointer from map and reset circles
                                    pointers.remove(pointerId)

                                    // Reset all circles that are no longer held
                                    val updatedLocks = locks.map { circle ->
                                        if (pointers.values.none { circle.contains(it) }) {
                                            circle.apply { toggleHeld(false) }
                                        } else circle
                                    }
                                    viewModel.refreshLocks(updatedLocks)
                                }

                            }


                            // Exit loop if no pointers are active
                            if (event.changes.none { it.pressed }) break
                        }


                    }

                }


                ,


        ) {

            drawBackground(size)
            drawLocks(
                lockStates = state.value.locks,
                openLockIcon = openLockBitmap,
                closedLockIcon = closedLockBitmap)




        }

        SmallFloatingActionButton(
            onClick = onDismiss,
            modifier = Modifier
                .padding(10.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Exit",
                tint = Color.Red
            )
        }

    }






}



