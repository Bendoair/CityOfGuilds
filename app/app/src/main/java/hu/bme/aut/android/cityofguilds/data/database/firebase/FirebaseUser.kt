package hu.bme.aut.android.cityofguilds.data.database.firebase

import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.cityofguilds.domain.model.User

data class FirebaseUser (
    @DocumentId val id: String = "",
    val numberOfPoints: Int  = 0
)

fun FirebaseUser.asUser() = User(
    id = id,
    numberOfPoints = numberOfPoints,
    isDeveloper = false
)

fun User.asFirebaseUser() = FirebaseUser(
    id = id,
    numberOfPoints = numberOfPoints
)

