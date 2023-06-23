package com.bpavuk.kyt.music

import com.bpavuk.kyt.types.music.MusicItem

public sealed interface MusicRepository {
    public val data: MusicItem
}
