package com.caunb163.data.datalocal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profanity")
class ProfanityWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String
)