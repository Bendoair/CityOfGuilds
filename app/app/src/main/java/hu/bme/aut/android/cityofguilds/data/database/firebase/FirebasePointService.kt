package hu.bme.aut.android.cityofguilds.data.database.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class FirebasePointService (
    private val firestore: FirebaseFirestore,
    private val authService: AuthService
):PointService{

    @OptIn(ExperimentalCoroutinesApi::class)
    override val points: Flow<List<Point>> = authService.currentUser.flatMapLatest { user ->
        if (user == null) flow { emit(emptyList()) }
        else userCollection(user.id)
            .snapshots()
            .map { snapshot ->
                snapshot
                    .toObjects<FirebasePoint>()
                    .map {
                        it.asPoint()
                    }
            }
    }
    override suspend fun getPoint(id: String): Point? {

        return firestore.collection(POINTS_COLLECTION).get().await().toObjects<FirebasePoint>().singleOrNull()?.asPoint()
    }

    override suspend fun addNewPoint(point: Point) {
        if(authService.hasUser){
            firestore.collection(POINTS_COLLECTION).add(point.asFirebasePoint())
        }
    }

    override suspend fun capturePoint(pointId:String, userId:String) :Boolean{
        Log.i("capture", "${pointId} Guild is being captured by: ${userId}")

        //return true


        var pointInQuestion = getPoint(pointId)
        if(pointInQuestion == null){
            Log.i("capture", "No point like that found in db")
            return false
        }

        pointInQuestion.ownerId = userId
        pointInQuestion.captureDate = LocalDateTime.now().toKotlinLocalDateTime()

        userCollection(userId).document(pointInQuestion.id).set(pointInQuestion.asFirebasePoint())

        updatePointInGlobal(pointInQuestion)

        return true

    }

    override suspend fun updatePointInGlobal(point: Point) {
        firestore.collection(POINTS_COLLECTION).document(point.id).set(point.asFirebasePoint())
    }

    override suspend fun getAllPoints(): List<Point> {
        val firebasePointList =  firestore.collection(POINTS_COLLECTION).get().await().toObjects<FirebasePoint>()
        return firebasePointList.map { it.asPoint() }
    }

    override suspend fun getCurrentUserPoints(): List<Point> {
        if(!authService.hasUser){
            Log.i("getCurrUserPoints", "Noone was logged in")
            return emptyList()
        }
        Log.i("entered", "getting points for ${authService.currentUserId!!}")

        return userCollection(authService.currentUserId!!).get().await().toObjects<FirebasePoint>().map { it.asPoint() }
    }

    override suspend fun addNewUser(id: String) {
        firestore.collection(USER_COLLECTION).add(User(id, 0).asFirebaseUser())
    }

    override suspend fun getUser(id: String): User? {
        //TODO fully implement this if we ever wanna use firebase, this WILL break the app
        return null
    }


    private fun userCollection(userId: String) = firestore.collection(USER_COLLECTION).document(userId).collection(CAPTURED_POINTS)


    companion object {
        private const val USER_COLLECTION = "users"
        private const val CAPTURED_POINTS = "captured_points"
        private const val POINTS_COLLECTION = "all_points"
    }
}
