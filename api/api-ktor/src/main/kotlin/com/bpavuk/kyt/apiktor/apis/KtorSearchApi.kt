package com.bpavuk.kyt.apiktor.apis

import com.bpavuk.kyt.apiktor.serialization.search.SearchRequestSerializable
import com.bpavuk.kyt.apiktor.serialization.search.SearchResult
import com.bpavuk.kyt.apiktor.serialization.search.SearchResultSerializable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

public class KtorSearchApi(
    private val baseUrl: Url,
    private val httpClient: HttpClient,
    private val json: Json
) {
    public suspend fun search(request: SearchRequestSerializable): List<SearchResult> {
        val response = httpClient.post("$baseUrl/search/?alt=json") {
            setBody(request)
        }
        val result = response.body<SearchResultSerializable>()
        return json.decodeFromJsonElement<List<SearchResult>>(result.contents)
    }
}
