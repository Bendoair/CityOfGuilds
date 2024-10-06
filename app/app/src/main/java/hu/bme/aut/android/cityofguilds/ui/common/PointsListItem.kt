package hu.bme.aut.android.cityofguilds.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.cityofguilds.ui.model.CapturePointUi
import kotlinx.datetime.toLocalDate
import java.time.temporal.ChronoUnit
import java.time.LocalDate


@Composable
fun PointsListItem(point: CapturePointUi){
    Row(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {
        Text(
            text = point.guildname,
            style = TextStyle(
                fontFamily = FontFamily.Cursive,
                fontSize = 200.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                //fontStyle = FontStyle.Italic
            )
        )
        Spacer(modifier = Modifier.size(20.dp))
        val daysPassed = ChronoUnit.DAYS.between(LocalDate.parse(point.captureDate), LocalDate.now())
        when{
            daysPassed < 1 -> {
                Icons.Default.Air}
            daysPassed < 7 -> {Icons.Default.AcUnit}
            daysPassed < 30 -> {Icons.Default.AttachMoney}
            else -> {Icons.Default.Cake}
        }
    }
}