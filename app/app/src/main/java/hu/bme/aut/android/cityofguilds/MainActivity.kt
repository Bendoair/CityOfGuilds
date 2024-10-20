package hu.bme.aut.android.cityofguilds

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.cityofguilds.feature.capture.NfcHandlerViewModel
import hu.bme.aut.android.cityofguilds.feature.games.multilock.MultilockGameScreen
import hu.bme.aut.android.cityofguilds.navigation.NavGraph
import hu.bme.aut.android.cityofguilds.ui.theme.CityOfGuildsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val NFCviewModel:NfcHandlerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NFCviewModel.initializeNfcAdapter(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            CityOfGuildsTheme {
                MultilockGameScreen(3, this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        NFCviewModel.enableForeground(this, pendingIntent)
    }

    override fun onPause() {
        super.onPause()
        NFCviewModel.disableForeground(this)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NFCviewModel.processNfcIntent(intent)
    }
}
