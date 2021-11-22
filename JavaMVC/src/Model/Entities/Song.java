package Model.Entities;

import Controller.Application;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class representing a song entity in DB.
 *
 * @author Raman Zatsarenko | rz4983@rit.edu
 * @author Austin Couch | alc9026@rit.edu
 * @author Srikar Sundaram | ss8931@rit.edu
 * @author Ricky Gupta | rg4825@rit.edu
 * @version 2021.10.24.1
 */
public class Song {

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
        }
    }

    /**
     * The unique alphanumeric string that identifies this song.
     */
    private final String songID;
    /**
     * The length of the song in ms.
     */
    private final int length;
    /**
     * The name of the song.
     */
    private final String title;
    /**
     * The date the song was released in format yyyy-mm-dd (or yyyy if full date is unavailable).
     */
    private final String songReleaseDate;

    /**
     * Constructor is called after needed data from DB is queried.
     *
     * @param songID          unique string identifier
     * @param length          length of song in ms
     * @param title           name of the song
     * @param songReleaseDate the date in which the song was released
     */
    public Song(String songID, int length, String title, String songReleaseDate) {
        this.songID = songID;
        this.length = length;
        this.title = title;
        this.songReleaseDate = songReleaseDate;
    }

    public String queryArtist() throws SQLException {
        String query = "Select get_song_artist('" + this.songID + "');";

        ResultSet rs = stmt.executeQuery(query);

        String songArtist = "";
        while (rs.next()) {
            songArtist = rs.getString("get_song_artist");
        }
        if (songArtist == null || songArtist.equals("")) {
            songArtist = "N/A";
        }

        return songArtist;
    }

    public String getSongID() {
        return songID;
    }

    public int queryNumPlayed() throws SQLException {
        int numPlays = 0;
        String getCount =
            "SELECT COUNT(*) FROM \"Plays\" WHERE \"songID\" = '" + this.songID + "' and email = '"
                + Application.getCurrentUser().getEmail() + "';";

        ResultSet rs = stmt.executeQuery(getCount);

        while (rs.next()) {
            numPlays = rs.getInt("count");
        }
        return numPlays;
    }

    public String queryAlbum() throws SQLException {
        String query = "Select get_song_album('" + this.songID + "');";

        ResultSet rs = stmt.executeQuery(query);

        String songAlbum = "";

        while (rs.next()) {
            songAlbum = rs.getString("get_song_album");
        }
        if (songAlbum == null || songAlbum.equals("")) {
            songAlbum = "N/A";
        }

        return songAlbum;
    }

    /**
     * @return TODO
     */
    @Override
    public String toString() {

        try {
            return String.format(
                "Song: %s \n\t-- Artist: %s \n\t-- Album: %s \n\t-- Minutes: %.2f \n\t-- Release Date: %s \n\t-- " +
                    "Number of plays: %d", title, queryArtist(), queryAlbum(),
                length / 1000.0 / 60, songReleaseDate, queryNumPlayed());
        } catch (SQLException ignore) {
        }
        return "SQL Exception";
    }
}
