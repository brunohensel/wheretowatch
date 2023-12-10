package dev.bruno.wheretowatch.db.columnadapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

internal object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return databaseValue.toLocalDate()
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }
}
