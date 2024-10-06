package hu.bme.aut.android.cityofguilds.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.auth.FirebaseAuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.data.database.firebase.FirebasePointService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitPointServiceImpl
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitService.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideAuthService(): AuthService = FirebaseAuthService(FirebaseAuth.getInstance())
    /*
    @Provides
    @Singleton
    fun providePointServiceInstance(
        authService: AuthService
    ): PointService = FirebasePointService(FirebaseFirestore.getInstance(), authService)
    */

    //New fancy backend
    @Provides
    @Singleton
    fun provideRetrofitService():RetrofitService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun providePointServiceInstance(
        authService: AuthService,
        retrofitService : RetrofitService
    ): PointService = RetrofitPointServiceImpl(authService, retrofitService)

}