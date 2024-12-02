package hu.bme.aut.android.cityofguilds.ui.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cityofguilds.domain.model.User

@Composable
fun LeaderboardItem(user: User, isHighlighted:Boolean = false, ){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = user.id.take(8),
            modifier = if (isHighlighted) Modifier.padding(4.dp) else Modifier,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "Points: ${user.numberOfPoints}",
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
    }
}