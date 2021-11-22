package Model.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an Album entity
 *
 * @author  Raman Zatsarenko | rz4983@rit.edu
 * @author  Austin Couch | alc9026@rit.ed
 * @author  Srikar Sundaram | ss8931@rit.edu
 * @author  Ricky Gupta | rg4825@rit.edu
 *
 * @version 2021.10.24.1
 *
 */
public class Album {

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
        }
    }

    /** Unique alphanumeric String identifying this album entity. */
    private final String albumID;
    /** Non-unique String for the title of this album entity. */
    private final String title;
    /** String in format y-m-d or just y */
    private final String AlbumReleaseDate;

    /**
     * Constructor called after needed data is queried from database
     *
     * @param albumID unique String identifier
     * @param title non-unique String title
     * @param AlbumReleaseDate String release date in y-m-d or y
     */
    public Album(String albumID, String title, String AlbumReleaseDate) {
        this.albumID = albumID;
        this.title = title;
        this.AlbumReleaseDate = AlbumReleaseDate;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static void setStmt(Statement stmt) {
        Album.stmt = stmt;
    }

    public String getAlbumID() {
        return albumID;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbumReleaseDate() {
        return AlbumReleaseDate;
    }

    public String queryArtist() throws SQLException {
        String checkQ = "Select get_album_artist('" + this.albumID + "');";

        ResultSet rs = stmt.executeQuery(checkQ);

        String albumArtist = "";
        while (rs.next()) {
             albumArtist = rs.getString("get_album_artist");
        }

        if (albumArtist == null || albumArtist.equals("")){
            albumArtist = "N/A";
        }

        return albumArtist;
    }

    public List<Song> getSongs(){

        List<Song> songs = new ArrayList<Song>();

        String getSongs = "SELECT \"Song\".* FROM \"Song\", \"ComposedOf\" " +
                "WHERE \"ComposedOf\".\"albumID\" = '"+ this.albumID +"' AND " +
                "      \"ComposedOf\".\"songID\" = \"Song\".\"songID\"";

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
     * @return string representation of album formatted for ptui
     */
    @Override
    public String toString() {
        try {
            return String.format("Album: %s \n\t-- Author: %s \n\t-- Release Date: %s", this.title,
                    queryArtist(), this.AlbumReleaseDate);
        } catch (SQLException e) {
        }
        return "SQL Exception";
    }
}
