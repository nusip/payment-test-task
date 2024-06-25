package kz.maks.paymenttesttask.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component("userDetailsService")
class DomainUserDetailsService() : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        throw NotImplementedError("This method should not be used")
    }
}
