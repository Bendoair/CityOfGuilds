package hu.bme.aut.android.cityofguilds.domain.usecases

import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test


class PasswordsMatchUseCaseTest {
    private lateinit var useCase : PasswordsMatchUseCase

    @Before
    fun setUp(){
        useCase = PasswordsMatchUseCase()
    }

    @Test
    fun `Passwords match_true`(){
        assert(useCase("ASD", "ASD"))
    }

    @Test
    fun  `Passwords don't match_false`(){
        assertFalse(useCase("AS", "ASD"))
    }

}