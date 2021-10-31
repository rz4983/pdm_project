package Model.QueryDB;

import Controller.Application;
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
        String songID = song.getSongID();
        String playlistID = playlist.getPlaylistID();
        String query = "CALL add_song('" + songID + "', '" + playlistID + "');";
        stmt.execute(query);
    }

    public static void removeSong(Song song, Playlist playlist) throws SQLException {
        String songID = song.getSongID();
        String playlistID = playlist.getPlaylistID();
        String query = "CALL remove_song('" + songID + "', '" + playlistID + "');";
        stmt.execute(query);
    }

    public static void addAlbum(Album album, Playlist playlist) throws SQLException {
        String albumID = album.getAlbumID();
        String playlistID = playlist.getPlaylistID();
        String query = "CALL add_album('" + albumID + "', '" + playlistID + "');";
        stmt.execute(query);
    }

    public static void removeAlbum(Album album, Playlist playlist) throws SQLException {
        String albumID = album.getAlbumID();
        String playlistID = playlist.getPlaylistID();
        String query = "CALL remove_album('" + albumID + "', '" + playlistID + "');";
        stmt.execute(query);
    }

    public static void sharePlaylist(Playlist searchPlaylist, User searchUser) throws SQLException {
        String sharePlaylist = "INSERT INTO \"Make\" " +
                "VALUES ('" + searchUser.getEmail() + "', '" + searchPlaylist.getPlaylistID() + "') " +
                "ON CONFLICT DO NOTHING;";

        stmt.execute(sharePlaylist);
    }

    public static void rename(Playlist searchPlaylist, String field) throws SQLException {
        String playlistID = searchPlaylist.getPlaylistID();
        String query = "CALL rename_playlist('" + playlistID + "', '" + field + "');";
        stmt.execute(query);
    }

    public static void play(Song song) {

        String playSong = "INSERT INTO \"Plays\" \n" +
                "VALUES ('" + Controller.Application.getCurrentUser().getEmail() + "', '" + song.getSongID() + "');";

        try {
            stmt.execute(playSong);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPlaylist(String playlistName) throws SQLException {
        UUID id = UUID.randomUUID();

        String createPlaylist = "INSERT INTO \"Playlist\" VALUES ('"
            + id + "' , '"
            + playlistName + "');"
            + " INSERT INTO \"Make\" VALUES ('" + Application.getCurrentUser().getEmail() + "', '"
            + id +
            "'); ";

        stmt.execute(createPlaylist);

        System.out.printf("Playlist '%s' has been created%n", playlistName);
    }

    public static void deletePlaylist(Playlist playlist) throws SQLException {
        String playlistId = playlist.getPlaylistID();

        String deletePlaylist = "DELETE FROM \"Contains\" " +
                "WHERE \"playlistID\" = '" + playlistId + "'; " +
                "DELETE FROM \"Make\" " +
                "WHERE \"playlistID\" = '" + playlistId + "'; " +
                "DELETE FROM \"Playlist\" WHERE " +
                "\"playlistID\" = '" + playlistId + "';";

        stmt.execute(deletePlaylist);
    }
}

