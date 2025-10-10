package com.thesomeshkumar.pokepedia.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Created by Somesh Kumar on 26 July, 2025.
 * Copyright ©2025 Somesh Kumar. All rights reserved.
 */
object HttpClientFactory {
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        explicitNulls = false
                        coerceInputValues = true
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "pokeapi.co"
                    path("api/v2/")
                }
                contentType(ContentType.Application.Json)
            }
        }
    }
}
