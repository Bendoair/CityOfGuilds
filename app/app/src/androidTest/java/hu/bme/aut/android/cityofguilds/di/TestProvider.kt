package hu.bme.aut.android.cityofguilds.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitPointServiceImpl
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitService
import hu.bme.aut.android.cityofguilds.data.database.retrofit.RetrofitService.Companion.BASE_URL
import hu.bme.aut.android.cityofguilds.dummyClasses.DummyAuthService
import hu.bme.aut.android.cityofguilds.dummyClasses.DummyPointService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class TestProvider {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = DummyAuthService()



    @Provides
    @Singleton
    fun providePointServiceInstance(): PointService = DummyPointService()
}