package com.bpavuk.kyt.music

import com.bpavuk.kyt.YTMusicAPI
import com.bpavuk.kyt.types.Thumbnail
import com.bpavuk.kyt.types.music.Artist
import io.ktor.http.*

public class ArtistRepository(
    override val data: Artist,
    private val api: YTMusicAPI
) : MusicRepository {
    public val id: String get() = data.id
    public val name: String get() = data.name
    public val thumbnails: List<Thumbnail> get() = data.thumbnails
    public val url: Url get() = Url("https://music.youtube.com/channel/$id")
}
