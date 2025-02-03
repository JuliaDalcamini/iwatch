package auth

import auth.JwtParams.USER_ID_CLAIM
import auth.refresh.RefreshToken
import auth.refresh.RefreshTokenRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.julia.iwatch.auth.LoginResponse
import com.julia.iwatch.auth.TokenPair
import com.julia.iwatch.auth.refresh.RefreshTokensRequest
import com.julia.iwatch.login.LoginRequest
import common.networking.error.UnauthorizedError
import org.mindrot.jbcrypt.BCrypt
import java.util.Date
import plugins.jwtVerifier
import user.UserRepository

class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    suspend fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)

        if (user != null && BCrypt.checkpw(request.password, user.password)) {
            return LoginResponse(
                userId = user.id.toString(),
                tokens = generateTokenPair(user.id.toString())
            )
        } else {
            throw UnauthorizedError("Unable to authenticate")
        }
    }

    suspend fun refreshTokens(request: RefreshTokensRequest): TokenPair {
        val decodedToken = verifyRefreshToken(request.refreshToken)
        val savedToken = refreshTokenRepository.findByRefreshToken(request.refreshToken)

        if (savedToken?.userId != null && decodedToken?.userId == savedToken.userId) {
            val tokenPair = generateTokenPair(savedToken.userId)

            refreshTokenRepository.deleteById(savedToken.id)

            return tokenPair
        } else {
            throw UnauthorizedError("Unable to authenticate")
        }
    }

    private fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJwt = jwtVerifier.verify(token)

        return if (decodedJwt.audience.contains(JwtParams.AUDIENCE)) {
            decodedJwt
        } else {
            null
        }
    }

    private suspend fun generateTokenPair(userId: String): TokenPair {
        val accessToken = generateJwtToken(userId, ACCESS_TOKEN_LIFETIME)
        val refreshToken = generateJwtToken(userId, REFRESH_TOKEN_LIFETIME)

        refreshTokenRepository.insert(
            RefreshToken(
                refreshToken = refreshToken,
                userId = userId
            )
        )

        return TokenPair(accessToken, refreshToken)
    }

    private fun generateJwtToken(userId: String, lifetime: Long): String =
        JWT.create()
            .withAudience(JwtParams.AUDIENCE)
            .withIssuer(JwtParams.DOMAIN)
            .withClaim(USER_ID_CLAIM, userId)
            .withExpiresAt(Date(System.currentTimeMillis() + lifetime))
            .sign(Algorithm.HMAC256(JwtParams.SECRET))

    companion object {
        private const val ACCESS_TOKEN_LIFETIME = 18000L
        private const val REFRESH_TOKEN_LIFETIME = 86400000L
    }
}