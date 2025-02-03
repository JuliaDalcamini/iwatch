package plugins

import auth.AuthService
import auth.refresh.RefreshTokenRepository
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import itemList.ItemListRepository
import itemList.ItemListService
import itemList.item.ItemRepository
import itemList.item.ItemService
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import user.UserRepository
import user.UserService

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
                single<ItemListService> { ItemListService(get()) }
                single<ItemListRepository> { ItemListRepository(get()) }
            }
        )
    }
}