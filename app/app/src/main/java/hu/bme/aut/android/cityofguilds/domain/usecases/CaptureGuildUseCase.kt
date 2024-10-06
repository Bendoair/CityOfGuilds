package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.data.database.PointService

class CaptureGuildUseCase {
    suspend operator fun invoke(capturedGuildId:String, userId:String,repository:PointService):Boolean {
        return repository.capturePoint(capturedGuildId, userId)
    }
}