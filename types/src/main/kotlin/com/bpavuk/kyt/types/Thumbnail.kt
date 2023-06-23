package com.bpavuk.kyt.types

import kotlinx.serialization.Serializable

@Serializable
public data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)
