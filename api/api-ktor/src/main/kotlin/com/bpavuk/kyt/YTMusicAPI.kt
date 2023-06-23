package com.bpavuk.kyt

import com.bpavuk.kyt.apiktor.KtorYTMusicEngine

public suspend fun YTMusicAPI(logging: Boolean): YTMusicAPI = YTMusicAPI(
    engine = KtorYTMusicEngine(
        baseUrl = "https://music.youtube.com",
        logging = logging
    ).apply {
        println("Setting up YT Music client...")
        setup()
        println("Setup successful")
    }
)
