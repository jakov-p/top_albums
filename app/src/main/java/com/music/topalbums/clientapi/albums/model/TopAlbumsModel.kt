package com.music.topalbums.clientapi.albums

import com.google.gson.annotations.SerializedName

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */


class TopAlbumsCollection
{
    var feed: Feed? = null
    //var id: Id? = null
}


class Feed
{
    var author: Author? = null
    var entry: ArrayList<Entry>? = null
    var updated: Updated? = null
    var rights: Rights? = null
    var title: Title? = null
    var icon: Icon? = null
    var link: ArrayList<Link>? = null
    var id: Id? = null
}

class Entry
{
    @SerializedName("im:name")
    var im_name: ImName? = null
    @SerializedName("im:image")
    var im_image: List<ImImage>? = null
    @SerializedName("im:itemCount")
    var im_itemCount: ImItemCount? = null
    @SerializedName("im:price")
    var im_price: ImPrice? = null
    @SerializedName("im:contentType")
    var im_contentType: ImContentType? = null
    var rights: Rights? = null
    var title: Title? = null
    var link: Link? = null
    var id: Id? = null
    var im_artist: ImArtist? = null
    var category: Category? = null
    var im_releaseDate: ImReleaseDate? = null
}


class Attributes
{
    var height: String? = null
    var rel: String? = null
    var type: String? = null
    var href: String? = null
    var amount: String? = null
    var currency: String? = null
    var term: String? = null
    var label: String? = null
    @SerializedName("im:id")
    var im_id: String? = null
    var scheme: String? = null
}

class Author
{
    var name: Name? = null
    var uri: Uri? = null
}

class Category
{
    var attributes: Attributes? = null
}


class Icon
{
    var label: String? = null
}

class Id
{
    var label: String? = null
    var attributes: Attributes? = null
}

class ImArtist
{
    var label: String? = null
    var attributes: Attributes? = null
}

class ImContentType
{
    @SerializedName("im:contentType")
    var im_contentType: ImContentType? = null
    var attributes: Attributes? = null
}

class ImImage
{
    var label: String? = null
    var attributes: Attributes? = null
}

class ImItemCount
{
    var label: String? = null
}

class ImName
{
    var label: String? = null
}

class ImPrice
{
    var label: String? = null
    var attributes: Attributes? = null
}

//Date
class ImReleaseDate
{
    var label: String? = null
    var attributes: Attributes? = null
}

class Link
{
    var attributes: Attributes? = null
}

class Name
{
    var label: String? = null
}

class Rights
{
    var label: String? = null
}

class Title
{
    var label: String? = null
}

//Date
class Updated
{
    var label: String? = null
}

class Uri
{
    var label: String? = null
}



