package hu.bme.aut.android.cityofguilds.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Register: Screen("register")
    object PointsMap: Screen("pointsmap")
    object CapturePoint: Screen("capture")
    object CheckPoints: Screen("checkpoints")
    object Main:Screen("mainscreen")
    object Load:Screen("loadscreen")
}

