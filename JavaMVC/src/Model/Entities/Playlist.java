package Model.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Playlist {
    private String playlistID;
    private String name;

    public Playlist(String playlistID, String name) {
        this.playlistID = playlistID;
        this.name = name;
    }

    Playlist(String playlistID) {
        // Create all of this by fetching form the database.
        this(playlistID, null);
    }

    public List<Song> getSongs() {
        // TODO
        return null;
    }
}
