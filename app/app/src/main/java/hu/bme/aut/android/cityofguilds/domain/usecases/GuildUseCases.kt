package hu.bme.aut.android.cityofguilds.domain.usecases


class GuildUseCases(

    val emailValidUseCase: IsEmailValidUseCase,
    val passwordsMatchUseCase: PasswordsMatchUseCase,
    val listAllUseCase: ListAllPointsUseCase,
    val listOwnUseCase: ListOwnPointsUseCase,
    val captureGuildUseCase: CaptureGuildUseCase
) {
}