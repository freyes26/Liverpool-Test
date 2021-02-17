package com.liverpool.test.liverpooltest.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.repository.database.dao.SearchDao
import com.liverpool.test.liverpooltest.repository.database.model.Search

@Database(entities = [Search::class], version = 1, exportSchema = false)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun serachDao() : SearchDao

    companion object {
        @Volatile
        private var INSTANCE: SearchDatabase? = null

        fun getDatabase(context: Context): SearchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchDatabase::class.java,
                    Constants.databseConstants.DATABASE_NAME
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}