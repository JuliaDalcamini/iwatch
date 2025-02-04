package com.julia.iwatch.user

import com.julia.iwatch.auth.RegisterRequest
import com.julia.iwatch.common.networking.error.ConflictError
import org.mindrot.jbcrypt.BCrypt

class UserService(
    private val repository: UserRepository
) {

    suspend fun register(request: RegisterRequest) {
        val userCredentials = request.userCredentials
        val existingUser = repository.findByEmail(userCredentials.email)

        if (existingUser == null) {
            repository.insert(
                User(
                    firstName = request.firstName,
                    lastName = request.lastName,
                    email = userCredentials.email,
                    password = BCrypt.hashpw(userCredentials.password, BCrypt.gensalt(PASSWORD_HASHING_ROUNDS))
                )
            )
        } else {
            throw ConflictError("User already exists")
        }
    }

    companion object {
        private const val PASSWORD_HASHING_ROUNDS = 12
    }
}