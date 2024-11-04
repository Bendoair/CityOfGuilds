package hu.bme.aut.android.cityofguilds.domain.usecases

import org.junit.Assert
import org.junit.Test

class IsEmailValidUseCaseTest{
    private val testEmailUseCase = IsEmailValidUseCase()
    @Test
    fun Correct_format_email_checks_correct(){
        assert(testEmailUseCase("kiss.janos@aut.bme.hu"))
    }

    @Test
    fun Wrong_format_email_is_wrong(){
        Assert.assertFalse(testEmailUseCase("kisanos@"))
    }
}