package itemList.item

import kotlinx.serialization.Serializable

@Serializable
data class ItemRequest(
    val name: String,
    val watched: Boolean = false,
    val itemListId: String
)