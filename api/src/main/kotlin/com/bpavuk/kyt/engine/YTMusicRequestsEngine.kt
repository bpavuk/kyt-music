package com.bpavuk.kyt.engine

import com.bpavuk.kyt.engine.requests.YTMusicRequest

public interface YTMusicRequestsEngine {
    public suspend fun <T> execute(request: YTMusicRequest<T>): T
}
