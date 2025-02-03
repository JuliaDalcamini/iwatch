package auth

import auth.JwtParams.USER_ID_CLAIM
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

val ApplicationCall.authenticatedUserId: String
    get() = principal<JWTPrincipal>()?.payload?.getClaim(USER_ID_CLAIM)?.asString()
        ?: throw IllegalStateException("The user is not authenticated")

val DecodedJWT.userId: String?
    get() = claims?.get(USER_ID_CLAIM)?.asString()