package com.muhammetuslu.cinema101.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.muhammetuslu.cinema101.model.Film
import com.muhammetuslu.cinema101.service.FilmAPIService
import com.muhammetuslu.cinema101.service.FilmDatabase
import com.muhammetuslu.cinema101.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private val filmApiService= FilmAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime =10*60*1000*1000*1000L

    val films = MutableLiveData<List<Film>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()


    fun refreshData() {

        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime()-updateTime<refreshTime)
        {
            getDataFromSQlite()
        }
        else
        {
            getDataFromAPI()
        }

    }
    fun refreshFromApi()
    {
        getDataFromAPI()
    }

    private fun getDataFromSQlite() {
        countryLoading.value=true
        launch {
            val films = FilmDatabase(getApplication()).filmDao().getAllFilms()
            showFilms(films)
            //Toast.makeText(getApplication(),"Films From SQlite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromAPI() {
        countryLoading.value=true

        disposable.add(
            filmApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Film>>(){
                    override fun onSuccess(t: List<Film>) {
                        storeInSQlite(t)
                        //Toast.makeText(getApplication(),"Films From API", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value=false
                        countryError.value=true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun showFilms(filmList: List<Film>)
    {
        films.value=filmList
        countryError.value=false
        countryLoading.value=false
    }

    private fun storeInSQlite(list:List<Film>)
    {
        launch {
            val dao = FilmDatabase(getApplication()).filmDao()
            dao.deleteAllFilms()
            val listLong=dao.insertAll(*list.toTypedArray())
            var i=0
            while (i<list.size)
            {
                list[i].uuid=listLong[i].toInt()
                i++
            }
            showFilms(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}