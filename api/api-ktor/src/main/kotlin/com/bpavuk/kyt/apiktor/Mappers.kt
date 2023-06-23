package com.bpavuk.kyt.apiktor

import com.bpavuk.kyt.apiktor.serialization.search.SearchRequestSerializable
import com.bpavuk.kyt.apiktor.serialization.search.SearchResult
import com.bpavuk.kyt.engine.requests.SearchRequest
import com.bpavuk.kyt.types.annotations.NotFinishedAPI
import com.bpavuk.kyt.types.music.Artist
import com.bpavuk.kyt.types.music.MusicItem
import com.bpavuk.kyt.types.music.Song
import com.bpavuk.kyt.types.music.Video
import com.bpavuk.kyt.types.search.filters.*
import com.bpavuk.kyt.types.search.scopes.LibraryScope
import com.bpavuk.kyt.types.search.scopes.UploadsScope
import com.bpavuk.kyt.types.user.Context

public fun SearchRequest.serializable(): SearchRequestSerializable = SearchRequestSerializable(
    Context(), query, getParams()
)

@OptIn(NotFinishedAPI::class)
private fun SearchRequest.getParams(): String? {
    val filteredParam1 = "EgWKAQI"
    var params: String? = null
    var param1: String? = null
    var param2: String? = null
    var param3: String? = null
    val params2Map = mapOf(
        SongsFilter to "I", VideosFilter to "Q", AlbumsFilter to "Y",
        ArtistsFilter to "g", PlaylistsFilter to "o")

    if (filter == null && scope == null && !ignoreTyposFix) {
        return null
    }

    if (scope == UploadsScope) {
        params = "agIYAw%3D%3D"
    }

    if (scope == LibraryScope) {
        if (filter != null) {
            param1 = filteredParam1
            param2 = params2Map[filter]
            param3 = "AWoKEAUQCRADEAoYBA%3D%3D"
        } else {
            params = "agIYBA%3D%3D"
        }
    }

    if (scope == null && filter != null) {
        if (filter == PlaylistsFilter) {
            params = "Eg-KAQwIABAAGAAgACgB"
            params += if (!ignoreTyposFix) {
                "MABqChAEEAMQCRAFEAo%3D"
            } else {
                "MABCAggBagoQBBADEAkQBRAK"
            }
        } else if (PlaylistsFilter == filter) {
            param1 = "EgeKAQQoA"
            param2 = if (filter == FeaturedPlaylistsFilter) {
                "Dg"
            } else { // community_playlists
                "EA"
            }

            param3 = if (!ignoreTyposFix) {
                "BagwQDhAKEAMQBBAJEAU%3D"
            } else {
                "BQgIIAWoMEA4QChADEAQQCRAF"
            }
        } else {
            param1 = filteredParam1
            param2 = params2Map[filter]
            param3 = if (!ignoreTyposFix) {
                "AWoMEA4QChADEAQQCRAF"
            } else {
                "AUICCAFqDBAOEAoQAxAEEAkQBQ%3D%3D"
            }
        }
    }

    if (scope == null && filter == null /* && ignoreTyposFix */) {
        params = "EhGKAQ4IARABGAEgASgAOAFAAUICCAE%3D"
    }

    return params ?: (param1 + param2 + param3)

}

internal fun SearchResult.toMusicItem(): MusicItem = when (this) {
    is SearchResult.SearchResultSong -> Song(title, watchId, duration, thumbnails)
    is SearchResult.SearchResultArtist -> Artist(id, name, thumbnails)
    is SearchResult.SearchResultVideo -> Video(title, watchId, duration, thumbnails, channelId)
}
