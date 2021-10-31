package Model.Entities;

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

    private final int runtime;

    /**
     * Constructor is called after needed data is queried from DB.
     *
     * @param playlistID unique string identifier
     * @param name non-unique string
     */
    public Playlist(String playlistID, String name, int runtime) {
        this.playlistID = playlistID;
        this.name = name;
        this.runtime = runtime;
    }

    //TODO helper methods

    /**
     * TODO
     *
     * @return
     */
    public List<Song> getSongs() {

        String query = "SELECT \"Song\".* FROM \"Song\", \"Contains\" " +
                "WHERE \"Contains\".\"playlistID\" = '"+ playlistID +"' AND " +
                "      \"Contains\".\"songID\" = \"Song\".\"songID\"";
        // assign to result set
        // loop through and make song entities and add to new list

        // return list
        return null;
    }

    /**
     * @return String representation of playlist for PTUI
     */
    @Override
    public String toString() {
        return String.format("Playlist: %s -- Number of songs -- %s -- Runtime %s%n", this.name, getSongs().size(), this.runtime);
    }

    public String getPlaylistID() {
        return this.playlistID;
    }
}
