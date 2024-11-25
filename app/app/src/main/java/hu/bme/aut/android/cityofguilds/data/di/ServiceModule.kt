package hu.bme.aut.android.cityofguilds.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.auth.retrofit.RetrofitAuthService
import hu.bme.aut.android.cityofguilds.data.auth.retrofit.RetrofitAuthenticator
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitPointServiceImpl
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitService.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    //New fancy backend
    @Provides
    @Singleton
    fun provideRetrofitAuthenticator():RetrofitAuthenticator =
        Retrofit.Builder()
            .baseUrl(RetrofitAuthenticator.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAuthenticator::class.java)

    @Provides
    @Singleton
    fun provideAuthService(
        retrofitAuthenticator: RetrofitAuthenticator,
        @ApplicationContext context: Context
    ):AuthService = RetrofitAuthService(retrofitAuthenticator, context)



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