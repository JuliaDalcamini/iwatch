package com.julia.iwatch.user

import com.julia.iwatch.common.database.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

/**
 * Repository for managing users.
 */
class UserRepository(database: MongoDatabase) : CrudRepository<User>() {

    override val collection: MongoCollection<User> = database.getCollection("users")

    suspend fun findByEmail(email: String): User? =
        collection.find(Filters.eq("email", email)).firstOrNull()
}