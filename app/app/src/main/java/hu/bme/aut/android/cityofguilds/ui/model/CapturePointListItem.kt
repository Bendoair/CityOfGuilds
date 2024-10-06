package hu.bme.aut.android.cityofguilds.ui.model

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cityofguilds.R

@Composable
fun CapturePointListItem (point:CapturePointUi) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = point.guildname)
        Icon(
            imageVector = Icons.Default.AccountBalance,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier
                .size(22.dp)
                .padding(start = 10.dp)
        )
    }
}

@Preview
@Composable
fun CapLIPreview(){
    CapturePointListItem(point = CapturePointUi(
        guildname = "Marauder's Guild",
        captureDate = "2024.05.29."
    ))


}