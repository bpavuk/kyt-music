package com.bpavuk.kyt.music

import com.bpavuk.kyt.YTMusicAPI
import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.Video
import com.bpavuk.kyt.types.music.song.Duration
import io.ktor.http.*

public class VideoRepository(
    override val data: Video,
    private val api: YTMusicAPI
) : MusicRepository {
    public val title: String get() = data.title
    public val watchId: String get() = data.watchId
    public val duration: Duration get() = data.duration
    public val thumbnails: List<Thumbnail> get() = data.thumbnails
    public val channelId: String get() = data.channelId
    public val channelUrl: Url get() = Url(data.channelUrl)
    public val watchUrl: Url get() = Url(data.watchUrl)
}
