package auth.refresh

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import common.database.CrudRepository
import kotlinx.coroutines.flow.firstOrNull

class RefreshTokenRepository(database: MongoDatabase) : CrudRepository<RefreshToken>() {

    override val collection: MongoCollection<RefreshToken> = database.getCollection("refreshTokens")

    suspend fun findByRefreshToken(token: String): RefreshToken? =
        collection.find(Filters.eq("refreshToken", token)).firstOrNull()
}