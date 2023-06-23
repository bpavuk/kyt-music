package com.bpavuk.kyt

import com.bpavuk.kyt.engine.YTMusicRequestsEngine
import com.bpavuk.kyt.search.SearchApi

public class YTMusicAPI(public val engine: YTMusicRequestsEngine) {
    public val search: SearchApi = SearchApi(api = this)
}
