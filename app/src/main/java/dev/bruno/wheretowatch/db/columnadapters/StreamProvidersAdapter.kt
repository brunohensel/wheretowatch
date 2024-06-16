package dev.bruno.wheretowatch.db.columnadapters

import app.cash.sqldelight.ColumnAdapter

internal object StreamProvidersAdapter: ColumnAdapter<List<Int>, String> {

    override fun decode(databaseValue: String): List<Int> {
        if (databaseValue.isEmpty()) return emptyList()
        return databaseValue.split(",").map { it.trim().toInt() }
    }

    override fun encode(value: List<Int>): String {
        return value.joinToString()
    }
}
