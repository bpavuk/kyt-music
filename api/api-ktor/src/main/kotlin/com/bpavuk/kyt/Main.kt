package com.bpavuk.kyt

import com.bpavuk.kyt.music.ArtistRepository
import com.bpavuk.kyt.music.SongRepository
import com.bpavuk.kyt.music.VideoRepository

public suspend fun main() {
    val api = YTMusicAPI(logging = false)
    val songs = api.search.search(
        query = "classic"
    )
    songs.forEach {
        when (it) {
            is ArtistRepository -> println("""
                Artist
                 Meet ${it.name}
                 Listen more at ${it.url}
                
            """.trimIndent())

            is SongRepository -> println("""
                Song "${it.title}"
                 Listen at ${it.url}
                
            """.trimIndent())

            is VideoRepository -> println("""
                Video "${it.title}"
                 Listen/watch at ${it.watchUrl}
                 More videos from this author here: ${it.channelUrl}
                
            """.trimIndent())
        }
    }
}
