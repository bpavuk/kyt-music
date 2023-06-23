package com.bpavuk.kyt.engine.requests

import com.bpavuk.kyt.types.annotations.NotFinishedAPI
import com.bpavuk.kyt.types.music.MusicItem
import com.bpavuk.kyt.types.search.filters.Filter
import com.bpavuk.kyt.types.search.scopes.Scope
import com.bpavuk.kyt.types.search.scopes.UploadsScope

@OptIn(NotFinishedAPI::class)
public data class SearchRequest(
    val query: String,
    val filter: Filter?,
    val scope: Scope?,
    val limit: Int = 20,
    val ignoreTyposFix: Boolean = false,
): YTMusicRequest<SearchRequest.Response> {
    init {
        if (filter != null && scope == UploadsScope)
            throw IllegalArgumentException("If you use UploadsScope you are unable to use filter")
    }

    public data class Response(val response: List<MusicItem>)
}
