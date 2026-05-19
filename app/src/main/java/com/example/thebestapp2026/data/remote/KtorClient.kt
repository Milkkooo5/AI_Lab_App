package com.example.thebestapp2026.data.remote


import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.json.Json

object KtorClient {

    const val BASE_URL = "http://10.0.2.2:8080"

    val client = HttpClient(Android) {

        install(ContentNegotiation) {

            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}