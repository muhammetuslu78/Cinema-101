package com.muhammetuslu.cinema101.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Film(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val filmName:String?,
    @ColumnInfo(name = "director")
    @SerializedName("director")
    val filmDirector:String?,
    @ColumnInfo(name = "genre")
    @SerializedName("genre")
    val filmGenre:String?,
    @ColumnInfo(name = "country")
    @SerializedName("country")
    val filmCountry:String?,
    @ColumnInfo(name = "year")
    @SerializedName("year")
    val filmYear:String?,
    @ColumnInfo(name = "duraion")
    @SerializedName("duraion")
    val filmDuration:String?,
    @ColumnInfo(name = "writer")
    @SerializedName("writer")
    val filmWriter:String?,
    @ColumnInfo(name = "stars")
    @SerializedName("stars")
    val filmStars:String?,
    @ColumnInfo(name = "rating")
    @SerializedName("rating")
    val filmRating:String?,
    @ColumnInfo(name = "oscar")
    @SerializedName("oscar")
    val filmOscar:String?,
    @ColumnInfo(name = "posterbg")
    @SerializedName("posterbg")
    val posterbg:String?,
    @ColumnInfo(name = "review")
    @SerializedName("review")
    val filmReview:String?,
    @ColumnInfo(name = "poster")
    @SerializedName("poster")
    val poster:String?
)
{
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0
}