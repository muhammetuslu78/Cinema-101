package com.muhammetuslu.cinema101.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.muhammetuslu.cinema101.R
import com.muhammetuslu.cinema101.adapter.FilmAdapter
import com.muhammetuslu.cinema101.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {
    private lateinit var viewModel: FeedViewModel
    private val filmAdapter = FilmAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        var linearViewHorizantal = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        filmList.layoutManager = linearViewHorizantal
        filmList.adapter = filmAdapter


        swipeRefreshLayout.setOnRefreshListener {
            filmList.visibility=View.GONE
            countryError.visibility=View.GONE
            countryLoading.visibility=View.VISIBLE
            viewModel.refreshFromApi()
            swipeRefreshLayout.isRefreshing=false
        }
        observeLiveData()
    }

    private fun observeLiveData()
    {
        viewModel.films.observe(viewLifecycleOwner, Observer {films ->
            films?.let {
                filmList.visibility=View.VISIBLE
                filmAdapter.updateFilmList(films)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                if(it) {
                    countryError.visibility = View.VISIBLE
                } else {
                    countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                if (it) {
                    countryLoading.visibility = View.VISIBLE
                    filmList.visibility = View.GONE
                    countryError.visibility = View.GONE
                } else {
                    countryLoading.visibility = View.GONE
                }
            }
        })
    }
}
