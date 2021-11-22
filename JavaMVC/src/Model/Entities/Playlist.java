package Model.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
        }
    }

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

    public List<Song> getSongs(){

        List<Song> songs = new ArrayList<Song>();

        String getSongs = "SELECT \"Song\".* FROM \"Song\", \"Contains\" " +
                "WHERE \"Contains\".\"playlistID\" = '"+ this.playlistID +"' AND " +
                "      \"Contains\".\"songID\" = \"Song\".\"songID\"";

        ResultSet rs = null;
        // assign to result set
        try {
             rs = stmt.executeQuery(getSongs);

             // loop through and make song entities and add to new list
             while (rs.next()){
                Song newSong = new Song(rs.getString("songID"),
                        rs.getInt("length"),
                        rs.getString("title"),
                        rs.getString("songReleaseDate"));

                songs.add(newSong);
             }
        } catch (SQLException e) {
        }

        // return list
        return songs;
    }

    /**
     * @return String representation of playlist for PTUI
     */
    @Override
    public String toString() {
        return String.format("Playlist: %s \n\t-- Number of songs: %s \n\t-- Runtime %s", this.name, getSongs().size(), Math.round(this.runtime / 100.0 / 60) / 10.0);
    }

    public String getPlaylistID() {
        return this.playlistID;
    }
}
