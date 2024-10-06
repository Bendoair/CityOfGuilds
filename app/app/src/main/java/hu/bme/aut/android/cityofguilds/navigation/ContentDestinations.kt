package hu.bme.aut.android.cityofguilds.navigation

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardCommandKey
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import hu.bme.aut.android.cityofguilds.R

enum class ContentDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(R.string.text_home, Icons.Default.Home, R.string.text_home),
    LIST(R.string.text_your_points_list, Icons.Default.KeyboardCommandKey, R.string.text_your_points_list),
    MAP(R.string.text_see_map, Icons.Default.Map, R.string.text_see_map),
    CAPTURE(R.string.text_capture_point, Icons.Default.AddModerator, R.string.text_capture_point),
    DEVTOOLS(R.string.devtools, Icons.Default.DeveloperMode, R.string.devtools)
}