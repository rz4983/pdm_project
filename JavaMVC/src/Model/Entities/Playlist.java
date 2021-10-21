package JavaMVC.src.Model.Entities;

import java.util.ArrayList;
import java.util.Collection;

public class Playlist {
    private String playlistID;
    private String name;
    private Collection<Song> songs;

    Playlist(String playlistID, String name, Collection<Song> songs) {
        this.playlistID = playlistID;
        this.name = name;
        this.songs = songs;
    }

    Playlist(String playlistID) {
        // Create all of this by fetching form the database.
        this(playlistID, null, getSongs(playlistID));
    }

    private static Collection<Song> getSongs(String playlistID) {
        Collection<Song> songs = new ArrayList<Song>();
        return songs;
    }
}
