package hu.bme.aut.android.cityofguilds.data.database.retrofit

import hu.bme.aut.android.cityofguilds.domain.model.Point
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PointDTO(
    val id: String = "",
    val guildname: String = "",
    val coordinateX:Float = 0.0f,
    val coordinateY:Float = 0.0f,
    var captureDate: String = "",
    var ownerId: String = ""
)

fun PointDTO.toPoint():Point{
    return Point(
        id = id,
        guildname = guildname,
        captureDate = LocalDateTime.parse(captureDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toKotlinLocalDateTime(),
        ownerId = ownerId,
        coordinateX = coordinateX.toFloat(),
        coordinateY = coordinateY.toFloat()
    )
}

fun Point.toDTO():PointDTO{
    return PointDTO(
        id = id,
        guildname = guildname,
        captureDate = captureDate.toString(),
        ownerId = ownerId,
        coordinateX = coordinateX.toFloat(),
        coordinateY = coordinateY.toFloat()
    )
}
