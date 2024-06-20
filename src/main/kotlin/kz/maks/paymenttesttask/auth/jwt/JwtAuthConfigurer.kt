package kz.maks.paymenttesttask.auth.jwt

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtAuthConfigurer(private val tokenAuthProvider: JwtAuthProvider) :
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val customFilter = JwtAuthFilter(tokenAuthProvider)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}
