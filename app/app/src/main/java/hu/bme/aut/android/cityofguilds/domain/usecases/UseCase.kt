package hu.bme.aut.android.cityofguilds.domain.usecases

import hu.bme.aut.android.cityofguilds.data.auth.AuthService
import hu.bme.aut.android.cityofguilds.data.database.PointService
import hu.bme.aut.android.cityofguilds.domain.model.Point
import hu.bme.aut.android.cityofguilds.domain.model.User
import kotlinx.coroutines.flow.first

sealed class UseCase() {
    class ListAllUseCaseUseCase(
        private val authService: AuthService,
        private val  pointService: PointService) :UseCase(){
        suspend operator fun invoke():List<Point>{
            return pointService.getAllPoints()
        }
    }
    class ListOwnUseCaseUseCase(
        private val authService: AuthService,
        private val  pointService: PointService) :UseCase(){
        suspend operator fun invoke():List<Point>{
            return pointService.getCurrentUserPoints()
        }
    }
    class GetCurrentUserUseCase(
        private val authService: AuthService,
        private val  pointService: PointService):UseCase(){
        suspend operator fun invoke(): User?{
            return authService.currentUser.first()
        }
    }
    class GetLeaderboardUseCase(
        private val authService: AuthService,
        private val  pointService: PointService):UseCase(){
        suspend operator fun invoke() : List<User>{
            return pointService.getLeaderboardUsers()
        }
    }
    class CaptureGuildUseCase(
        private val authService: AuthService,
        private val  pointService: PointService):UseCase(){
        suspend operator fun invoke(pointId:String,userId: String):Boolean{
            return pointService.capturePoint(pointId = pointId, userId = userId)
        }
    }
    class RegisterUseCase(
        private val authService: AuthService,
        private val  pointService: PointService):UseCase(){
        suspend operator fun invoke(email:String, password:String){
            authService.signUp(email = email, password = password)
        }
    }
    class LoginUseCase(
        private val authService: AuthService,
        private val  pointService: PointService):UseCase(){
        suspend operator fun invoke(email:String, password:String):Boolean{
            authService.authenticate(email = email, password = password)
            //Return boolean weather login was successful
            return authService.hasUser
        }
    }
    class TryLoginWithTokenUseCase(
        private val authService: AuthService,
        private val  pointService: PointService):UseCase(){
        suspend operator fun invoke():Boolean{
            authService.tryGetTokenFromStore()
            return authService.hasUser
        }
    }


}