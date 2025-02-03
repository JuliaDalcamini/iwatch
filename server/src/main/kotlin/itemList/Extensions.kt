package itemList

suspend fun ItemListRepository.isOwner(idItemList: String, loggedUserId: String): Boolean =
    existsByUserIdAndItemListId(idItemList, loggedUserId)