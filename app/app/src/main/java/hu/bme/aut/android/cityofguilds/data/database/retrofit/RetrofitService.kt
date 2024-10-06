package hu.bme.aut.android.cityofguilds.data.database.retrofit

import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Retrofit interface to map your API endpoints
interface RetrofitService {

    @GET("points/getAll")
    suspend fun getAllPoints(): List<PointDTO>

    @GET("points/get/{pointId}")
    suspend fun getPoint(@Path("pointId") id: String): PointDTO?

    @PUT("points/capture/{pointId}")
    suspend fun capturePoint(
        @Path("pointId") pointId: String,
        @Body newOwner: User
    ): Boolean

    @POST("points/addNewPoint")
    suspend fun addNewPoint(@Body point: PointDTO): PointDTO

    @PUT("users/addNewUser")
    suspend fun addNewUser(@Body user: User): User

    @GET("users/usersPoints/{userId}")
    suspend fun getCurrentUserPoints(@Path("userId") userId: String): List<PointDTO>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User?

    companion object{
        const val BASE_URL = "http://192.168.0.52:8080/" //188.6.3.79:8080/
    }
}