### In short

‘Top albums’ application downloads and shows the current top albums list of a chosen country.  The list can be filtered and searched. Additionally with a click on an album the application will show the song list of the album. Any song can be played (the first 30 seconds).  Also, it is possible to get the list of all the albums ever released by an author.

By clicking on floating buttons it is possible to visit the album’s or artist’s web page.

You can see the application in action in "videos" subfolder. 



# ‘Top albums’ fragment

This fragment is the entry into the application and shows the top albums list in a selected country.
The albums are shown in the same order as they are positioned in the top list.

![Screenshot_20230501_235031](https://github.com/jakov-p/top_albums/assets/134881206/c8de2062-b228-483e-be66-b4550137f861)

Each row has the album's cover image, the albums name, the artist's name and its genre. At the end of the line the position in the list is given. The list rows are of alternate colours so that the rows are recognized from each other.

When an album is selected (clicked) a new fragment with the album songs will be opened.


### Country selection
The country is selected by clicking on the control with the current country's name and a special third party control for country selection will be opened.

![choosing_a_country](https://github.com/jakov-p/top_albums/assets/134881206/026a02aa-48a7-40b3-9950-66419571021e)

As soon as a new country is selected the list starts being filled with the new content.


### Search
Any text entered longer than 2 letters is searched in the album list. If an album contains this text is the album's name or in the artist's name, then that text will be highlighted (made bigger and bold). The search is case insensitive and can spread over words (spaces can be part of the searched text).

The search is being made immediately as the text is being typed, so that the user can see right away in the list what albums satisfy the search criteria.

![search_text](https://github.com/jakov-p/top_albums/assets/134881206/7343fb58-a7cb-4578-b761-8e8af3c9419c)

If the entered text is shorter than 3 letters than search will not be performed.



### Filter
Albums can be filtered for their genre and the release date.

The user enters the filter dialog by clicking on the "Filter" floating button. There by clicking on chip controls the user can choose a genre and/or a release date criteria.

![filter_options_dialog](https://github.com/jakov-p/top_albums/assets/134881206/4e5a1c23-bd1f-4e7d-8abe-2329a27f07a9)

Only a single genre can be selected. Then only the albums of this genre will be shown in the album list (after the dialog is closed). The choice of genres is limited to just a small subset of available genres (there are many more genres not offered in this dialog).
If no genre is selected (by unchecking "genre" button), then any album regardless of genre is shown in the list.

Also, only one release date criteria can be chosen. "Newer than 1 year" means that any album released in the period between this date and the date one year ago will be shown in the list.
If no release date criteria is selected (by unchecking "Release time" button), then any album regardless of its release date is shown in the list.

As soon as the dialog is closed the filter is finally applied on the album list. Also, the selected genre and the selected release criteria are shown above the search phrase area in the upper portion of the screen.
The dialog is closed by clicking outside the dialog box.

![search_text_plus_filter](https://github.com/jakov-p/top_albums/assets/134881206/43a080a9-0eb8-4074-be88-56b79d8d0cab)

Filter can always be combined with text search.


# ‘Songs’  fragment
This fragment shows an album’s song list.

The upper part of the screen shows the album's cover, the album’s name, and the name of the author.

Below is the list of all the songs on the album. Each song has its duration at the end of the row. If the name of the song is too long for one line it gets wrapped into the next line.
The list rows are of alternate colours so that the rows are recognized from each other.



### Floating buttons
There are floating button at the bottom of the page. On its click two hidden buttons are shown.

One button leads to the fragment with the list of all the albums of this author.  

![floating_button_for_album_web_page](https://github.com/jakov-p/top_albums/assets/134881206/3250bade-5878-4106-a89f-0a527f5a6ad7)

The other one opens the album's web page in a browser.

![album_web_page](https://github.com/jakov-p/top_albums/assets/134881206/8fe2ec95-9d16-4c34-8a6a-bec79809c407)



### Song playing
When a song is selected (clicked) it will begin to play. It usually takes a second or two before the player is shown at the bottom of the screen. When the song is selected its row will flash for a moment to give the user a clear indication that the song's playing is about to start.

![playing_a_song](https://github.com/jakov-p/top_albums/assets/134881206/a3eb76e0-28ce-4e1c-8213-e993877ca46a)

The player has a standard look with the play, pause and stop buttons, together with a progress bar. The user can, by a click on the progress bar, jump to any second of the song and continue playing from that position.

Each song is limited to only 30 seconds of playing time (that is the length of the downloaded sample).

While the player is on the screen the playing song will have in its row an animated gif indicating it. This way the user any time can easily recognize the song being played. Also, the list is darkened so that the player area better stands out on the screen.

The dialog is closed by clicking outside of it.





# ‘Artist albums’ fragment

This fragment shows the list of all the albums ever released by an author.

The albums are sorted by the release date - the newest ones are at the top. Each row contains the album's cover image, the albums name, the month and the year of release and its genre. At the end of the line the number of the tracks on the album is given. The list rows are of alternate colours so that the rows are recognized from each other.

![artist_albums](https://github.com/jakov-p/top_albums/assets/134881206/3af73fcc-e5a1-4e06-af4e-f0cb63924d3a)

When an album is selected (clicked) a new fragment with the album songs will be opened.



### Floating buttons
There is a floating button at the bottom of the page leading to the artist’s web page. 

![floating_button_for_artist_web_page](https://github.com/jakov-p/top_albums/assets/134881206/aa9351e3-091d-4b84-8600-b735dff1dc77)

The click on it opens this page in a browser.




# Technical considerations 

Note that https://itunes.apple.com/us/rss/topalbums/limit=100/json  never returns 100 albums, the number is significantly smaller - often less than 50.  

Top album list loading time can be quite long - in a range from a fraction of a second up to 10 seconds. Once a country is selected again the next download of the same country will be significantly faster, so there is no need for caching them on the mobile device for better subsequential downloads.

None of the lists in the application (a top album list, a song list, an artist's album list) has support for paging on the web directly (in Internet API). Meaning that only the full list can be downloaded as a whole, not just an arbitrary part of the list. So, to show the first items on the screen the whole list had to be downloaded.

Although the internet API does not support direct paging, the application itself implements paging (Paging 3 control is used), but that paging is implemented only between the GUI control and the data source on the mobile device, which already has the whole list downloaded from the site.


### More complex solution
This solution with download of the whole list to before being able to show the first items to the user proved to be unsatisfactory. The user sometimes would have to wait for 10 seconds looking at the white list area and a circling progress bar, so a new improved approach is implemented to shorted the period until the first items becomes visible on the screen.

As already explained, the Internet API does not support paging, but fortunately it supports limiting the length of the country’s top album list. And this functionality is used for this new approach.

When the user selects a country than in the main thread not a whole list, but only a small list limited to only 8 countries is requested. This request takes much shorter time to be processed by the site, so it usually comes in a fraction of second. So very quickly the first items are shown to the user. At the same time in the background the whole list (without a limit) is being downloaded. At the moment when the user wants to scroll down to the 9th item (beyond the small list's limit) usually, the whole list is already downloaded, and the user can seamlessly scroll downwards. If the whole list is still not available, the user will feel a little delay (as if being blocked).

All this complicates the algorithm, but improves the user experience.


### When download fails 
Sometimes a download fails (for whatever reason) and the user is offered a "Retry" button, after which the same download often succeeds.

But there are such top album lists or song lists where download never succeeds - either the returned JSON content is problematic or it is simply empty.

In some cases the returned JSON is of proper structure, but contains no entry - no albums or no songs. In that case the text "No result" is shown on the screen (the "Retry" button is not offered).


### The country selector control 
The country selector control is a third-party control taken from this place
[https://github.com/hbb20/CountryCodePickerProject](https://github.com/hbb20/CountryCodePickerProject)


### The last selected country is remembered
The last selected country is remembered after exiting application. On the next start that country will be already preselected.

