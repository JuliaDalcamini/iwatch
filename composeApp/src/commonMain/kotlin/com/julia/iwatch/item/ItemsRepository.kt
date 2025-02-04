package com.julia.iwatch.item

import com.julia.iwatch.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ItemsRepository(private val client: HttpClient = configuredHttpClient) {

    /**
     * Retrieves all items in a given list.
     */
    suspend fun getAll(listId: String): List<Item> =
        client.get("lists/$listId/items").body()

    /**
     * Updates an item.
     */
    suspend fun update(id: String, listId: String, watched: Boolean): Item =
        client.patch("lists/$listId/items/$id") {
            contentType(ContentType.Application.Json)
            setBody(UpdateItemRequest(watched = watched))
        }.body()

    /**
     * Creates a new item.
     */
    suspend fun create(request: CreateItemRequest, listId: String): Item =
        client.post("lists/$listId/items") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    /**
     * Deletes an item.
     */
    suspend fun delete(id: String, listId: String) {
        client.delete("lists/$listId/items/$id")
    }
}