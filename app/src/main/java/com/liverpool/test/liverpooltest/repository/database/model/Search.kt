package com.liverpool.test.liverpooltest.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.liverpool.test.liverpool.Constants

@Entity(tableName = Constants.databseConstants.SEARCH_TABLE)
class Search(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "search") val search: String
) {
}