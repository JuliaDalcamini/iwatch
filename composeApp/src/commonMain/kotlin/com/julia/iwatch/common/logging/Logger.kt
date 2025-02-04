package com.julia.iwatch.common.logging

import io.ktor.client.plugins.logging.Logger
import co.touchlab.kermit.Logger as KermitLogger

fun createLogger(tag: String): Logger {
    val logger = KermitLogger.withTag(tag)

    return object : Logger {
        override fun log(message: String) {
            logger.v(message)
        }
    }
}