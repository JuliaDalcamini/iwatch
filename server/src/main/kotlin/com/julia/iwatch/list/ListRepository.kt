package com.julia.iwatch.list

import com.julia.iwatch.common.database.CrudRepository
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

/**
 * Repository for managing item lists.
 */
class ListRepository(database: MongoDatabase) : CrudRepository<ItemListEntity>() {

    override val collection: MongoCollection<ItemListEntity> = database.getCollection("itemLists")

    suspend fun findByUserId(userId: String): List<ItemListEntity> =
        collection
            .find(Filters.eq("userId", userId))
            .toList()
}