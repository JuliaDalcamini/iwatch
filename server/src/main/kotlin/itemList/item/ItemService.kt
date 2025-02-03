package itemList.item

import common.networking.error.UnauthorizedError
import itemList.ItemListRepository
import itemList.isOwner

class ItemService(
    private val repository: ItemRepository,
    private val itemListRepository: ItemListRepository
) {
    suspend fun getItemsFromList(itemListId: String): List<Item> {
        return repository.getItemsByItemListId(itemListId)
    }

    suspend fun addItem(request: ItemRequest, loggedUserId: String) {
        if (itemListRepository.isOwner(request.itemListId, loggedUserId)) {
            val newItem = Item(
                name = request.name,
                watched = request.watched,
                itemListId = request.itemListId
            )
            repository.insert(newItem)
        } else throw UnauthorizedError("Only owner can add this item list")
    }

    suspend fun updateItem(idItem: String, request: ItemRequest, loggedUserId: String) {
        val item = getItem(idItem)

        val updatedItemList = item.copy(
            name = request.name,
            watched = request.watched
        )

        if (itemListRepository.isOwner(request.itemListId, loggedUserId)) {
            repository.replaceById(
                id = item.id,
                item = updatedItemList
            )
        } else throw UnauthorizedError("Only owner can update this item list")
    }

    suspend fun deleteItem(idItem: String, loggedUserId: String) {
        val item = getItem(idItem)

        if (itemListRepository.isOwner(item.itemListId, loggedUserId))
            repository.deleteById(item.id)
        else throw UnauthorizedError("Only owner can delete this item list")
    }

    private suspend fun getItem(idItem: String): Item {
        return repository.findById(idItem)
            ?: throw IllegalStateException("Item not found")
    }
}