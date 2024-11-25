package hu.bme.aut.android.cityofguilds.data.auth.retrofit

import androidx.browser.trusted.Token
import hu.bme.aut.android.cityofguilds.domain.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAuthenticator {


    @POST("auth/register")
    suspend fun register(@Body authDTO: AuthDTO) : TokenDTO

    @POST("auth/login")
    suspend fun login(@Body authDTO: AuthDTO) : TokenDTO

    @POST("auth/authenticate")
    suspend fun authenticate(@Body tokenDTO: TokenDTO) : User?


    companion object{
        const val BASE_URL = "http://46.139.171.101:8080/" //188.6.3.79:8080/
    }
}