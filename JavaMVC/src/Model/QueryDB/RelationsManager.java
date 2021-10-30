package Model.QueryDB;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;
import java.sql.*;

/**
 * FIXME This class manages all relation not between user and some other entity. The logic behind
 * this decision is because we only need to keep in memory anything the user is not interacting with
 * right now. So it manages everything but relations between (current) user and other users, and
 * (current) user and their playlists.
 */
public class RelationsManager {

    private static SQLException SQLException;

    // User adds a given song to a given playlist
    // does not return anything
    public static void addSong(Song songID, Playlist playlist) throws SQLException {
        boolean alreadyRelated = false;

        // Query to see if song-playlist relation already exists
        String checkQ = "SELECT 1" +
                " FROM \"Contains\"" +
                " WHERE \"playlistID\" = '" + playlist +
                "' and \"songID\" = '" + songID + "'";
        try (Connection conn = Controller.PostgresSSHTest.Database.getConn()) {

        Statement stmt = conn.createStatement();

        alreadyRelated = stmt.execute(checkQ);

        if (!alreadyRelated){
            String QUERY2 = "INSERT INTO \"Contains\" VALUES ('"
                    + songID + "', '"
                    + playlist + "');";
        }

        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public static void removeSong(Song songID, Playlist playlist) {}

    public static void addAlbum(Album album, Playlist playlist) {}

    public static void removeAlbum(Album album, Playlist playlist) {}

    public static void sharePlaylist(Playlist searchPlaylist, User searchUser) {}

    public static void rename(Playlist searchPlaylist, String field) {}

    public static void createPlaylist(String playlistName) {}
}
