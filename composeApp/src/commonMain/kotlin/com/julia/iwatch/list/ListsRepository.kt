package com.julia.iwatch.list

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

class ListsRepository(private val client: HttpClient = configuredHttpClient) {

    /**
     * Retrieves all lists available to the current user.
     */
    suspend fun getAll(): List<ItemList> =
        client.get("lists").body()

    /**
     * Updates a list with a new name.
     */
    suspend fun update(listId: String, newName: String): ItemList =
        client.patch("lists/$listId") {
            contentType(ContentType.Application.Json)
            setBody(ItemListRequest(name = newName))
        }.body()

    /**
     * Creates a new list.
     */
    suspend fun create(name: String): ItemList =
        client.post("lists") {
            contentType(ContentType.Application.Json)
            setBody(ItemListRequest(name = name))
        }.body()

    /**
     * Deletes a list.
     */
    suspend fun delete(listId: String) {
        client.delete("lists/$listId")
    }
}