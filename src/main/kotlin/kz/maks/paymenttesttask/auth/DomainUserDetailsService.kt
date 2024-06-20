package kz.maks.paymenttesttask.auth

import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component("userDetailsService")
class DomainUserDetailsService() : UserDetailsService {

    private val log = LoggerFactory.getLogger(DomainUserDetailsService::class.java)

    override fun loadUserByUsername(username: String): UserDetails {
        throw NotImplementedError("This method should not be used")
    }
}
