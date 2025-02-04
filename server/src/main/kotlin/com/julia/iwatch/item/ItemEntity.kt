package com.julia.iwatch.item

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ItemEntity(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val year: String?,
    val posterUrl: String?,
    val watched: Boolean,
    val tmdbId: String,
    val listId: String
)

/**
 * Converts entity to model.
 */
fun ItemEntity.toItem() = Item(
    id = this.id.toString(),
    name = this.name,
    year = this.year,
    posterUrl = this.posterUrl,
    watched = this.watched,
    tmdbId = this.tmdbId,
    listId = this.listId
)