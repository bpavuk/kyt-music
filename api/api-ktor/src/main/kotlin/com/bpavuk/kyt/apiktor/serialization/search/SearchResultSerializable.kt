package com.bpavuk.kyt.apiktor.serialization.search

import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.song.Duration
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
public data class SearchResultSerializable(
    @Serializable(with = UnwrappingTransformerSerializer::class)
    public val contents: JsonElement
)

private object UnwrappingTransformerSerializer :
    JsonTransformingSerializer<JsonElement>(JsonElement.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        var jsonResult: JsonElement? = element.jsonObject["tabbedSearchResultsRenderer"]
            ?.jsonObject?.get("tabs")
            ?.jsonArray?.get(0)  // TODO: implement scopes when auth is implemented
            ?.jsonObject?.get("tabRenderer")
            ?.jsonObject?.get("content")
            ?: element
        jsonResult = jsonResult?.jsonObject?.get("sectionListRenderer")
            ?.jsonObject?.get("contents")

        require(jsonResult?.jsonArray?.toList() != null) { "Invalid response from YouTube Music API" }

        val result = mutableListOf<SearchResult>()
        for (res in jsonResult?.jsonArray?.toList()!!) {
            if (res.jsonObject["musicCardShelfRenderer"] != null)
                result.addAll(res.jsonObject["musicCardShelfRenderer"]!!.jsonObject.parseMusicCardShelfRenderer())
            if (res.jsonObject["musicShelfRenderer"] != null)
                result.addAll(res.jsonObject["musicShelfRenderer"]!!.jsonObject.parseMusicShelfRenderer())
        }

        return Json.encodeToJsonElement(result)
    }
}

private fun JsonObject.parseMusicCardShelfRenderer(): List<SearchResult> {
    val resultType = jsonObject["subtitle"]
        ?.jsonObject?.get("runs")
        ?.jsonArray?.get(0)
        ?.jsonObject?.get("text")
        ?.jsonPrimitive?.content?.toLowerCasePreservingASCIIRules()
    requireNotNull(resultType)

    val titleOrName = jsonObject["title"]!!
        .jsonObject["runs"]!!
        .jsonArray[0]
        .jsonObject["text"]!!
        .jsonPrimitive.content

    val result: MutableList<SearchResult> = mutableListOf()
    when (resultType) {
        "artist" -> {
            val id = jsonObject["title"]!!
                .jsonObject["runs"]!!
                .jsonArray[0]
                .jsonObject["navigationEndpoint"]!!
                .jsonObject["browseEndpoint"]!!
                .jsonObject["browseId"]!!
                .jsonPrimitive.content
            val thumbnails = jsonObject["thumbnail"]!!
                .jsonObject["musicThumbnailRenderer"]!!
                .jsonObject["thumbnail"]!!
                .jsonObject["thumbnails"]!!
                .jsonArray.map<JsonElement, Thumbnail> { Json.decodeFromJsonElement(it) }
            result += SearchResult.SearchResultArtist(
                id,
                titleOrName,
                thumbnails
            )
        }
        in listOf("video", "song") -> {
            val videoId = jsonObject["onTap"]!!
                .jsonObject["watchEndpoint"]!!
                .jsonObject["videoId"]!!
                .jsonPrimitive.content
            val duration = jsonObject["subtitle"]!!
                .jsonObject["runs"]!!
                .jsonArray[6]
                .jsonObject["text"]!!
                .jsonPrimitive.content
            result += SearchResult.SearchResultSong(
                title = titleOrName,
                watchId = videoId,
                duration = Duration(duration),
                thumbnails = getThumbnails()
            )
        }
        "album" -> TODO()
        else -> throw IllegalStateException("Invalid response from YouTube Music API")
    }
    return result
}

private fun JsonObject.parseMusicShelfRenderer(): List<SearchResult> {
    println(this) // TODO
    val type = jsonObject["title"]!!
        .jsonObject["runs"]!!
        .jsonArray[0]
        .jsonObject["text"]!!
        .jsonPrimitive
        .content
        .toLowerCasePreservingASCIIRules()
        .removeSuffix("s")
    println(type)
    val content = jsonObject["contents"]!!.jsonArray.toList()

    val result: MutableList<SearchResult> = mutableListOf()

    for (p in content) {
        val piece = p.jsonObject["musicResponsiveListItemRenderer"]!!
        when (type) {
            "album" -> { /* TODO */
                val title = piece.getTitle()
                val albumId = piece.getBrowseId()
                val authorId = piece.getChannelIdOrNull()
                val thumbnails = piece.getThumbnails()
            }

            "artist" -> {
                val name = piece.getTitle()
                val authorId = piece.getBrowseId()
                val thumbnails = piece.getThumbnails()

                result.add(SearchResult.SearchResultArtist(authorId, name, thumbnails))
            }

            "community playlist" -> { /* TODO */
            }

            in listOf("playlist", "featured playlist") -> { /* TODO */
            }

            "song" -> {
                val title = piece.getTitle()
                val duration = piece.findDuration()
                val watchId = piece.getBrowseId()
                val thumbnails: List<Thumbnail> = piece.getThumbnails()

                result.add(SearchResult.SearchResultSong(title, watchId, Duration(duration), thumbnails))
            }

            "video" -> {
                val title = piece.getTitle()
                val duration = Duration(piece.findDuration())
                val watchId = piece.getBrowseId()
                val thumbnails: List<Thumbnail> = piece.getThumbnails()
                val channelId = piece.getChannelId()

                result.add(SearchResult.SearchResultVideo(title, watchId, channelId, duration, thumbnails))
            }

            else -> throw IllegalArgumentException("Invalid response from YouTube Music API")
        }
    }
    return result
}

private fun JsonElement.getTitle(): String = jsonObject["flexColumns"]
    ?.jsonArray?.get(0)
    ?.jsonObject?.get("musicResponsiveListItemFlexColumnRenderer")
    ?.jsonObject?.get("text")
    ?.jsonObject?.get("runs")
    ?.jsonArray?.get(0)
    ?.jsonObject?.get("text")
    ?.jsonPrimitive?.content
    ?: throw IllegalArgumentException("Title or name are not found")

private fun JsonElement.findDuration(): String = jsonObject["flexColumns"]
    ?.jsonArray?.get(1)
    ?.jsonObject?.get("musicResponsiveListItemFlexColumnRenderer")
    ?.jsonObject?.get("text")
    ?.jsonObject?.get("runs")
    ?.jsonArray?.find {
        Duration.isDuration(it.jsonObject["text"]?.jsonPrimitive?.content ?: "")
    }?.jsonObject?.get("text")
    ?.jsonPrimitive?.content
    ?: throw IllegalArgumentException("Duration not found")

private fun JsonElement.getBrowseId(): String = getBrowseIdOrNull()
    ?: throw IllegalArgumentException("ID not found")

private fun JsonElement.getBrowseIdOrNull(): String? = jsonObject["playlistItemData"]
    ?.jsonObject?.get("videoId")
    ?.jsonPrimitive?.content
    ?: jsonObject["navigationEndpoint"]
        ?.jsonObject?.get("browseEndpoint")
        ?.jsonObject?.get("browseId")
        ?.jsonPrimitive?.content

private fun JsonElement.getThumbnails(): List<Thumbnail> = jsonObject["thumbnail"]
    ?.jsonObject?.get("musicThumbnailRenderer")
    ?.jsonObject?.get("thumbnail")
    ?.jsonObject?.get("thumbnails")
    ?.jsonArray?.map { Json.decodeFromJsonElement(it) }
    ?: throw IllegalArgumentException("Thumbnails were not found")

private fun JsonElement.getChannelId(): String = getChannelIdOrNull()
    ?: throw IllegalArgumentException("Channel ID was not found")

private fun JsonElement.getChannelIdOrNull(): String? = jsonObject["flexColumns"]
    ?.jsonArray?.get(1)
    ?.jsonObject?.get("musicResponsiveListItemFlexColumnRenderer")
    ?.jsonObject?.get("text")
    ?.jsonObject?.get("runs")
    ?.jsonArray?.find {
        it.jsonObject["navigationEndpoint"]
            ?.jsonObject?.get("browseEndpoint")
            ?.jsonObject?.get("browseEndpointContextSupportedConfigs")
            ?.jsonObject?.get("browseEndpointContextMusicConfig")
            ?.jsonObject?.get("pageType")
            ?.jsonPrimitive?.content in listOf("MUSIC_PAGE_TYPE_USER_CHANNEL", "MUSIC_PAGE_TYPE_ARTIST")
    }?.getBrowseIdOrNull()
