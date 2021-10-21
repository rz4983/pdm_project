package Model;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Holds all static methods used for searching. Can search:
 */
public class Search {
    /**
     * Search songs if they appear in the title. Case insensitive.
     *
     * @param term
     * @return
     */
    public static Collection<Song> searchSongs(String term) {
        return searchSongs(Song.class.getSimpleName(), term, null, true);
    }

    /**
     *
     * @param category category | must be in entity. use Entity.className.class.toString()
     * @param term term in the category's title or equivalent.
     * @param sort null if sorted by song name then artist.
     * @param ascending true if ascending.
     * @return Collection of songs sorted
     */
    public static Collection<Song> searchSongs(
            String category, String term, String sort, boolean ascending) {
        // TODO: please write an actual sql query here.
        ArrayList<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < 100; i++) {
            songs.add(
                    new Song(
                            "Some nonsense songID",
                            ThreadLocalRandom.current().nextInt(),
                            "Some nonsense songID",
                            ThreadLocalRandom.current().nextInt()));
        }
        return songs;
    }

    public static Collection<Playlist> searchPlaylist(String term) {
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        for (int i = 0; i < 100; i++) {
            playlists.add(new Playlist("playlistID", "name"));
        }
        return playlists;
    }

    public static User searchUser(String email) {
        return new User("munson@rit.edu", "munson", 0, 0, "munson", "munson", 0);
    }
}
