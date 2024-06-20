package kz.maks.paymenttesttask.auth.jwt

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JwtAuthFilter(private val tokenAuthProvider: JwtAuthProvider) : GenericFilterBean() {

    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        val tokenString = resolveTokenFromRequest(req as HttpServletRequest)

        if (tokenString != null) {
            SecurityContextHolder.getContext().authentication = tokenAuthProvider.authenticate(tokenString)
        }

        filterChain.doFilter(req, res)
    }

    private fun resolveTokenFromRequest(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader(TOKEN_HEADER)
        return if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            bearerToken.substring(TOKEN_PREFIX.length, bearerToken.length)
        } else null
    }
}
