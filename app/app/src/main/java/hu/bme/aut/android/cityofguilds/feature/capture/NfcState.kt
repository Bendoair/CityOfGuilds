package hu.bme.aut.android.cityofguilds.feature.capture

import android.nfc.NfcAdapter
import kotlinx.coroutines.flow.MutableStateFlow

data class NfcState(
    val newInfo:Boolean = false,
    val pointId: String ="",
    var nfcAdapter: NfcAdapter? = null,
    var nfcEnabled:Boolean = false,
    var inCaptureMode:Boolean = false,
    var inWriteMode:Boolean = false,
){
    companion object{
        val state = MutableStateFlow(NfcState())
    }

}

enum class CaptureServiceState{
    IDLE, LOOKING, FOUND
}