package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.dummyClasses.DummyPointService
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.Console

class CaptureGuildUseCaseTest{

    private lateinit var captureUseCase : CaptureGuildUseCase
    private lateinit var dummyRepo : PointService

    @Before
    fun setUp() {
        dummyRepo = DummyPointService()
        captureUseCase = CaptureGuildUseCase()
    }

    @Test
    fun `Capture a Guild that is in the repo_success`() = runBlocking {
        val guild = dummyRepo.addNewPoint(
            Point(
                guildname = "TestToCapture",
                ownerId = ""
            )
        )
        val testOwnerId = "TestUserId"
        // Perform the capture operation
        val captureResult = captureUseCase("0", testOwnerId, dummyRepo)
        val updatedGuild = dummyRepo.points.first().firstOrNull() { it.id == guild.id }
        val isOwnerUpdated = updatedGuild?.ownerId == testOwnerId

        // Log intermediate results
        println("Capture result: $captureResult")
        println("Updated guild: $updatedGuild")

        // Assertion with logging
        assert(captureResult && isOwnerUpdated) {
            "Test failed: captureResult=$captureResult, expected ownerId=$testOwnerId, actual ownerId=${updatedGuild?.ownerId}"
        }
    }

    @Test
    fun `Can't capture a guild that is not in the repo`() = runBlocking{
        assertFalse(captureUseCase("any ID", "Any User ID", dummyRepo))
    }


}