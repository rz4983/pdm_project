package Model.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class representing a playlist entity in DB, with references to the songs it
 * contains.
 *
 * @author Raman Zatsarenko | rz4983@rit.edu
 * @author Austin Couch | alc9026@rit.edu
 * @author Srikar Sundaram | ss8931@rit.edu
 * @author Ricky Gupta | rg4825@rit.edu
 * @version 2021.10.24.1
 */
public class Playlist {
    /** The unique alphanumeric string that identifies this playlist. */
    private final String playlistID;
    /** The name of the playlist. */
    private final String name;

    /**
     * Constructor is called after needed data is queried from DB.
     *
     * @param playlistID unique string identifier
     * @param name non-unique string
     */
    public Playlist(String playlistID, String name) {
        this.playlistID = playlistID;
        this.name = name;
    }

    //TODO helper methods

    /**
     * TODO
     *
     * @return
     */
    public List<Song> getSongs() {
        // TODO
        return null;
    }

    /**
     * @return String representation of playlist for PTUI
     */
    @Override
    public String toString() {
        return "Playlist{" +
                "playlistID='" + playlistID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
