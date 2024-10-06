package hu.bme.aut.android.cityofguilds.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.cityofguilds.domain.usecases.CaptureGuildUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
import hu.bme.aut.android.cityofguilds.domain.usecases.IsEmailValidUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.ListAllPointsUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.ListOwnPointsUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.PasswordsMatchUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GuildUseCaseModule {

    @Provides
    @Singleton
    fun provideEmailValidUseCase():IsEmailValidUseCase = IsEmailValidUseCase()

    @Provides
    @Singleton
    fun providePasswordMatchUseCase():PasswordsMatchUseCase = PasswordsMatchUseCase()

    @Provides
    @Singleton
    fun provideListAllUseCase():ListAllPointsUseCase = ListAllPointsUseCase()


    @Provides
    @Singleton
    fun provideListOwnUseCase():ListOwnPointsUseCase = ListOwnPointsUseCase()

    @Provides
    @Singleton
    fun provideCaptureGuildUseCase():CaptureGuildUseCase = CaptureGuildUseCase()

    @Provides
    @Singleton
    fun provideGuildUseCases(
        emailvalid:IsEmailValidUseCase,
        listAll:ListAllPointsUseCase,
        listOwn:ListOwnPointsUseCase,
        passwordMatch:PasswordsMatchUseCase,
        captureGuild: CaptureGuildUseCase
    ):GuildUseCases = GuildUseCases(emailvalid,passwordMatch, listAll, listOwn, captureGuild)

}