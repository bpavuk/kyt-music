package com.bpavuk.kyt.apiktor

import com.bpavuk.kyt.apiktor.requests.search.SearchEngine
import com.bpavuk.kyt.engine.YTMusicRequestsEngine
import com.bpavuk.kyt.engine.requests.SearchRequest
import com.bpavuk.kyt.engine.requests.YTMusicRequest
import com.tfowl.ktor.client.features.JsoupPlugin
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

@OptIn(ExperimentalSerializationApi::class)
public class KtorYTMusicEngine(
    baseUrl: Url,
    httpClient: HttpClient = HttpClient(CIO),
    json: Json = Json,
    private val logging: Boolean
) : YTMusicRequestsEngine {
    public constructor(
        baseUrl: String,
        httpClient: HttpClient = HttpClient(CIO),
        json: Json = Json,
        logging: Boolean
    ) : this(Url(baseUrl), httpClient, json, logging)

    private val _json = Json(json) {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    private val baseUrl = Url("${baseUrl.protocol.name}://${baseUrl.host}/youtubei/v1")

    private var httpClient = httpClient.config {
        expectSuccess = false

        install(UserAgent) {
            agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0"
        }
        install(ContentEncoding) {
            deflate(1F)
            gzip(0.9F)
        }
        install(JsoupPlugin) {
            parsers[ContentType.Text.Html] = Parser.htmlParser()
        }
        install(ContentNegotiation) {
            json(_json)
        }
        defaultRequest {
            header(HttpHeaders.Origin, baseUrl.protocol.name + "://" + baseUrl.host)
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Connection, "keep-alive")
            header("x-goog-visitor-id", gHeader)
            cookie("CONSENT", "YES+1")
            url("https://music.youtube.com/")
        }
    }

    private var gHeader: String? = null

    public suspend fun setup() {
        coroutineScope {
            launch {
                if (logging) httpClient.configLogging()
                val ytcfgRegex = Regex("ytcfg\\.set\\s*\\(\\s*(\\{.+?})\\s*\\)\\s*;")
                val gHeaderResponse = async(Dispatchers.IO) {
                    this@KtorYTMusicEngine.httpClient
                        .get("/")
                        .body<Document>()
                }.await().html()

                var match = ytcfgRegex.find(gHeaderResponse)?.value
                    ?: throw IllegalStateException("YT user ID was not found")
                match = Regex("\\{.*}").find(match)?.value
                    ?: throw IllegalStateException("YT user ID was not found")

                val id = this@KtorYTMusicEngine._json.parseToJsonElement(match).jsonObject["VISITOR_DATA"]
                gHeader = id?.toString() ?: throw IllegalStateException("YT user ID was not found")

                httpClient = httpClient.config {
                    defaultRequest {
                        url(baseUrl.toString())
                    }
                }
            }
        }
    }

    private val search: SearchEngine get() {
        if (logging) httpClient.configLogging()
        return SearchEngine(this.baseUrl, this.httpClient, this._json)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> execute(request: YTMusicRequest<T>): T = when (request) {
        is SearchRequest -> search.search(request) as T
    }

    @Suppress("todo", "unused")
    private fun notSupported(): Nothing = TODO("This request is not supported yet!")
}

private fun HeadersBuilder.forEach(function: (String, List<String>) -> Unit) {
    this.build().forEach(function)
}

private fun HttpClient.configLogging() = plugin(HttpSend).intercept { request ->
    println("[ADDRESS] ${request.url.buildString()}")
    request.headers.forEach { s: String, strings: List<String> ->
        println("[HEADER $s]: ${strings.joinToString(", ")}")
    }
    println("[BODY]: ${request.body}")
    println("[PARAMS]: ${request.url.encodedParameters.build()}")
    val originalCall = execute(request)
    originalCall
}
