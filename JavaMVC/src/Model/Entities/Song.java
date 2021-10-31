package Model.Entities;

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
    /** The unique alphanumeric string that identifies this song. */
    private final String songID;
    /** The length of the song in ms. */
    private final int length;
    /** The name of the song. */
    private final String title;
    /** The date the song was released in format yyyy-mm-dd (or yyyy if full date is unavailable). */
    private final String songReleaseDate;

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor is called after needed data from DB is queried.
     *
     * @param songID unique string identifier
     * @param length length of song in ms
     * @param title name of the song
     * @param songReleaseDate the date in which the song was released
     */
    public Song(String songID, int length, String title, String songReleaseDate) {
        this.songID = songID;
        this.length = length;
        this.title = title;
        this.songReleaseDate = songReleaseDate;
    }

    //TODO helper methods

    /**
     * TODO
     */
    public void playSong() {
        // mark song as played
    }

    public String queryArtist() throws SQLException {
        String checkQ = "Select get_song_artist('" + this.songID +"');";

        ResultSet rs = stmt.executeQuery(checkQ);

        String songArtist = "";
        while (rs.next()) {
            songArtist = rs.getString("get_song_artist");
        }
        if (songArtist == null || songArtist.equals("")){
            songArtist = "N/A";
        }

        return songArtist;
    }

    public String getSongID() {
        return songID;
    }

    public int getLength() {
        return length;
    }

    public String getTitle() {
        return title;
    }

    public String getSongReleaseDate() {
        return songReleaseDate;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static void setStmt(Statement stmt) {
        Song.stmt = stmt;
    }

    public int getNumPlayed() throws SQLException {
        int numPlays = 0;
        String getCount = "SELECT COUNT(*) FROM \"Plays\" WHERE \"songID\" = '" + this.songID + "';";

        ResultSet rs = stmt.executeQuery(getCount);

        while(rs.next()){
             numPlays = rs.getInt("count");
        }
        return numPlays;
    }

    /**
     * @return TODO
     */
    @Override
    public String toString() {

        try {
            return String.format("Song: %s -- Artist: %s -- Minutes: %.2f -- Release Date: %s -- Number of plays: %d", title, queryArtist(),
                    length / 1000.0 / 60, songReleaseDate, getNumPlayed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "SQL Exception";
    }
}
