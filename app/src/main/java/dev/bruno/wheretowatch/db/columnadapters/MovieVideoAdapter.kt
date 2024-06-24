package dev.bruno.wheretowatch.db.columnadapters

import app.cash.sqldelight.ColumnAdapter
import dev.bruno.wheretowatch.db.MovieVideoEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MovieVideoAdapter : ColumnAdapter<List<MovieVideoEntity>, String> {

    override fun decode(databaseValue: String): List<MovieVideoEntity> {
        if (databaseValue.isEmpty()) return emptyList()
        return Json.decodeFromString<List<MovieVideoEntity>>(databaseValue)
    }

    override fun encode(value: List<MovieVideoEntity>): String {
        return Json.encodeToString(value)
    }
}
