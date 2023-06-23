package com.bpavuk.kyt.types.user

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class Context(
    val client: Client = Client.default(),
    val user: JsonObject = JsonObject(emptyMap())
)

@Serializable
public data class Client(
    val clientName: String,
    val clientVersion: String,
    val hl: String
)

public fun Client.Companion.default(): Client = Client(
    clientName = "WEB_REMIX",
    clientVersion = buildString {
        append("1.")
        append(Clock.System.now()
            .toLocalDateTime(TimeZone.UTC).date
            .toString().replace("-", "")
        )
        append(".01.00")
    },
    hl = "en"
)
