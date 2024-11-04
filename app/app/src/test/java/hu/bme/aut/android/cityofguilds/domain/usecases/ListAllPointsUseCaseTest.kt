package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.dummyClasses.DummyPointService
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class ListAllPointsUseCaseTest {

    private lateinit var getAllUseCase : ListAllPointsUseCase
    private lateinit var dummyRepo : PointService

    @Before
    fun setUp() {
        dummyRepo = DummyPointService()
        getAllUseCase = ListAllPointsUseCase()
    }

    @Test
    fun `Get all Points when repo is empty_No points found`() = runBlocking{
        val points = getAllUseCase(dummyRepo)
        assert(points.isEmpty()) { "Empty repo does not return empty list via UseCase" }

    }

    @Test
    fun `Get all Points when repo is not empty_Correct number of points found`() = runBlocking {
        val points = getAllUseCase(dummyRepo)
        assert(points.containsAll(dummyRepo.points.last())) { "Empty repo does not return list containing the points in repo via UseCase" }

    }
}