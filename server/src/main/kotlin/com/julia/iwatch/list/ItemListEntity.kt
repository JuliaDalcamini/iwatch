package com.julia.iwatch.list

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ItemListEntity(
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val userId: String
)

/**
 * Converts entity to model.
 */
fun ItemListEntity.toItemList(itemCount: Int) = ItemList(
    id = this.id.toString(),
    name = this.name,
    userId = this.userId,
    itemCount = itemCount
)