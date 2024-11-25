package hu.bme.aut.android.cityofguilds.data.auth

import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val currentUserId: String?

    val hasUser: Boolean

    val currentUser: Flow<User?>

    val currentToken: String?

    suspend fun signUp(
        email: String, password: String,
    )

    suspend fun authenticate(
        email: String,
        password: String
    )

    suspend fun tryGetTokenFromStore()

/*

    suspend fun sendRecoveryEmail(email: String)

    suspend fun deleteAccount()

*/

    suspend fun signOut()

    companion object{
        const val KEY_ALIAS = "auth_token"
        const val SECURITY_PREFERENCE = "secure_prefs"
    }
}