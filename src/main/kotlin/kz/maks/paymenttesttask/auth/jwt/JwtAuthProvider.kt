package kz.maks.paymenttesttask.auth.jwt

import kz.maks.paymenttesttask.auth.AuthUser
import kz.maks.paymenttesttask.model.User
import org.springframework.security.core.Authentication

const val TOKEN_HEADER = "Authorization"
const val TOKEN_PREFIX = "Bearer "

interface JwtAuthProvider {

    fun createToken(user: User): String

    fun authenticate(tokenString: String): Authentication?

}

fun User.toAuthUser(authToken: String): AuthUser {
    return AuthUser(
        authToken = authToken,
        id = this.id,
        username = this.username,
    )
}
