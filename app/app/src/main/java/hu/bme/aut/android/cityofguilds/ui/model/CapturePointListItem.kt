package hu.bme.aut.android.cityofguilds.ui.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.outlined.Castle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cityofguilds.R
import hu.bme.aut.android.cityofguilds.ui.theme.ParchmentBrown

@Composable
fun CapturePointListItem (point:CapturePointUi) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .alpha(0.8f),
        colors = CardDefaults.cardColors(contentColor = ParchmentBrown),
        shape = RoundedCornerShape(5.dp)


    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Column {
                Icon(
                    imageVector = Icons.Outlined.Castle,
                    tint = Color.Red,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = point.guildname,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.CenterStart)
                        .padding(5.dp),
                    textAlign = TextAlign.Left
                )
            }

            VerticalDivider(
                thickness = 5.dp,
                modifier = Modifier.padding(10.dp)

            )
            Text(
                text = point.captureDate.substringBefore('T'),
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center),
            )
        }

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