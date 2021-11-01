package Model.QueryDB;

import Controller.Application;
import Controller.PostgresSSHTest.Database;
import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all static methods used for searching. Can search.
 */
public class Search {

    private static int limit = 100;

    /**
     * Search songs if they appear in the title. Case-insensitive.
     *
     * @param term
     * @return
     */
    public static List<Song> searchSongs(String term) throws SQLException {
        return searchSongs("song", term, "song", true);
    }

    public static void setLimit(int limit) {
        Search.limit = limit;
    }

    /**
     * @param category  category | must be in entity. use Entity.className.class.toString()
     * @param term      term in the category's title or equivalent.
     * @param sort      null if sorted by song name then artist.
     * @param ascending true if ascending.
     * @return List of songs sorted
     */
    public static List<Song> searchSongs(String category, String term, String sort,
        boolean ascending) throws SQLException {
        boolean artistReq = sort.equalsIgnoreCase("artist");
        String orderBy = "order by " + switch (sort.toLowerCase()) {
            default -> {
                artistReq = true;
                yield "\"Song\".\"title\" " + (ascending ? "ASC" : "DESC")
                    + ",  \"Artist\".\"artistName\" ";
            }
            case "artist" -> "\"Artist\".\"artistName\" ";
            case "genre" -> "\"Genre\".\"genreName\" ";
            case "year" -> "\"Song\".\"songReleaseDate\" ";
        } + (ascending ? "ASC" : "DESC");

        String query = switch (category.toLowerCase()) {
            case "genre" -> "SELECT \"Song\".* FROM \"Song\", \"SongGNR\", \"Genre\""
                + (artistReq ? ", \"Artist\", \"Compose\" " : "")
                + " WHERE LOWER(\"Genre\".\"genreName\") = LOWER('" + term + "') "
                + "   and \"Genre\".\"genreID\" = \"SongGNR\".\"genreID\""
                + "   and \"Song\".\"songID\" = \"SongGNR\".\"songID\""
                + (artistReq ?
                " and  \"Artist\".\"artistName\"= \"Compose\".\"artistName\" "
                    + "  and \"Song\".\"songID\" = \"Compose\".\"songID\" " : "");

            case "song" -> "SELECT \"Song\".* FROM \"Song\""
                + (artistReq ? ", \"Artist\", \"Compose\" " : "")
                + (sort.equalsIgnoreCase("genre") ? ", \"Genre\", \"SongGNR\" " : "")
                + "     WHERE"
                + "     \"Song\".title ILIKE '%" + term + "%'"
                + (artistReq ?
                " and  \"Artist\".\"artistName\"= \"Compose\".\"artistName\" "
                    + "  and \"Song\".\"songID\" = \"Compose\".\"songID\" " : "")
                + (sort.equalsIgnoreCase("genre") ?
                "   and \"Genre\".\"genreID\" = \"SongGNR\".\"genreID\""
                    + "   and \"Song\".\"songID\" = \"SongGNR\".\"songID\"" : " ");

            case "artist" -> "SELECT \"Song\".* FROM \"Song\", \"Compose\", \"Artist\""
                + (sort.equalsIgnoreCase("genre") ? ",  \"Genre\", \"SongGNR\" " : "")
                + " WHERE \"Artist\".\"artistName\" ilike '%" + term + "%' "
                + "   and \"Artist\".\"artistName\"= \"Compose\".\"artistName\""
                + "   and \"Song\".\"songID\" = \"Compose\".\"songID\""
                + (sort.equalsIgnoreCase("genre") ?
                "   and \"Genre\".\"genreID\" = \"SongGNR\".\"genreID\""
                    + "   and \"Song\".\"songID\" = \"SongGNR\".\"songID\"" : " ");

            case "album" -> "SELECT \"Song\".* FROM \"Song\", \"ComposedOf\", \"Album\""
                + (artistReq ? ", \"Artist\", \"Compose\" " : "")
                + (sort.equalsIgnoreCase("genre") ? ", \"Genre\", \"SongGNR\" " : "")
                + " WHERE \"Album\".\"albumName\" ilike '%" + term + "%' "
                + "   and \"Album\".\"albumID\" = \"ComposedOf\".\"albumID\""
                + "   and \"Song\".\"songID\" = \"ComposedOf\".\"songID\" "
                + (artistReq ?
                " and  \"Artist\".\"artistName\"= \"Compose\".\"artistName\" "
                    + "  and \"Song\".\"songID\" = \"Compose\".\"songID\" " : "")
                + (sort.equalsIgnoreCase("genre") ?
                "   and \"Genre\".\"genreID\" = \"SongGNR\".\"genreID\""
                    + "   and \"Song\".\"songID\" = \"SongGNR\".\"songID\"" : " ");
            default -> throw new IllegalStateException(
                "Unexpected value: " + category.toLowerCase());
        }
            + orderBy
            + " LIMIT " + limit;

        ArrayList<Song> songs = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery(query);

        while (rs.next()) {
            songs.add(new Song(
                rs.getString("songID"),
                rs.getInt("length"),
                rs.getString("title"),
                rs.getString("songReleaseDate")
            ));
        }

        rs.close();
        return songs;
    }

    public static List<Playlist> searchPlaylist(String term) throws SQLException {
        ArrayList<Playlist> playlists = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery(
                "SELECT \"Playlist\".* FROM \"Make\", \"Playlist\""
                    + " WHERE \"Playlist\".\"playlistName\" ILIKE '%" + term + "%' AND"
                    + "      \"Make\".\"playlistID\" = \"Playlist\".\"playlistID\" AND"
                    + "      \"Make\".email = '" + Application.getCurrentUser().getEmail() + "'"
                    + " order by \"Playlist\".\"playlistName\" "
                    + " LIMIT " + limit);

        while (rs.next()) {
            playlists.add(new Playlist(
                rs.getString("playlistID"),
                rs.getString("playlistName"),
                rs.getInt("runtime")
            ));
        }

        rs.close();
        return playlists;
    }

    public static User searchUser(String email) throws SQLException {
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery("SELECT * FROM \"User\" WHERE \"User\".EMAIL = '" + email + "'");

        if (!rs.next()) {
            rs.close();
            return null;
        }

        User user = new User(
            rs.getString("email"),
            rs.getString("username"),
            rs.getString("creationDate"),
            rs.getString("lastAccessDate"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getInt("userNumFollowers")
        );

        rs.close();
        return user;
    }

    public static List<Album> searchAlbum(String term) throws SQLException {
        ArrayList<Album> albums = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery(
                "SELECT \"Album\".* FROM \"Album\" WHERE \"albumName\" ILIKE '%" + term + "%' "
                    + " LIMIT " + limit);

        while (rs.next()) {
            albums.add(new Album(
                rs.getString("albumID"),
                rs.getString("albumName"),
                rs.getString("albumReleaseDate")
            ));
        }

        rs.close();
        return albums;
    }


    public static List<Song> searchSongFromPlaylist(Playlist playlist, String term)
        throws SQLException {
        ArrayList<Song> songs = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        String query =
            "SELECT \"Song\".* FROM \"Song\", \"Make\", \"Contains\", \"User\" "
                + " WHERE  \"Make\".\"playlistID\" = \"Contains\".\"playlistID\" AND"
                + "        \"Make\".\"playlistID\" = '" + playlist.getPlaylistID() + "' AND"
                + "      \"User\".email = \"Make\".email AND"
                + "      \"Song\".title ILIKE '%" + term + "%' AND"
                + "      \"Song\".\"songID\" = \"Contains\".\"songID\" AND"
                + "      \"User\".email = '" + Application.getCurrentUser().getEmail() + "'"
                + " LIMIT " + limit;

        ResultSet rs = stmt
            .executeQuery(query);

        while (rs.next()) {
            songs.add(new Song(
                rs.getString("songID"),
                rs.getInt("length"),
                rs.getString("title"),
                rs.getString("songReleaseDate")
            ));
        }
        return songs;
    }
}
