package kz.maks.paymenttesttask.controller

import kz.maks.paymenttesttask.auth.jwt.JwtAuthProvider
import kz.maks.paymenttesttask.auth.jwt.JwtAuthResponse
import kz.maks.paymenttesttask.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val tokenAuthProvider: JwtAuthProvider,
    private val userRepository: UserRepository
) {

    @PostMapping("/test-auth")
    fun authenticate(
        @RequestParam username: String
    ): JwtAuthResponse {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User $username was not found")

        val jwtToken = tokenAuthProvider.createToken(user)

        return JwtAuthResponse(
            jwtToken = jwtToken
        )
    }
}
