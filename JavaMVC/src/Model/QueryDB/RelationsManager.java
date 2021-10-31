package Model.QueryDB;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * FIXME This class manages all relation not between user and some other entity. The logic behind
 * this decision is because we only need to keep in memory anything the user is not interacting with
 * right now. So it manages everything but relations between (current) user and other users, and
 * (current) user and their playlists.
 */
public class RelationsManager {

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // User adds a given song to a given playlist
    // does not return anything
    public static void addSong(Song song, Playlist playlist) throws SQLException {
        /*boolean alreadyRelated = false;

        // Query to see if song-playlist relation already exists
        String checkQ = "SELECT *" +
                " FROM \"Contains\"" +
                " WHERE \"playlistID\" = '" + playlist +
                "' and \"songID\" = '" + songID + "'";

        ResultSet rs = stmt.executeQuery(checkQ);

        alreadyRelated = rs.next();

        if (!alreadyRelated) {
            String addSong = "INSERT INTO \"Contains\" VALUES ('"
                    + songID + "', '"
                    + playlist + "');";
            stmt.executeQuery(addSong);
        }*/
        String songID = song.getSongID();
        String playlistID = playlist.getPlaylistID();


    }

    public static void removeSong(Song songID, Playlist playlist) {
        // check if in
        // if in -> remove from playlist
    }

    public static void addAlbum(Album album, Playlist playlist) {

    }

    public static void removeAlbum(Album album, Playlist playlist) {
    }

    public static void sharePlaylist(Playlist searchPlaylist, User searchUser) {
    }

    public static void rename(Playlist searchPlaylist, String field) {

    }

    public static void play(Song song){

    }

    public static void createPlaylist(String playlistName) throws SQLException {


        String createPlaylist = "INSERT INTO \"Playlist\" VALUES ('"
                + UUID.randomUUID() + "' , '"
                + playlistName + "');";

        stmt.execute(createPlaylist);

        System.out.printf("Playlist '%s' has been created%n", playlistName);
    }
}

