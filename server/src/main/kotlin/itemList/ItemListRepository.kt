package itemList

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import common.database.CrudRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

/**
 * Repository for managing item lists.
 */
class ItemListRepository(database: MongoDatabase) : CrudRepository<ItemList>() {

    override val collection: MongoCollection<ItemList> = database.getCollection("itemLists")

    suspend fun existsByUserIdAndItemListId(itemListId: String, userId: String): Boolean =
        collection
            .find(Filters.and(Filters.eq("userId", userId), Filters.eq("itemListId", itemListId)))
            .firstOrNull() != null

    suspend fun getItemListByUserId(userId: String): List<ItemList> =
        collection
            .find(Filters.and(Filters.eq("userId", userId)))
            .toList()
}