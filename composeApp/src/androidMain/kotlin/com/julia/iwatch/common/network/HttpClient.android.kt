package com.julia.iwatch.common.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun getClientEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = CIO