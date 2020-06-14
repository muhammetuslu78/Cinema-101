package com.muhammetuslu.cinema101.service

import com.muhammetuslu.cinema101.model.Film
import io.reactivex.Single
import retrofit2.http.GET

interface FilmAPI {

    //https://raw.githubusercontent.com/uslu78/muhammetuslu-filmdataset/master/film.json

    @GET("uslu78/muhammetuslu-filmdataset/master/film.json")
    fun getFilms():Single<List<Film>>

}