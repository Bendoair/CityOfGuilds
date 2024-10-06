package hu.bme.aut.android.cityofguilds.data.database.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.cityofguilds.domain.model.Point
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

data class FirebasePoint(
    @DocumentId val id: String = "",
    val guildname: String = "",
    val captureDate: Timestamp = Timestamp.now(),
    val ownerId : String = "",
    val coordinateX:Float = 0.0f,
    val coordinateY:Float = 0.0f

)

fun FirebasePoint.asPoint() = Point(
    id = id,
    guildname = guildname,
    captureDate = LocalDateTime
        .ofInstant(Instant.ofEpochSecond(captureDate.seconds), ZoneId.systemDefault())
    .toKotlinLocalDateTime()
    ,
    ownerId = ownerId,
    coordinateX = coordinateX.toFloat(),
    coordinateY = coordinateY.toFloat()
)
fun Point.asFirebasePoint() = FirebasePoint(
    id = id,
    guildname = guildname,
    captureDate = let{
        val localDateTime = LocalDateTime.now()
        val instant = localDateTime.toInstant(ZoneOffset.UTC)
        val date = java.util.Date.from(instant)
        Timestamp(date)
    },
    ownerId = ownerId,
    coordinateX = coordinateX,
    coordinateY = coordinateY

)


