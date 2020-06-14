package com.muhammetuslu.cinema101.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muhammetuslu.cinema101.model.Film

@Database(entities = arrayOf(Film::class),version = 1)
abstract class FilmDatabase : RoomDatabase(){

    abstract fun filmDao():FilmDao

    companion object
    {
        @Volatile private var instance :FilmDatabase?=null

        private val lock = Any()

        operator fun invoke(context: Context)= instance?: synchronized(lock)
        {
            instance?: makeDatabase(context).also {
                instance=it
            }
        }

        private fun makeDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,FilmDatabase::class.java,"filmdatabase"
        ).build()
    }
}