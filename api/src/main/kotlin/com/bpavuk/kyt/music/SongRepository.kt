package com.bpavuk.kyt.music

import com.bpavuk.kyt.YTMusicAPI
import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.Song
import com.bpavuk.kyt.types.music.song.Duration
import io.ktor.http.*

public class SongRepository(
    public override val data: Song,
    private val api: YTMusicAPI
): MusicRepository {
    public val title: String get() = data.title
    public val duration: Duration get() = data.duration
    public val id: String get() = data.watchId
    public val url: Url get() = Url(data.watchUrl)
    public val thumbnails: List<Thumbnail> get() = data.thumbnails
}
