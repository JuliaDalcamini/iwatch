package com.julia.iwatch.list

import com.julia.iwatch.common.networking.error.UnauthorizedError
import com.julia.iwatch.item.ItemRepository

class ListService(
    private val repository: ListRepository,
    private val itemRepository: ItemRepository
) {
    suspend fun get(listId: String, loggedUserId: String): ItemList {
        val list = repository.findById(listId)

        if (list?.userId == loggedUserId) {
            val itemCount = itemRepository.countByListId(listId)
            return list.toItemList(itemCount)
        } else {
            throw UnauthorizedError("Only owner access this list")
        }
    }

    suspend fun getAll(loggedUserId: String): List<ItemList> {
        return repository.findByUserId(loggedUserId).map { list ->
            val itemCount = itemRepository.countByListId(list.id)
            list.toItemList(itemCount)
        }
    }

    suspend fun create(request: ItemListRequest, loggedUserId: String): ItemList {
        return repository.insertAndGet(
            ItemListEntity(
                name = request.name,
                userId = loggedUserId
            )
        ).toItemList(0)
    }

    suspend fun update(listId: String, request: ItemListRequest, loggedUserId: String): ItemList {
        val list = repository.findById(listId)

        if (list?.userId == loggedUserId) {
            val updatedItemList = list.copy(name = request.name)
            val itemCount = itemRepository.countByListId(list.id)

            return repository.replaceByIdAndGet(
                id = list.id.toString(),
                item = updatedItemList
            ).toItemList(itemCount)
        } else {
            throw UnauthorizedError("Only owner can update this list")
        }
    }

    suspend fun delete(listId: String, loggedUserId: String) {
        val list = repository.findById(listId)

        if (list?.userId == loggedUserId) {
            itemRepository.deleteByListId(list.id)
            repository.deleteById(list.id)
        } else {
            throw UnauthorizedError("Only owner can delete this list")
        }
    }
}