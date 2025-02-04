package com.julia.iwatch.item

import com.julia.iwatch.common.database.CrudRepository
import com.julia.iwatch.common.database.error.ItemNotFoundException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

/**
 * Repository for managing items.
 */
class ItemRepository(database: MongoDatabase) : CrudRepository<ItemEntity>() {

    override val collection: MongoCollection<ItemEntity> = database.getCollection("items")

    suspend fun findByListId(listId: String): List<ItemEntity> =
        collection
            .find(Filters.eq("listId", listId))
            .toList()

    suspend fun findByIdAndListId(id: String, listId: String) =
        collection
            .find(
                Filters.and(
                    Filters.eq("_id", ObjectId(id)),
                    Filters.eq("listId", listId)
                )
            )
            .firstOrNull()

    suspend fun deleteByIdAndListId(id: String, listId: String) {
        val result = collection.deleteOne(
            Filters.and(
                Filters.eq("_id", ObjectId(id)),
                Filters.eq("listId", listId)
            )
        )

        if (result.deletedCount < 1) {
            throw ItemNotFoundException()
        }
    }

    suspend fun deleteByListId(listId: ObjectId) {
        collection.deleteMany(Filters.eq("listId", listId.toString()))
    }

    suspend fun countByListId(listId: String) =
        collection.countDocuments(Filters.eq("listId", listId)).toInt()

    suspend fun countByListId(listId: ObjectId) =
        collection.countDocuments(Filters.eq("listId", listId.toString())).toInt()
}