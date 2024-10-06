package hu.bme.aut.android.cityofguilds.feature.capture

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class NfcHandlerViewModel @Inject constructor(
    private val pointOperations: GuildUseCases,
    private val authService: AuthService,
    private val repository:PointService
    ) : ViewModel(){
    private val _state = NfcState.state
    val state = _state.asStateFlow()

    fun initializeNfcAdapter(activity: Activity) {
        _state.update { it.copy(
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        ) }
    }

    fun processNfcIntent(intent: Intent) {
        Log.i("started", "ProcessIntendStarted")
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            tag?.let {
                if (state.value.inCaptureMode){
                    readFromTag(it)
                }
                if (state.value.inWriteMode){
                    writeTag(it)
                }
            }
        }
    }

    private fun writeTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                Log.i("NFC", "WriteTagStarted")
                if (state.value.pointId.isEmpty()) {
                    Log.e("NFC", "Write Tag Data is not correct, it is null, or empty!")
                    return@launch
                }
                val ndef = Ndef.get(tag)
                ndef?.connect()
                val message = NdefMessage(
                    arrayOf(NdefRecord.createTextRecord("en", state.value.pointId))
                )
                ndef.writeNdefMessage(message)
                ndef?.close()
                _state.update {
                    it.copy(
                        newInfo = false,
                        pointId = ""
                    )
                }
            }catch (e:Exception){
                Log.e("NFC", "Error writing NFC tag", e)
            }

        }
    }

    private fun readFromTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("NFC", "ReadFromTagStarted")
            val ndef = Ndef.get(tag)
            ndef?.connect()
            val message = ndef?.ndefMessage
            ndef?.close()

            message?.records?.forEach { record ->
                val payload = record.payload
                val text = parseTextRecord(payload)
                Log.i("NFC", "NFC Info: $text")
                _state.update { it.copy(
                    newInfo = true,
                    pointId = text
                ) }
            }
        }
    }
    private fun parseTextRecord(payload: ByteArray): String {
        val textEncoding = if ((payload[0].toInt() and 0x80) == 0) "UTF-8" else "UTF-16" // Get the Text Encoding
        val languageCodeLength = payload[0].toInt() and 0x3F // Get the Language Code, length
        return String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, Charset.forName(textEncoding))
    }

    fun captureButtonOnClick(){
        if(!state.value.inCaptureMode){
            return
        }
        viewModelScope.launch {
            val success = captureGuild()
            if(success){
                _state.update { it.copy(
                    newInfo = false,
                    pointId = ""
                ) }
            }
        }
    }
    private suspend fun captureGuild():Boolean {

        val id = state.value.pointId
        val guildCaptured = pointOperations.captureGuildUseCase(id, authService.currentUserId?:"", repository)
        return guildCaptured
    }


    fun addNewPointButtonOnClick(){
        viewModelScope.launch {
            val success = captureGuild()
            if(success){
                _state.update { it.copy(
                    newInfo = false,
                    pointId = ""
                ) }
            }
        }
    }




    fun enableNFC(context: Context) {
        context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
    }

    fun checkNfcStatus(){
        _state.update { it.copy(
            nfcEnabled = it.nfcAdapter?.isEnabled?:false
        ) }

    }

    fun enableForeground(activity: Activity, pendingIntent:PendingIntent) {
        _state.value.nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    fun disableForeground(activity: Activity) {
        _state.value.nfcAdapter?.disableForegroundDispatch(activity)
    }


}