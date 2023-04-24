package com.music.topalbums.clientapi.collection

import android.os.Parcelable
import com.music.topalbums.clientapi.albums.TopAlbum
import com.music.topalbums.clientapi.albums.TopAlbumsCollection
import com.music.topalbums.clientapi.retrofit.model.ArtistAlbum
import com.music.topalbums.clientapi.retrofit.model.ArtistAlbumsCollection
import kotlinx.android.parcel.Parcelize

/**
 * This class is introduced as a common Album class from two different sources.
 * TopAlbum objects  and ArtistAlbum objects do not have the same fields (the original JSON is different for them).
 * This class contains only most relevant fields from the original classes.
 *
 * All these fields are optional, but in practice it seems that they are always non-null.
 */
@Parcelize
data class Album(
    // originalPos is the position in the list in the original json.
    //(It is useful when filtering, when many albums are missing in a filtered list)
    val originalPos: Int?,

    val artistName: String?, //the singer(group)'s name
    val artistViewUrl: String?, //link to the artist's web page
    val collectionImageUrl: String? = null, //album's image
    val collectionViewUrl: String?, //link to the album's web page
    val primaryGenreName: String? = null, //pop, rock, country,...
    val primaryGenreId:Int? = null, // a number assigned to the genre above
    val collectionId:Int? = null,  //unique id in the Server's dbase (used for fetching songs)
    val collectionName: String? = null, //the album name
    val collectionPrice: Float? = null, //the album's price
    val currency: String? = null, //$, Euro, Lira,..
    val trackCount:Int? = null, //how many songs on the labum
    val releaseDate: String? = null) //when the album was released
    : Parcelable
{
    /**
     * Used when the source is ArtistAlbum
     * @param originalPos the position in the list in the original json
     */
    constructor(originalPos:Int?, artistAlbum: ArtistAlbum) : this(

        originalPos = originalPos,
        artistName = artistAlbum.artistName,
        artistViewUrl = artistAlbum.artistLinkUrl,

        collectionImageUrl = artistAlbum.artworkUrl100,
        collectionViewUrl = artistAlbum.collectionViewUrl,

        primaryGenreName = artistAlbum.primaryGenreName,
        primaryGenreId = artistAlbum.primaryGenreId,

        collectionId = artistAlbum.collectionId,
        collectionName = artistAlbum.collectionName,
        collectionPrice = artistAlbum.collectionPrice,
        currency = artistAlbum.currency,

        trackCount = artistAlbum.trackCount,
        releaseDate = artistAlbum.releaseDate  //Date
    )

    /**
     * Used when the source is TopAlbum
     * @param originalPos the position in the list in the original json
     */
    constructor(originalPos:Int?, topAlbum: TopAlbum) : this(
        originalPos = originalPos,
        artistName = topAlbum.im_artist?.label,
        artistViewUrl = topAlbum.im_artist?.attributes?.href,
        collectionImageUrl = topAlbum.im_image?.get(2)?.label,
        collectionViewUrl = topAlbum.id?.label,

        primaryGenreName = topAlbum.category?.attributes?.term,
        primaryGenreId = topAlbum.category?.attributes?.im_id?.toInt(),

        collectionId = topAlbum.id?.attributes?.im_id?.toInt(),
        collectionName = topAlbum.title?.label,
        collectionPrice = topAlbum.im_price?.attributes?.amount?.toFloat(),
        currency = topAlbum.im_price?.attributes?.currency,

        trackCount = topAlbum.im_itemCount?.label?.toInt(),
        releaseDate = topAlbum.im_releaseDate?.label,  //Date
    )

    override fun toString(): String
    {
        //e.g. "2011-04-12T07:00:00Z" --> "2011-04-12"
        val releaseDateShortened = releaseDate?.split("T")?.get(0)

        //.g. '[23] Ghost Stories - Coldplay(Alternative)' by 'Coldplay', 2014-05-19 ( 14.99 BGN )'
        return "'[$originalPos] $collectionName($primaryGenreName)' by '$artistName', $releaseDateShortened ( $collectionPrice $currency )"
    }

}

/**
 * Justa a list of albums
 * @param list
 */
class AlbumCollection  (val list: List<Album>)
{
    /**
     * Used when the source is TopAlbums list
     * The source is a model object generated from the original JSON.
     */
    constructor(topAlbumsCollection: TopAlbumsCollection): this(mutableListOf<Album>())
    {
       topAlbumsCollection.feed?.topAlbums?.forEachIndexed() { i: Int, topAlbum: TopAlbum ->
           (list as MutableList).add(Album(i, topAlbum))
        }
    }


    /**
     * Used when the source is Artist's Albums list.
     * The source is a model object generated from the original JSON.
     */
    constructor(artistAlbumsCollection: ArtistAlbumsCollection): this(mutableListOf<Album>())
    {
        artistAlbumsCollection.results?.forEachIndexed { i: Int, artistAlbum: ArtistAlbum ->
            (list as MutableList).add(Album(i, artistAlbum))
        }
    }

}

