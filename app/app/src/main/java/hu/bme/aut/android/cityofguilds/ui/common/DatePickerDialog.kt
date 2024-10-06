package hu.bme.aut.android.todo.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import kotlinx.datetime.LocalDate
import hu.bme.aut.android.cityofguilds.R
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    currentDate: LocalDate,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    )
    AlertDialog(
        text = {
            androidx.compose.material3.DatePicker(state = datePickerState)
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(datePickerState.selectedDateMillis.let {
                    LocalDateTime.ofEpochSecond(
                        (it ?: 0) / 1000,
                        ((it ?: 0) % 1000).toInt(),
                        ZoneOffset.UTC
                    ).toLocalDate().toKotlinLocalDate()
                })
            }) {
                Text(text = stringResource(id = R.string.dialog_ok_button_text))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dialog_dismiss_button_text))
            }
        },
        onDismissRequest = onDismiss
    )
}