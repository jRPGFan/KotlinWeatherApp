package com.example.kotlinweatherapp.app

import android.app.Application
import androidx.room.Room
import com.example.kotlinweatherapp.room.HistoryDao
import com.example.kotlinweatherapp.room.HistoryDatabase
import com.example.kotlinweatherapp.room.NoteDao
import java.lang.IllegalStateException

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDatabase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDatabase::class.java) {
                    if (db == null) {
                        if (appInstance == null)
                            throw IllegalStateException("Application is null while creating database")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDatabase::class.java,
                            DB_NAME)
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }

            return db!!.historyDao()
        }

        fun getNoteDao(): NoteDao {
            if (db == null) {
                synchronized(HistoryDatabase::class.java) {
                    if (db == null) {
                        if (appInstance == null)
                            throw IllegalStateException("Application is null while creating database")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDatabase::class.java,
                            DB_NAME)
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }

            return db!!.noteDao()
        }
    }
}