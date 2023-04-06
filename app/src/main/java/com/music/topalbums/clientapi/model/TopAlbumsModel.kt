package com.music.topalbums.clientapi.albums

import com.google.gson.annotations.SerializedName


/*
{"feed":{"author":{"name":{"label":"iTunes Store"}, "uri":{"label":"http://www.apple.com/itunes/"}}, "entry":[
{"im:name":{"label":"Gettin' Old"}, "im:image":[
{"label":"https://is4-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/55x55bb.png", "attributes":{"height":"55"}},
{"label":"https://is3-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/60x60bb.png", "attributes":{"height":"60"}},
{"label":"https://is2-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/170x170bb.png", "attributes":{"height":"170"}}],
"im:itemCount":{"label":"18"},
"im:price":{"label":"$13.99", "attributes":{"amount":"13.99", "currency":"USD"}},
"im:contentType":{"im:contentType":{"attributes":{"term":"Album", "label":"Album"}}, "attributes":{"term":"Music", "label":"Music"}},
"rights":{"label":"℗ 2023 River House Artists LLC, under exclusive license to Sony Music Entertainment. All rights reserved."},
"title":{"label":"Gettin' Old - Luke Combs"}, "link":{"attributes":{"rel":"alternate", "type":"text/html", "href":"https://music.apple.com/us/album/gettin-old/1666738524?uo=2"}},
"id":{"label":"https://music.apple.com/us/album/gettin-old/1666738524?uo=2", "attributes":{"im:id":"1666738524"}},
"im:artist":{"label":"Luke Combs", "attributes":{"href":"https://music.apple.com/us/artist/luke-combs/815635315?uo=2"}},
"category":{"attributes":{"im:id":"6", "term":"Country", "scheme":"https://music.apple.com/us/genre/music-country/id6?uo=2", "label":"Country"}},
"im:releaseDate":{"label":"2023-03-24T00:00:00-07:00", "attributes":{"label":"March 24, 2023"}}},
 */

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
    @SerializedName("im:artist")
    var im_artist: ImArtist? = null
    var category: Category? = null
    @SerializedName("im:releaseDate")
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



