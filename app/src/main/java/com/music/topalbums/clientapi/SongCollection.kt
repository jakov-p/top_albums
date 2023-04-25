package com.music.topalbums.clientapi.collection

import com.music.topalbums.clientapi.retrofit.model.AlbumSong
import com.music.topalbums.clientapi.retrofit.model.AlbumSongsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistSong
import com.music.topalbums.clientapi.retrofit.model.ArtistSongsCollection

/**
 * This class is introduced as a common Song class from two different sources.
 * AlbumSong objects and ArtistSong objects do not have the same fields (the original JSON is a bit different for them,
 * but they overlap in 95% of the fields)
 * This class contains almost all the fields from the original classes.

 */
data class Song(

    val artistId: Int?,
    val collectionId: Int?,
    val amgArtistId: Int?,
    val artistName: String?,

    val country: String?,

    val collectionName: String?,
    val artistViewUrl: String?,

    val collectionPrice: Float?,
    val currency: String?,
    val collectionExplicitness: String?,

    val releaseDate: String?,  //Date
    val primaryGenreName: String?,

    val trackCount: Int?,

    val trackId: Int?,
    val trackName: String?,
    val trackCensoredName: String?,
    val trackViewUrl: String?,
    val previewUrl: String?,
    val trackPrice: Float?,
    val trackExplicitness: String?,
    val trackNumber: Int?,
    val trackTimeMillis: Int?,
    val isStreamable: Boolean)
{
    /**
     * Used when the source is AlbumSong
     */
    constructor(albumSong: AlbumSong) : this(
        artistId = albumSong.artistId,
        collectionId = albumSong.collectionId,
        amgArtistId = albumSong.amgArtistId,
        artistName = albumSong.artistName,

        country = albumSong.country,

        collectionName = albumSong.collectionName,
        artistViewUrl = albumSong.artistViewUrl,

        collectionPrice= albumSong.collectionPrice,
        currency = albumSong.currency,
        collectionExplicitness = albumSong.collectionExplicitness,

        releaseDate = albumSong.releaseDate,  //Date
        primaryGenreName = albumSong.primaryGenreName,

        trackCount = albumSong.trackCount,

        trackId = albumSong.trackId,
        trackName= albumSong.trackName,
        trackCensoredName= albumSong.trackCensoredName,
        trackViewUrl= albumSong.trackViewUrl,
        previewUrl= albumSong.previewUrl,
        trackPrice= albumSong.trackPrice,
        trackExplicitness = albumSong.trackExplicitness,
        trackNumber = albumSong.trackNumber,
        trackTimeMillis = albumSong.trackTimeMillis,
        isStreamable= albumSong.isStreamable
    )

    /**
     * Used when the source is ArtistSong
     */
    constructor(artistSong: ArtistSong) : this(
        artistId = artistSong.artistId,
        collectionId = artistSong.collectionId,
        amgArtistId = artistSong.amgArtistId,
        artistName = artistSong.artistName,

        country = artistSong.country,

        collectionName = artistSong.collectionName,
        artistViewUrl = artistSong.artistViewUrl,

        collectionPrice= artistSong.collectionPrice,
        currency = artistSong.currency,
        collectionExplicitness = artistSong.collectionExplicitness,

        releaseDate = artistSong.releaseDate,  //Date
        primaryGenreName = artistSong.primaryGenreName,

        trackCount = artistSong.trackCount,

        trackId = artistSong.trackId,
        trackName= artistSong.trackName,
        trackCensoredName= artistSong.trackCensoredName,
        trackViewUrl= artistSong.trackViewUrl,
        previewUrl= artistSong.previewUrl,
        trackPrice= artistSong.trackPrice,
        trackExplicitness = artistSong.trackExplicitness,
        trackNumber = artistSong.trackNumber,
        trackTimeMillis = artistSong.trackTimeMillis,
        isStreamable= artistSong.isStreamable
    )

    override fun toString(): String
    {
        return "($trackNumber)'$trackName' by '$artistName', $releaseDate ( $trackPrice $currency )"
    }
}


/**
 * Justa a list of songs
 * @param list
 */
class SongCollection private constructor (val list: List<Song>)
{
    /**
     * Used when the source is ArtistSongsCollection list
     * The source is a model object generated from the original JSON.
     */
    constructor(artistSongsCollection: ArtistSongsCollection): this(mutableListOf<Song>())
    {
        artistSongsCollection.results?.forEach {
            (list as MutableList).add(Song(it))
        }
    }

    /**
     * Used when the source is AlbumSongsCollection list
     * The source is a model object generated from the original JSON.
     */
    constructor(albumSongsCollection: AlbumSongsCollection): this(mutableListOf<Song>())
    {
        albumSongsCollection.results?.
        filter { it.wrapperType == "track" }?.
        forEach {
                (list as MutableList).add(Song(it))
            }
    }
}