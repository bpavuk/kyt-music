package com.bpavuk.kyt.engine.requests

public sealed interface YTMusicRequest<out T>

public typealias SimpleYTMusicRequest = YTMusicRequest<Unit>
