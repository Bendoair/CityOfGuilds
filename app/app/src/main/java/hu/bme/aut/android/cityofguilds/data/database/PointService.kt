package hu.bme.aut.android.cityofguilds.data.database

import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.Flow

interface PointService {
    val points: Flow<List<Point>>

    suspend fun getPoint(id: String): Point?

    suspend fun addNewPoint(point: Point)

    suspend fun capturePoint(pointId:String, userId:String):Boolean

    suspend fun updatePointInGlobal(point: Point)

    suspend fun getAllPoints(): List<Point>

    suspend fun getCurrentUserPoints(): List<Point>

    suspend fun addNewUser(id: String)

    suspend fun getUser(id:String) : User?
}