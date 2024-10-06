package hu.bme.aut.android.cityofguilds.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime


data class Point (
    val id: String = "",
    val guildname: String = "",
    val coordinateX:Float = 0.0f,
    val coordinateY:Float = 0.0f,
    var captureDate: kotlinx.datetime.LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
    var ownerId: String = ""
)