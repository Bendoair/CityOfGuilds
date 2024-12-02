package hu.bme.aut.android.cityofguilds.ui.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cityofguilds.domain.model.User

@Composable
fun Leaderboard(currentUser:User,users:List<User>, modifier: Modifier) {
    // Sort users by numberOfPoints in descending order, exclude current user
    val sortedUsers = users.filter { it.id != currentUser.id }
        .sortedByDescending { it.numberOfPoints }

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "You:", fontWeight = FontWeight.Bold)
        }
        // Display current user on top
        item {
            LeaderboardItem(user = currentUser, isHighlighted = true)
        }

        // Display the rest of the users
        items(sortedUsers.size) { i ->

            LeaderboardItem(user = sortedUsers[i])
            if(i != sortedUsers.size-1){
                HorizontalDivider(
                    thickness = 1.dp,)
            }
        }
    }
}