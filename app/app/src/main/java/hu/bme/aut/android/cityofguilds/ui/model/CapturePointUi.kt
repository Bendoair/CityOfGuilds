package hu.bme.aut.android.cityofguilds.ui.model

import hu.bme.aut.android.cityofguilds.domain.model.Point
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime

data class CapturePointUi (
    val id: String = "",
    val guildname: String = "",
    val captureDate: String = LocalDateTime.now().toKotlinLocalDateTime().date.toString(),
    val ownerId: String = "",
    val coordinateX:Float = 0.0f,
    val coordinateY:Float = 0.0f
)

fun Point.asCapturePointUi():CapturePointUi = CapturePointUi(
    id = id,
    guildname = guildname,
    captureDate = captureDate.toString(),
    ownerId = ownerId,
    coordinateX = coordinateX,
    coordinateY = coordinateY

)

fun CapturePointUi.asPoint() : Point = Point(
    id = id,
    guildname = guildname,
    captureDate = captureDate.toLocalDateTime(),
    ownerId = ownerId,
    coordinateX = coordinateX,
    coordinateY = coordinateY
)