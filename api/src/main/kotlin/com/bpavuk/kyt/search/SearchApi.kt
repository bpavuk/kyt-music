package com.bpavuk.kyt.search

import com.bpavuk.kyt.YTMusicAPI
import com.bpavuk.kyt.engine.requests.SearchRequest
import com.bpavuk.kyt.music.ArtistRepository
import com.bpavuk.kyt.music.MusicRepository
import com.bpavuk.kyt.music.SongRepository
import com.bpavuk.kyt.music.VideoRepository
import com.bpavuk.kyt.types.music.Artist
import com.bpavuk.kyt.types.music.Song
import com.bpavuk.kyt.types.music.Video
import com.bpavuk.kyt.types.search.filters.Filter
import com.bpavuk.kyt.types.search.scopes.Scope

public class SearchApi(private val api: YTMusicAPI) {
    public suspend fun search(
        query: String,
        filter: Filter? = null,
        scope: Scope? = null,
        limit: Int = 20,
        ignoreTyposFix: Boolean = false
    ): List<MusicRepository> {
        val result = api.engine.execute(
            request = SearchRequest(query, filter, scope, limit, ignoreTyposFix)
        )
        return result.response.map {
            when (it) {
                is Artist -> ArtistRepository(it, api)
                is Song -> SongRepository(it, api)
                is Video -> VideoRepository(it, api)
            }
        }
    }
}
