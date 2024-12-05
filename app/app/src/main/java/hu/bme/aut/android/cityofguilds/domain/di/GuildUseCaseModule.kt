package hu.bme.aut.android.cityofguilds.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.usecases.CaptureGuildUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
import hu.bme.aut.android.cityofguilds.domain.usecases.IsEmailValidUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.ListAllPointsUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.ListOwnPointsUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.PasswordsMatchUseCase
import hu.bme.aut.android.cityofguilds.domain.usecases.UseCase
import javax.inject.Inject
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
    fun provideOldListAllUseCase():ListAllPointsUseCase = ListAllPointsUseCase()


    @Provides
    @Singleton
    fun provideOldListOwnUseCase():ListOwnPointsUseCase = ListOwnPointsUseCase()

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


    //New Use Cases
    @Provides
    @Singleton
    fun provideLoginUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.LoginUseCase{
        return UseCase.LoginUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.RegisterUseCase{
        return UseCase.RegisterUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideTokenUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.TryLoginWithTokenUseCase{
        return UseCase.TryLoginWithTokenUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideCaptureUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.CaptureGuildUseCase{
        return UseCase.CaptureGuildUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideCurrentUserUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.GetCurrentUserUseCase{
        return UseCase.GetCurrentUserUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideListOwnUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.ListOwnUseCaseUseCase{
        return UseCase.ListOwnUseCaseUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideLeaderboardUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.GetLeaderboardUseCase{
        return UseCase.GetLeaderboardUseCase(authService, pointService)
    }

    @Provides
    @Singleton
    fun provideListAllUseCase(
        authService: AuthService,
        pointService: PointService
    ):UseCase.ListAllUseCaseUseCase{
        return UseCase.ListAllUseCaseUseCase(authService, pointService)
    }



}