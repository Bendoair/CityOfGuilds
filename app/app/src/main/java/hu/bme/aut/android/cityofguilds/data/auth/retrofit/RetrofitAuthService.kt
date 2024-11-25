package hu.bme.aut.android.cityofguilds.data.auth.retrofit

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.auth.AuthService.Companion.KEY_ALIAS
import hu.bme.aut.android.cityofguilds.data.auth.AuthService.Companion.SECURITY_PREFERENCE
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import javax.inject.Inject

class RetrofitAuthService @Inject constructor(
    private val retrofitAuthenticator: RetrofitAuthenticator,
    @ApplicationContext private val context: Context,
) : AuthService {

    override val currentUserId: String? get() = _currentUserId
    override val hasUser: Boolean get() = _currentUserId != null
    override val currentUser: Flow<User?>
        get() = runBlocking {
            flow{ emit(
                try {
                    retrofitAuthenticator.authenticate(TokenDTO(currentToken))
                }catch (e:HttpException){
                    null
                }
                ) }

        }
    override val currentToken: String
        get() = _currentToken?:""

    private var _currentUserId :String? = null
    private var _currentToken : String? = null


    override suspend fun signUp(email: String, password: String) {
        val token = retrofitAuthenticator.register(AuthDTO(email, password)).token
        _currentToken = if(token != ""){token}else{null}
        storeToken(_currentToken)
    }

    override suspend fun authenticate(email: String, password: String) {

        Log.i("authentication", "Auth called, calling retrofit service")

        var token = ""

        try{
            token = retrofitAuthenticator.login(AuthDTO(email, password)).token
            _currentToken = if (token != "") { token } else { null }
            storeToken(_currentToken)
            try{ _currentUserId = retrofitAuthenticator.authenticate(TokenDTO(token))?.id }
            catch (e:HttpException){
                Log.e("authentication", "Authenticate caught exception", e)
            }
        }catch (e:HttpException){
            Log.e("authentication", "Auth login caught exception", e)
        }catch (e:Exception){
            Log.e("authentication", "Something very wrong", e)
        }
        Log.i("authentication", "Auth called, retrofit service finished token:$token")


    }

    private fun storeToken(currentToken: String?) {
        val sharedPreferences = context.getSharedPreferences(SECURITY_PREFERENCE, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_ALIAS, currentToken?:"").apply()
    }

    override suspend fun tryGetTokenFromStore() {
        Log.i("Token Store", "Trying to get token from store")
        val sharedPreferences = context.getSharedPreferences(SECURITY_PREFERENCE, Context.MODE_PRIVATE)
        _currentToken = sharedPreferences.getString(KEY_ALIAS, null)
        if(_currentToken!=null){
            try{ _currentUserId = retrofitAuthenticator.authenticate(TokenDTO(_currentToken!!))?.id }
            catch (e:HttpException){
                Log.e("authentication", "Authenticate caught exception", e)
            }
        }

    }

    override suspend fun signOut() {
        _currentUserId = null
        _currentToken = null
    }


}