package hu.bme.aut.android.cityofguilds.dummyClasses

import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class DummyPointService : PointService {
    private val pointsList = mutableListOf<Point>()
    private val currentUser = "1"
    override val points: Flow<List<Point>>
        get() = flow { emit(pointsList) }

    override suspend fun getPoint(id: String): Point? {
        return pointsList.find { id == it.id }
    }

    override suspend fun addNewPoint(point: Point): Point {
        val pointIndexed = point.copy(
            id = pointsList.size.toString()
        )
        pointsList.add(pointIndexed)
        return pointIndexed
    }

    override suspend fun capturePoint(pointId: String, userId: String): Boolean {
        val result = pointsList.find { it.id == pointId } != null

        if(result){
            pointsList[pointId.toInt()] = pointsList[pointId.toInt()].copy(ownerId = userId)
        }

        return result
    }

    override suspend fun updatePointInGlobal(point: Point) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPoints(): List<Point> {
        return pointsList
    }

    override suspend fun getCurrentUserPoints(): List<Point> {
        return pointsList.filter { it.ownerId == currentUser}
    }

    override suspend fun addNewUser(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String): User? {
        TODO("Not yet implemented")
    }
}