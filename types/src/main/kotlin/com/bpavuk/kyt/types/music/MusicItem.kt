package com.bpavuk.kyt.types.music

import kotlinx.serialization.Serializable

/**
 * Everything that inherits this interface represents item of music (playlist, song, album, author, etc)
 */
@Serializable
public sealed interface MusicItem
