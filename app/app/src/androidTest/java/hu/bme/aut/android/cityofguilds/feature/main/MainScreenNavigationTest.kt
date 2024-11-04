package hu.bme.aut.android.cityofguilds.feature.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import hu.bme.aut.android.cityofguilds.HiltTestRunner
import hu.bme.aut.android.cityofguilds.MainActivity
import hu.bme.aut.android.cityofguilds.data.di.ServiceModule
import hu.bme.aut.android.cityofguilds.dummyClasses.DummyAuthService
import hu.bme.aut.android.cityofguilds.navigation.ContentDestinations
import hu.bme.aut.android.cityofguilds.ui.util.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@UninstallModules(ServiceModule::class)
class MainScreenNavigationTest{

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun test_buttons_are_correct(){
        composeRule.setContent {
            MainScreen(
                {},
                MainScreenViewModel(DummyAuthService())
            )
        }

        composeRule
            .onNodeWithText("Home")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Capture")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Your Guilds")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Guild-map")
            .assertIsDisplayed()


    }





}