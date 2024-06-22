package dev.bruno.wheretowatch.db.columnadapters

import app.cash.sqldelight.ColumnAdapter
import dev.bruno.wheretowatch.services.model.MovieVideo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MovieVideoAdapter : ColumnAdapter<List<MovieVideo>, String> {

    override fun decode(databaseValue: String): List<MovieVideo> {
        if (databaseValue.isEmpty()) return emptyList()
        return Json.decodeFromString<List<MovieVideo>>(databaseValue)
    }

    override fun encode(value: List<MovieVideo>): String {
        return Json.encodeToString(value)
    }
}
