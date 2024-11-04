package hu.bme.aut.android.cityofguilds.dummyClasses

import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DummyAuthService : AuthService {
    private var _currentUserId:String? = null

    override val currentUserId: String?
        get() = _currentUserId
    override val hasUser: Boolean
        get() = _currentUserId != null
    override val currentUser: Flow<User?>
        get() = if(_currentUserId == null) {
            flow{ null }
        } else {
            flow{ User(id = _currentUserId!!) }
        }

    override suspend fun signUp(email: String, password: String) {
        _currentUserId = email + password
    }

    override suspend fun authenticate(email: String, password: String) {
        _currentUserId = email + password
    }

    override suspend fun sendRecoveryEmail(email: String) {
        //Do nothing
    }

    override suspend fun deleteAccount() {
        //Do nothing
    }

    override suspend fun signOut() {
        _currentUserId = null
    }
}