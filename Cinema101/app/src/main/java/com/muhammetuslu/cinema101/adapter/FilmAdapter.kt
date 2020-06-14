package com.muhammetuslu.cinema101.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.muhammetuslu.cinema101.R
import com.muhammetuslu.cinema101.databinding.ItemFilmBinding
import com.muhammetuslu.cinema101.model.Film
import com.muhammetuslu.cinema101.util.downloadFromUrl
import com.muhammetuslu.cinema101.util.placeholderProgressBar


import com.muhammetuslu.cinema101.view.FeedFragmentDirections

import kotlinx.android.synthetic.main.item_film.view.*

class FilmAdapter(var filmList: ArrayList<Film>): RecyclerView.Adapter<FilmAdapter.FilmViewHolder>(),FilmClickListener {
    class FilmViewHolder(var view: ItemFilmBinding): RecyclerView.ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemFilmBinding>(inflater,R.layout.item_film,parent,false)

        return FilmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.view.film=filmList[position]
        holder.view.listener=this
    }

    fun updateFilmList(newCountryList: List<Film>) {
        filmList.clear()
        filmList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onFilmClicked(v: View) {
        val uuid = v.filmUuidText.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToFilmFragment()
        action.filmUuid=uuid
        Navigation.findNavController(v).navigate(action)
    }


}