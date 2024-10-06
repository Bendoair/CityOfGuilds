package hu.bme.aut.android.cityofguilds.data.database.retrofit

import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrofitPointServiceImpl @Inject constructor(
    private val authService: AuthService,
    private val retrofitService: RetrofitService
) : PointService {

    override val points: Flow<List<Point>> = flow {
        emit(retrofitService.getAllPoints().map { it.toPoint() })
    }

    override suspend fun getPoint(id: String): Point? {
        return retrofitService.getPoint(id)?.toPoint()
    }

    override suspend fun addNewPoint(point: Point) {
        retrofitService.addNewPoint(point.toDTO())
    }

    override suspend fun capturePoint(pointId: String, userId: String): Boolean {
        val dummyUserForID = User(id = userId)
        return retrofitService.capturePoint(pointId, dummyUserForID)
    }

    override suspend fun updatePointInGlobal(point: Point) {
        // Assuming you want to use the addNewPoint method as update logic
        retrofitService.addNewPoint(point.toDTO())
    }

    override suspend fun getAllPoints(): List<Point> {
        return retrofitService.getAllPoints().map { it.toPoint() }
    }

    override suspend fun getCurrentUserPoints(): List<Point> {
        // You can pass the userId from the context or other user management services
        if (!authService.hasUser){
            return emptyList()
        }
        //Double bangs is justified here
        val userId = authService.currentUserId!!
        return retrofitService.getCurrentUserPoints(userId).map { it.toPoint() }
    }

    override suspend fun addNewUser(id: String) {
        val user = User(id = id, numberOfPoints = 0, isDeveloper = true)
        retrofitService.addNewUser(user)
    }

    override suspend fun getUser(id: String) : User?{
        return retrofitService.getUser(id)
    }
}