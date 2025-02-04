package com.julia.iwatch.search

import com.julia.iwatch.common.navigation.serializableNavType
import com.julia.iwatch.list.ItemList
import kotlin.reflect.typeOf
import kotlinx.serialization.Serializable

@Serializable
data class SearchRoute(val list: ItemList) {

    companion object {
        val typeMap = mapOf(typeOf<ItemList>() to serializableNavType<ItemList>())
    }
}