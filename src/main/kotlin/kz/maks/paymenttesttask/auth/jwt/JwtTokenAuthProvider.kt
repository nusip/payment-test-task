package kz.maks.paymenttesttask.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import kz.maks.paymenttesttask.model.User
import kz.maks.paymenttesttask.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class JwtTokenAuthProvider(
    private val userRepository: UserRepository,

    @Value("\${security.jwt.token.secret-key}")
    private val secretKey: String,

    @Value("\${security.jwt.token.expire-length}")
    private val validityInSeconds: Long

) : JwtAuthProvider {

    override fun createToken(user: User): String {
        val claims = Jwts.claims().setSubject(user.username)
        val now = Date()
        val validity = Date(now.time + validityInSeconds * 1000)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    override fun authenticate(tokenString: String): Authentication? {
        val claims = parse(tokenString)

        if (claims != null) {
            val username = claims.body.subject

            try {
                val authPatient = userRepository.findByUsername(username)
                    ?.toAuthUser(tokenString)
                    ?: throw UsernameNotFoundException("User $username was not found")

                return UsernamePasswordAuthenticationToken(authPatient, "", authPatient.authorities)
            } catch (_: UsernameNotFoundException) {
            }
        }
        return null
    }

    private fun parse(tokenString: String): Jws<Claims>? {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenString)
        } catch (_: JwtException) {
        } catch (_: IllegalArgumentException) {
        }
        return null
    }
}
