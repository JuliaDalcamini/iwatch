package com.julia.iwatch.register

import com.julia.iwatch.auth.UserCredentials

class RegisterRequest (
    val firstName: String,
    val lastName: String,
    val userCredentials: UserCredentials
)