package com.muhammetuslu.cinema101.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

import com.muhammetuslu.cinema101.R
import com.muhammetuslu.cinema101.util.downloadFromUrl
import com.muhammetuslu.cinema101.util.placeholderProgressBar
import com.muhammetuslu.cinema101.viewmodel.FilmViewModel
import kotlinx.android.synthetic.main.fragment_film.*

class FilmFragment : Fragment() {
    private lateinit var viewModel: FilmViewModel
    private var filmUuid=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_film, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            filmUuid= FilmFragmentArgs.fromBundle(it).filmUuid}

        viewModel= ViewModelProviders.of(this).get(FilmViewModel::class.java)
        viewModel.getDataFromRoom(filmUuid)

        observeLiveData()
    }

    private fun observeLiveData()
    {
        viewModel.filmLiveData.observe(viewLifecycleOwner, Observer {film ->
            film?.let {

                collapsing_toolbar.title=film.filmName

                filmDirector.text=film.filmDirector
                filmGenre.text=film.filmGenre
                filmCountry.text=film.filmCountry
                filmYear.text=film.filmYear
                filmDuration.text=film.filmDuration
                filmWriter.text=film.filmWriter
                filmStar.text=film.filmStars
                filmRating.text=film.filmRating
                if(film.filmOscar=="yes")
                {
                    filmOscar.visibility=View.VISIBLE
                }
                else
                {
                    filmOscar.visibility=View.GONE
                }
                filmReview.text=film.filmReview
                context?.let {
                    header.downloadFromUrl(film.posterbg, placeholderProgressBar(it))
                }
                myRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    Toast.makeText(context,"Rating : "+ratingBar.rating, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
