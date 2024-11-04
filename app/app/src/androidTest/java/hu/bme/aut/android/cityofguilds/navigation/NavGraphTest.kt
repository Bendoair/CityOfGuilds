package hu.bme.aut.android.cityofguilds.navigation

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import hu.bme.aut.android.cityofguilds.MainActivity
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.TestActivity
import hu.bme.aut.android.cityofguilds.data.di.ServiceModule
import hu.bme.aut.android.cityofguilds.ui.theme.CityOfGuildsTheme
import hu.bme.aut.android.cityofguilds.ui.util.TestTags
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(ServiceModule::class)
class NavGraphTest{



    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule =  createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp(){


    }

    @After
    fun resetNavigation(){

    }



    @Test
    fun after_10_seconds_login_button_is_visible(){
        // Wait until the button with text "Continue to Login" becomes visible, or timeout after 10 seconds
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasText("Continue to Login")).fetchSemanticsNodes().isNotEmpty()
        }

        // Assert that the button is displayed
        composeRule
            .onNodeWithText("Continue to Login")
            .assertIsDisplayed()
    }

    @Test
    fun continue_to_login_button_is_clicked_navigates(){
        // Wait until the button with text "Continue to Login" becomes visible, or timeout after 10 seconds
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasText("Continue to Login")).fetchSemanticsNodes().isNotEmpty()
        }

        // Perform click action on the button
        composeRule
            .onNodeWithText("Continue to Login")
            .assertIsDisplayed()
            .performClick()

        // Assert that the login screen is displayed by checking for a node with a specific tag
        composeRule
            .onNodeWithTag(TestTags.LOGIN_SCREEN)
            .assertIsDisplayed()

        composeRule.onNodeWithText("Email").performTextInput("simon@benedek.hu")
        composeRule.onNodeWithText("Password").performTextInput("Password1")

        composeRule
            .onNodeWithText("Sign In")
            .performClick()




    }

    @Test
    fun continue_to_register_button_is_clicked_navigates(){
        // Wait until the button with text "Continue to Login" becomes visible, or timeout after 10 seconds
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodes(hasText("Continue to Login")).fetchSemanticsNodes().isNotEmpty()
        }

        // Perform click action on the button
        composeRule
            .onNodeWithText("Continue to Login")
            .assertIsDisplayed()
            .performClick()

        // Assert that the login screen is displayed by checking for a node with a specific tag
        composeRule
            .onNodeWithTag(TestTags.LOGIN_SCREEN)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("I don't have an account yet.")
            .performClick()




    }





}