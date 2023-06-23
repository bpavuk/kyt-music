package com.bpavuk.kyt.types.music

import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.song.Duration
import kotlinx.serialization.Serializable


@Serializable
public data class Video(
    val title: String,
    val watchId: String,
    val duration: Duration,
    val thumbnails: List<Thumbnail>,
    val channelId: String
): MusicItem {
    public val channelUrl: String = "https://youtube.com/channel/${channelId}"
    public val watchUrl: String = "https://youtu.be/${watchId}"
}
