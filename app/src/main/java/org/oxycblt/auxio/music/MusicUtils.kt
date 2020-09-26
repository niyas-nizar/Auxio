package org.oxycblt.auxio.music

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.oxycblt.auxio.R
import org.oxycblt.auxio.music.models.Album
import org.oxycblt.auxio.music.models.Artist

// List of ID3 genres + Winamp extensions, each index corresponds to their int value.
// There are a lot more int-genre extensions as far as Im aware, but this works for most cases.
private val ID3_GENRES = arrayOf(
    "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
    "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno",
    "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno",
    "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental",
    "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk",
    "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave",
    "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
    "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American",
    "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal",
    "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock",

    // Winamp extensions
    "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival",
    "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock",
    "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour",
    "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus",
    "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad",
    "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella",
    "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie",
    "Britpop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal",
    "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal",
    "Anime", "JPop", "Synthpop"
)

const val PAREN_FILTER = "()"

// --- EXTENSION FUNCTIONS ---

// Convert legacy ID3 genres to a named genre
fun String.toNamedGenre(): String? {
    // Strip the genres of any parentheses, and convert it to an int
    val intGenre = this.filterNot {
        PAREN_FILTER.indexOf(it) > -1
    }.toInt()

    // If the conversion fails [Due to the genre using an extension that Auxio doesn't have],
    // then return null.
    return ID3_GENRES.getOrNull(intGenre)
}

// Convert a song to its URI
fun Long.toURI(): Uri {
    return ContentUris.withAppendedId(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        this
    )
}

// Convert an albums ID into its album art URI
fun Long.toAlbumArtURI(): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        this
    )
}

// Convert a string into its duration
fun Long.toDuration(): String {
    val durationString = DateUtils.formatElapsedTime(this)

    val durationSplit = durationString.chunked(1).toMutableList()

    // Iterate through the string and remove the first zero found
    // If anything else is found, exit the loop.
    for (i in 0 until durationSplit.size) {
        if (durationSplit[i] == "0") {
            durationSplit.removeAt(i)

            break
        } else {
            break
        }
    }

    return durationSplit.joinToString("")
}

// --- BINDING ADAPTERS ---

fun getAlbumSongCount(album: Album, context: Context): String {
    return context.resources.getQuantityString(
        R.plurals.format_song_count, album.numSongs, album.numSongs
    )
}

// Format the amount of songs in an album
@BindingAdapter("songCount")
fun TextView.bindAlbumSongs(album: Album) {
    text = getAlbumSongCount(album, context)
}

@BindingAdapter("artistCounts")
fun TextView.bindArtistCounts(artist: Artist) {
    val albums = context.resources.getQuantityString(
        R.plurals.format_albums, artist.numAlbums, artist.numAlbums
    )
    val songs = context.resources.getQuantityString(
        R.plurals.format_song_count, artist.numSongs, artist.numSongs
    )

    text = context.getString(R.string.format_double_counts, albums, songs)
}

// Get the artist genre.
// TODO: Add option to list all genres
@BindingAdapter("artistGenre")
fun TextView.bindArtistGenre(artist: Artist) {
    // If there are multiple genres, then pick the most "Prominent" one,
    // Otherwise just pick the first one
    if (artist.genres.keys.size > 1) {
        text = artist.genres.keys.sortedByDescending {
            artist.genres[it]?.size
        }[0]
    } else {
        text = artist.genres.keys.first()
    }
}

// Get a bunch of miscellaneous album information [Year, Songs, Duration] and combine them
@BindingAdapter("albumDetails")
fun TextView.bindAlbumDetails(album: Album) {
    text = context.getString(
        R.string.format_double_info,
        album.year.toString(),
        getAlbumSongCount(album, context),
        album.totalDuration
    )
}
