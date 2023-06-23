package com.bpavuk.kyt.apiktor.serialization.search

import com.bpavuk.kyt.types.user.Context
import kotlinx.serialization.Serializable

@Serializable
public data class SearchRequestSerializable(
    val context: Context = Context(),
    val query: String,
    val params: String?
)
