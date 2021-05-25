package com.caunb163.data.datalocal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfanityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllProfanityWord(list: List<ProfanityWord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(word: ProfanityWord)

    @Query("SELECT * FROM profanity")
    suspend fun getAllWord(): List<ProfanityWord>
}