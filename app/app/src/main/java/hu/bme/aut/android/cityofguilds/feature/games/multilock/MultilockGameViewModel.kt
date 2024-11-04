package hu.bme.aut.android.cityofguilds.feature.games.multilock

import android.content.Context
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MultilockGameViewModel @Inject constructor(
) :ViewModel() {

    private val _state = MutableStateFlow(MultilockGameState())
    val state = _state.asStateFlow()

    fun init(context: Context, canvasSize: Offset, numberOfLocks: Int, onGameWon: () -> Unit){
        val generatedLocks = generateLocks(
            context = context,
            numberOfCircles = numberOfLocks,
            canvasSize = canvasSize,
            minRadius = 20.dp,
            maxRadius = 40.dp

        )
        _state.update {
            it.copy(
                numberOfLocks = numberOfLocks,
                locks = generatedLocks,
                heldLocks = 0,
                gameWon = false,
                initialized = true,
                onGameWon = onGameWon,
            )
        }
    }

    fun refreshLocks(updatedLocks :List<LockState>){
        //Don't make changes if the game is already won
        if(_state.value.gameWon){
            return
        }
        val updatedHeldLocks = updatedLocks.count { it.isHeld }
        _state.update {
            it.copy(
                heldLocks = updatedHeldLocks,
                locks = updatedLocks,
                gameWon = updatedHeldLocks >= it.numberOfLocks
            )
        }
        if (updatedHeldLocks >= _state.value.numberOfLocks){
            Log.i("GameScreen", "Game won set")
            _state.value.onGameWon()
        }

    }


    fun generateLocks(context: Context, numberOfCircles: Int, canvasSize: Offset, minRadius: Dp, maxRadius: Dp): List<LockState> {
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
    private fun Dp.toPx(context: Context): Float {
        return this.value * context.resources.displayMetrics.density
    }

}

data class MultilockGameState(
    var numberOfLocks : Int = 0,
    var locks : List<LockState> = emptyList(),
    var heldLocks : Int = 0,
    var gameWon : Boolean = false,
    var initialized:Boolean = false,
    var onGameWon: ()->Unit = {},
)