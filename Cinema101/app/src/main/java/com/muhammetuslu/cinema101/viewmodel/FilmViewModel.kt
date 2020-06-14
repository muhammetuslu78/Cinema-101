package com.muhammetuslu.cinema101.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.muhammetuslu.cinema101.model.Film
import com.muhammetuslu.cinema101.service.FilmDatabase
import kotlinx.coroutines.launch

class FilmViewModel(application: Application):BaseViewModel(application) {
    val filmLiveData= MutableLiveData<Film>()

    fun getDataFromRoom(uuid:Int)
    {
        launch {
            val dao = FilmDatabase(getApplication()).filmDao()
            val film = dao.getFilm(uuid)
            filmLiveData.value=film
        }
    }
}