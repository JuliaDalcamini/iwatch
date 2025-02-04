package com.julia.iwatch.common.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import io.ktor.http.decodeURLPart
import io.ktor.http.encodeURLPathPart
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class SerializableNavType<T>(type: KType) : NavType<T>(type.isMarkedNullable) {

    private val serializer = serializer(type)

    @Suppress("UNCHECKED_CAST")
    override fun parseValue(value: String): T =
        Json.decodeFromString(serializer, value.decodeURLPart()) as T

    override fun serializeAsValue(value: T): String =
        Json.encodeToString(serializer, value).encodeURLPathPart()

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { parseValue(it) }
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, serializeAsValue(value))
    }
}

inline fun <reified T> serializableNavType() = SerializableNavType<T>(typeOf<T>())