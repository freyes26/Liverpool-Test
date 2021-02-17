package com.liverpool.test.liverpooltest.repository.database.dao

import androidx.room.*
import com.liverpool.test.liverpooltest.repository.database.model.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("SELECT * FROM Search")
    fun getSearch(): Flow<List<Search>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(search: Search)

    @Query("DELETE FROM search")
    fun deleteAll()

    @Query("DELETE FROM Search WHERE id = :id")
    fun delete(id: Int)

    @Update
    fun upsate(note: Search)

}