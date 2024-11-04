package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.dummyClasses.DummyPointService
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ListOwnPointsUseCaseTest {
    private lateinit var dummyRepo: DummyPointService
    private lateinit var listOwnPointsUseCase: ListOwnPointsUseCase

    @Before
    fun setUp() {
        dummyRepo = DummyPointService()
        listOwnPointsUseCase = ListOwnPointsUseCase()
    }

    @Test
    fun `ListOwnPointsUseCase should return points owned by the current user`() = runBlocking {
        val currentUserPoint = dummyRepo.addNewPoint(Point(guildname = "CurrentUserPoint", ownerId = "1"))
        val otherUserPoint = dummyRepo.addNewPoint(Point(guildname = "OtherUserPoint", ownerId = "OtherUser"))

        val result = listOwnPointsUseCase(dummyRepo)

        assert(result.contains(currentUserPoint))
        assertFalse(result.contains(otherUserPoint))
    }
}