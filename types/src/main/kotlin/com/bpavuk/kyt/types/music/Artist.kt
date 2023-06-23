package com.bpavuk.kyt.types.music

import com.bpavuk.kyt.types.Thumbnail
import kotlinx.serialization.Serializable

@Serializable
public data class Artist(
    public val id: String,
    public val name: String,
    public val thumbnails: List<Thumbnail>
): MusicItem
