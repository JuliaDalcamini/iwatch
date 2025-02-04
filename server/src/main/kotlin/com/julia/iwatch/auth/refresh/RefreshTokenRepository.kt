package com.julia.iwatch.auth.refresh

import com.julia.iwatch.common.database.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class RefreshTokenRepository(database: MongoDatabase) : CrudRepository<RefreshToken>() {

    override val collection: MongoCollection<RefreshToken> = database.getCollection("refreshTokens")

    suspend fun findByRefreshToken(token: String): RefreshToken? =
        collection.find(Filters.eq("refreshToken", token)).firstOrNull()
}