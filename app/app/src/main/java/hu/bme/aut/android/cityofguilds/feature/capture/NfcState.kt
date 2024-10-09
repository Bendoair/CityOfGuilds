package hu.bme.aut.android.cityofguilds.feature.capture

import android.nfc.NfcAdapter
import hu.bme.aut.android.cityofguilds.domain.model.Point
import kotlinx.coroutines.flow.MutableStateFlow

data class NfcState(
    val newInfo:Boolean = false,
    val pointInfo: Point? = null,
    var nfcAdapter: NfcAdapter? = null,
    var nfcEnabled:Boolean = false,
    var currentMode: NfcModes = NfcModes.READ
){
    enum class NfcModes{
        READ, WRITE
    }
    companion object{
        val state = MutableStateFlow(NfcState())
    }

}

enum class CaptureServiceState{
    IDLE, LOOKING, FOUND
}