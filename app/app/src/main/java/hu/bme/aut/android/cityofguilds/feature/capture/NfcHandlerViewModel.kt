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
import android.nfc.tech.NdefFormatable
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.usecases.GuildUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel //Durva barbarizmus, ez miért ViewModel, annak kell lennie?
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
        Log.i("NFC", "ProcessIntendStarted, intent.action: ${intent.action}")
        when(intent.action){
            NfcAdapter.ACTION_NDEF_DISCOVERED -> {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

                tag?.let {
                    when(state.value.currentMode){
                        NfcState.NfcModes.READ -> readFromTag(it)
                        NfcState.NfcModes.WRITE -> writeTag(it)
                        else -> {Log.e("NFC", "NFC intent received without NFC mode set!")}
                    }

                }
            }
            NfcAdapter.ACTION_TAG_DISCOVERED -> {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                //Fuck it, we ball
                //Turns out there is absolutely zero reason to check if the tag is writable in my UseCase
                tag?.let {
                    when(state.value.currentMode){
                        NfcState.NfcModes.READ -> {Log.e("NFC", "Trying to read an unformatted NFC tag, wont work!")}
                        NfcState.NfcModes.WRITE -> writeTag(it)
                        else -> {Log.e("NFC", "NFC intent received without NFC mode set!")}
                    }

                }
            }
        }

    }

    private fun writeTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                Log.i("NFC", "WriteTagStarted")
                if(state.value.pointInfo == null) {
                    Log.e("NFC", "Write Tag Data is not correct, it is empty!")
                    return@launch
                }

                //First write into database to get ID as well
                //addNewPoint returns the database instance
                val pointInDatabase = repository.addNewPoint(state.value.pointInfo!!)
                _state.update {
                    it.copy(
                        pointInfo = pointInDatabase
                    )
                }

                if(_state.value.pointInfo?.id == null){
                    Log.e("NFC", "Point in database with ID of null??")
                }

                //Now we can write the tag
                val ndef = Ndef.get(tag)
                ndef?.connect()
                val message = NdefMessage(
                    //Double bangs is justified because of the above null check
                    arrayOf(NdefRecord.createTextRecord("en", state.value.pointInfo!!.toNfcTextRecord()))
                )
                ndef.writeNdefMessage(message)
                ndef?.close()
                Log.i("Add Point", "NFC written, starting Database things")
                //Double bangs is justified because of the above null check
                _state.update {
                    it.copy(
                        newInfo = false,
                        pointInfo = null
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
                    pointInfo = text.toPointFromNfcTextRecord()
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
        if(state.value.currentMode != NfcState.NfcModes.READ){
            return
        }
        viewModelScope.launch {
            val success = captureGuild()
            if(success){
                _state.update { it.copy(
                    newInfo = false,
                    pointInfo = null
                ) }
            }
        }
    }
    private suspend fun captureGuild():Boolean {


        val id = state.value.pointInfo?.id?:""
        val guildCaptured = pointOperations.captureGuildUseCase(id, authService.currentUserId?:"", repository)
        Log.i("Capture", "Tried capturing guild with id: $id, the resulting guild was: $guildCaptured")
        return guildCaptured
    }

    fun enableNFC(context: Context) {
        context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
    }

    fun checkNfcStatus(){
        _state.update { it.copy(
            nfcEnabled = it.nfcAdapter?.isEnabled?:false
        ) }

    }

    fun updatePointInfo(point:Point){
        _state.update { it.copy(
            newInfo = true,
            pointInfo = point
        ) }
    }

    fun changeMode(mode:NfcState.NfcModes){
        Log.i("NFC", "NFC mode changed to: $mode")
        _state.update {it.copy(
            currentMode = mode
        )
        }
    }

    fun enableForeground(activity: Activity, pendingIntent:PendingIntent) {
        _state.value.nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    fun disableForeground(activity: Activity) {
        _state.value.nfcAdapter?.disableForegroundDispatch(activity)
    }
    fun Point.toNfcTextRecord():String{
        return "${this.id};${this.guildname};${this.coordinateX};${this.coordinateY};${this.captureDate};${this.ownerId}"
    }

    fun String.toPointFromNfcTextRecord(): Point{
        val parsed = this.split(";")
        if(parsed.size != 6){
            Log.e("NFC", "Couldn't parse NFC text record, it was not exactly 6 elements, containing a point info")
            return Point()
        }
        return Point(
            id = parsed[0],
            guildname = parsed[1],
            coordinateX = parsed[2].toFloat(),
            coordinateY = parsed[3].toFloat(),
            captureDate = parsed[4].toLocalDateTime(),
            ownerId = parsed[5]
        )
    }
}

