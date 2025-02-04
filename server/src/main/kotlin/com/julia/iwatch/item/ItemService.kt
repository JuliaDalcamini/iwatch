package com.julia.iwatch.item

import com.julia.iwatch.common.networking.error.UnauthorizedError
import com.julia.iwatch.list.ListRepository
import io.ktor.server.plugins.NotFoundException

class ItemService(
    private val repository: ItemRepository,
    private val listRepository: ListRepository
) {
    suspend fun get(id: String, listId: String, loggedUserId: String): Item {
        val list = listRepository.findById(listId)

        if (list?.userId == loggedUserId) {
            return repository.findByIdAndListId(id, listId)?.toItem()
                ?: throw NotFoundException("Item not found")
        }

        throw UnauthorizedError("Only owner can access list items")
    }

    suspend fun getAll(listId: String, loggedUserId: String): List<Item> {
        val list = listRepository.findById(listId)

        if (list?.userId == loggedUserId) {
            return repository.findByListId(listId).map { it.toItem() }
        } else {
            throw UnauthorizedError("Only owner can access list items")
        }
    }

    suspend fun addItem(request: CreateItemRequest, listId: String, loggedUserId: String): Item {
        val list = listRepository.findById(listId)

        if (list?.userId == loggedUserId) {
            val newItem = ItemEntity(
                name = request.name,
                year = request.year,
                posterUrl = request.posterUrl,
                watched = false,
                tmdbId = request.tmdbId,
                listId = listId
            )

            return repository.insertAndGet(newItem).toItem()
        } else {
            throw UnauthorizedError("Only owner can add items to this list")
        }
    }

    suspend fun updateItem(id: String, listId: String, request: UpdateItemRequest, loggedUserId: String): Item {
        val list = listRepository.findById(listId)

        if (list?.userId == loggedUserId) {
            val item = repository.findByIdAndListId(id, listId)
                ?: throw NotFoundException("Item not found")

            val updatedItem = item.copy(watched = request.watched)

            return repository.replaceByIdAndGet(
                id = id,
                item = updatedItem
            ).toItem()
        }

        throw UnauthorizedError("Only owner can update this item list")
    }

    suspend fun deleteItem(id: String, listId: String, loggedUserId: String) {
        val list = listRepository.findById(listId)

        if (list?.userId == loggedUserId) {
            repository.deleteByIdAndListId(id, listId)
        } else {
            throw UnauthorizedError("Only owner can delete this item list")
        }
    }
}