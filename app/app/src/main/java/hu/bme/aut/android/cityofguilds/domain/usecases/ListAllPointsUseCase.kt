package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point

class ListAllPointsUseCase {
    suspend operator fun invoke(repository : PointService): List<Point>{
        return  repository.getAllPoints()
    }
}