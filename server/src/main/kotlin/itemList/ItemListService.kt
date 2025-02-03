package itemList

import common.networking.error.UnauthorizedError

class ItemListService(
    private val repository: ItemListRepository
) {
    suspend fun getAll(loggedUserId: String): List<ItemList> {
        return repository.getItemListByUserId(loggedUserId)
    }

    suspend fun createItemList(request: ItemListRequest, loggedUserId: String): ItemList {
        return repository.insertAndGet(
            ItemList(
                name = request.name,
                userId = loggedUserId
            )
        )
    }

    suspend fun updateItemList(idItemList: String, request: ItemListRequest, loggedUserId: String) {
        val list = getItemList(idItemList)

        if (repository.isOwner(idItemList, loggedUserId)) {
            val updatedItemList = list.copy(
                name = request.name
            )

            repository.replaceById(
                id = list.id,
                item = updatedItemList
            )
        } else {
            throw UnauthorizedError("Only owner can update this list")
        }
    }

    suspend fun deleteItemList(idItemList: String, loggedUserId: String) {
        val list = getItemList(idItemList)

        if (repository.isOwner(idItemList, loggedUserId)) {
            repository.deleteById(list.id)
        } else {
            throw UnauthorizedError("Only owner can delete this list")
        }
    }

    private suspend fun getItemList(idItemList: String): ItemList {
        return repository.findById(idItemList)
            ?: throw IllegalStateException("Item list not found")
    }
}