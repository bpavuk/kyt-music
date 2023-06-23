package com.bpavuk.kyt.apiktor.requests.search


import com.bpavuk.kyt.apiktor.apis.KtorSearchApi
import com.bpavuk.kyt.apiktor.serializable
import com.bpavuk.kyt.apiktor.toMusicItem
import com.bpavuk.kyt.engine.requests.SearchRequest
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

public class SearchEngine(
    baseUrl: Url,
    httpClient: HttpClient,
    json: Json
) {
    private val base = KtorSearchApi(baseUrl, httpClient, json)

    public suspend fun search(request: SearchRequest): SearchRequest.Response =
        SearchRequest.Response(base.search(request.serializable()).map { it.toMusicItem() })

}
