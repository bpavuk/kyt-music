package com.bpavuk.kyt.types.music

import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.song.Duration
import kotlinx.serialization.Serializable

@Serializable
public data class Song(
    val title: String,
//    val year: Int,
    val watchId: String,
    val duration: Duration,
    val thumbnails: List<Thumbnail>,
//    val artistId: String
): MusicItem {
    public val watchUrl: String = "https://music.youtube.com/watch?v=${watchId}"
}
