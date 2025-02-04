package com.julia.iwatch.plugins

import com.julia.iwatch.auth.AuthService
import com.julia.iwatch.auth.refresh.RefreshTokenRepository
import com.julia.iwatch.item.ItemRepository
import com.julia.iwatch.item.ItemService
import com.julia.iwatch.list.ListRepository
import com.julia.iwatch.list.ListService
import com.julia.iwatch.user.UserRepository
import com.julia.iwatch.user.UserService
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            module {
                single<MongoClient> { MongoClient.create("mongodb://localhost:27017") }
                single<MongoDatabase> { get<MongoClient>().getDatabase("iwatch-db") }
            },
            module {
                single<UserRepository> { UserRepository(get()) }
                single<UserService> { UserService(get()) }
                single<RefreshTokenRepository> { RefreshTokenRepository(get()) }
                single<AuthService> { AuthService(get(), get()) }
                single<ItemService> { ItemService(get(), get()) }
                single<ItemRepository> { ItemRepository(get()) }
                single<ListService> { ListService(get(), get()) }
                single<ListRepository> { ListRepository(get()) }
            }
        )
    }
}