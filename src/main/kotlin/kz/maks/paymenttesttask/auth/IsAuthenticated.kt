package kz.maks.paymenttesttask.auth

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION
import org.springframework.security.access.prepost.PreAuthorize

@Target(FUNCTION, CLASS)
@Retention(RUNTIME)
@PreAuthorize("isAuthenticated()")
@MustBeDocumented
annotation class IsAuthenticated
