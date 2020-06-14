package com.muhammetuslu.cinema101.service

import androidx.room.*
import com.muhammetuslu.cinema101.model.Film

@Dao
interface FilmDao {
    @Insert
    suspend fun insertAll(vararg films:Film):List<Long>

    @Query("SELECT * FROM film")
    suspend fun getAllFilms():List<Film>

    @Query("SELECT * FROM film WHERE uuid = :filmId")
    suspend fun getFilm(filmId:Int):Film

    @Query("DELETE FROM film")
    suspend fun deleteAllFilms()
}