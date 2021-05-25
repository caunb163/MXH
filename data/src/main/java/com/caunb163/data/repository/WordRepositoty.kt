package com.caunb163.data.repository

import android.content.Context
import com.caunb163.data.datalocal.ProfanityDao
import com.caunb163.data.datalocal.ProfanityDatabase
import com.caunb163.data.datalocal.ProfanityWord

class WordRepositoty(context: Context) {
    private val profanityDao: ProfanityDao

    init {
        val db = ProfanityDatabase.getDatabase(context)
        profanityDao = db.profanityDao()
    }

    fun addAllProfanityWord(list: List<ProfanityWord>) {
        profanityDao.addAllProfanityWord(list)
    }

    fun addWord(word: ProfanityWord) {
        ProfanityDatabase.Companion.databaseWriteExecutor.execute(Runnable {
            profanityDao.addWord(word)
        })
    }

    suspend fun getProfanityWord(): List<ProfanityWord> {
        return profanityDao.getAllWord()
    }

}