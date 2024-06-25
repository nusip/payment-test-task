package kz.maks.paymenttesttask.auth

import org.springframework.security.core.userdetails.User

class AuthUser(
    val authToken: String,
    val id: Long,
    username: String
) : User(username, "", emptyList())