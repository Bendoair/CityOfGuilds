package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point

class ListOwnPointsUseCase {
    suspend operator fun invoke(repository : PointService): List<Point>{
        return  repository.getCurrentUserPoints()
    }
}