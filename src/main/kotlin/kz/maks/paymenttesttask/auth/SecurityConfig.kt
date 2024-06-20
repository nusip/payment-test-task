package kz.astonline.myhealth.auth

import kz.maks.paymenttesttask.auth.jwt.JwtAuthConfigurer
import kz.maks.paymenttesttask.auth.jwt.JwtAuthProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val tokenProvider: JwtAuthProvider
) : WebSecurityConfigurerAdapter() {

    private val log = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Value("\${security.cors.enabled}")
    private val corsEnabled = false

    @Value("\${security.cors.origins}")
    private lateinit var corsAllowedOrigins: List<String>

    @Value("\${security.cors.methods}")
    private lateinit var corsAllowedMethods: List<String>

    @Value("\${security.cors.headers}")
    private lateinit var corsAllowedHeaders: List<String>

    @Value("\${security.cors.credentials}")
    private val corsAllowCredentials = false

    @Bean
    @ConditionalOnProperty(name = ["security.cors.enabled"], havingValue = "true")
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        log.info("cors.enabled = $corsEnabled")
        log.info("cors.origins = ${corsAllowedOrigins.joinToString()}")
        log.info("cors.methods = ${corsAllowedMethods.joinToString()}")
        log.info("cors.headers = ${corsAllowedHeaders.joinToString()}")
        log.info("cors.credentials = $corsAllowCredentials")

        val corsConfig = CorsConfiguration()
        corsConfig.allowedOrigins = corsAllowedOrigins
        corsConfig.allowedMethods = corsAllowedMethods
        corsConfig.allowedHeaders = corsAllowedHeaders
        corsConfig.allowCredentials = corsAllowCredentials
        source.registerCorsConfiguration("/**", corsConfig)

        return source
    }

    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http
            .csrf().disable()
            .httpBasic().disable()
            .exceptionHandling()
        .and()
            .cors()
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .anyRequest().permitAll()
        .and()
            .apply(JwtAuthConfigurer(tokenProvider))
        // @formatter:on
    }
}
