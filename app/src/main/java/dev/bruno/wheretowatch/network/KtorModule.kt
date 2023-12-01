package dev.bruno.wheretowatch.network

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dev.bruno.wheretowatch.BuildConfig
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.di.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

@Module
@ContributesTo(AppScope::class)
object KtorModule {

    @Provides
    @SingleIn(AppScope::class)
    fun providesKtor(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config { callTimeout(30, TimeUnit.SECONDS) }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org/3"
                }
                header("accept", "application/json")
                header("Authorization", BuildConfig.API_KEY)
            }
            install(Resources)
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Logger Ktor => $message")
                    }
                }
                level = LogLevel.BODY
            }
        }
    }
}