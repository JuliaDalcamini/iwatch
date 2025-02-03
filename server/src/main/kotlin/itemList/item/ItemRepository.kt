package itemList.item

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import common.database.CrudRepository
import kotlinx.coroutines.flow.toList

/**
 * Repository for managing items.
 */
class ItemRepository(database: MongoDatabase) : CrudRepository<Item>() {

    override val collection: MongoCollection<Item> = database.getCollection("items")

    suspend fun getItemsByItemListId(itemListId: String): List<Item> =
        collection
            .find(Filters.and(Filters.eq("itemListId", itemListId)))
            .toList()
}