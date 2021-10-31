package Model.Entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            e.printStackTrace();
        }
    }

    /** Unique alphanumeric String identifying this album entity. */
    private final String AlbumID;
    /** Non-unique String for the title of this album entity. */
    private final String title;
    /** String in format y-m-d or just y */
    private final String AlbumReleaseDate;

    /**
     * Constructor called after needed data is queried from database
     *
     * @param AlbumID unique String identifier
     * @param title non-unique String title
     * @param AlbumReleaseDate String release date in y-m-d or y
     */
    public Album(String AlbumID, String title, String AlbumReleaseDate) {
        this.AlbumID = AlbumID;
        this.title = title;
        this.AlbumReleaseDate = AlbumReleaseDate;
    }

    public String queryArtist() throws SQLException {
        String checkQ = "Select get_album_artist('" + this.AlbumID + "')';";

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

    /**
     * @return string representation of album formatted for ptui
     */
    @Override
    public String toString() {
        try {
            return String.format("Album: %s -- Author: %s -- Release Date: %s", this.title,
                    queryArtist(), AlbumReleaseDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "SQL Exception";
    }
}
