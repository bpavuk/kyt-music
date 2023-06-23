package com.bpavuk.kyt.apiktor.serialization.search

import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.song.Duration
import kotlinx.serialization.Serializable

@Serializable
public sealed interface SearchResult {
    @Serializable
    public data class SearchResultSong(
        val title: String,
//        val year: Int,
        val watchId: String,
        val duration: Duration,
        val thumbnails: List<Thumbnail>,
//        val artistId: String
    ): SearchResult

    @Serializable
    public data class SearchResultArtist(
        public val id: String,
        public val name: String,
        public val thumbnails: List<Thumbnail>
    ): SearchResult

    @Serializable
    public data class SearchResultVideo(
        val title: String,
        val watchId: String,
        val channelId: String,
        val duration: Duration,
        val thumbnails: List<Thumbnail>,
    ): SearchResult
}
