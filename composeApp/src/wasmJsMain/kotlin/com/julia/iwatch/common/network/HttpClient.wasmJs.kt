package com.julia.iwatch.common.network

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual fun getClientEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = Js