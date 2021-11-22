package Model.Entities;

import Controller.PostgresSSHTest.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a user entity in DB.
 *
 * @author Raman Zatsarenko | rz4983@rit.edu
 * @author Austin Couch | alc9026@rit.edu
 * @author Srikar Sundaram | ss8931@rit.edu
 * @author Ricky Gupta | rg4825@rit.edu
 * @version 2021.10.24.1
 */
public class User {

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
        }
    }

    /**
     * Unique email of the user.
     */
    private final String email;
    /**
     * Unique username of the user.
     */
    private final String username;
    /**
     * Unix time stamp of when the account was created.
     */
    private final String creationDate;
    /**
     * The first name of the user.
     */
    private final String firstName;
    /**
     * The last name of the user.
     */
    private final String lastName;
    /**
     * Unix time stamp of when the most recent access time was.
     */
    private String lastAccessDate;
    /**
     * The number of users that follows this user.
     */
    private long userNumFollowers;

    /**
     * Constructor is called after needed data from DB is queried.
     *
     * @param email            email address of the user
     * @param username         username of the user
     * @param creationDate     the unix time stamp this user was created
     * @param lastAccessDate   the unix time stamp this user account was last accessed
     * @param firstName        first name of the user
     * @param lastName         last name of the user
     * @param userNumFollowers number of followers this user has
     */
    public User(
        String email,
        String username,
        String creationDate,
        String lastAccessDate,
        String firstName,
        String lastName,
        long userNumFollowers) {
        this.email = email;
        this.username = username;
        this.creationDate = creationDate;
        this.lastAccessDate = lastAccessDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userNumFollowers = userNumFollowers;
    }

    public void addFriend(User user) throws SQLException {
        ResultSet rs = stmt
            .executeQuery("SELECT * FROM \"FollowsUser\" "
                + "WHERE \"followerEmail\" = '" + email + "' "
                + "AND \"followeeEmail\" = '" + user.getEmail() + "'");

        if (rs.next()) {
            System.out.println("You already follow " + user.getEmail());
            return;
        }
        String query = "CALL add_follower('" + this.email + "', '" + user.getEmail() + "');";
        stmt.execute(query);
        System.out.println("You are now following " + user.getEmail());
    }

    public void removeFriend(User user) throws SQLException {
        ResultSet rs = stmt
            .executeQuery("SELECT * FROM \"FollowsUser\" "
                + "WHERE \"followerEmail\" = '" + email + "' "
                + "AND \"followeeEmail\" = '" + user.getEmail() + "'");

        if (!rs.next()) {
            System.out.println("You don't follow " + user.getEmail() + ".");
            return;
        }
        String query = "CALL remove_follower('" + this.email + "', '" + user.getEmail() + "');";
        stmt.execute(query);
        System.out.println("You are no longer following " + user.getEmail());
    }

    public List<Playlist> getPlaylists() throws SQLException {
        ArrayList<Playlist> playlists = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery(
                "SELECT \"Playlist\".* FROM \"Make\", \"Playlist\" WHERE "
                    + "      \"Make\".\"playlistID\" = \"Playlist\".\"playlistID\" AND "
                    + "      \"Make\".email = '" + email + "'"
                    + " order by \"Playlist\".\"playlistName\" ");

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

    public String getEmail() {
        return email;
    }

    public String toString() {
        return "User: " +
                "\n\t-- email: " + email + ' ' +
                "\n\t-- username: " + username + ' ' +
                "\n\t-- name: " + this.getName() + ' ' +
                "\n\t-- followers: " + this.getNumFollowers();
    }

    public String displayProfile() throws SQLException {
        StringBuilder profile =
                new StringBuilder("User: " +
                        "\n\t-- email: " + email + ' ' +
                        "\n\t-- username: " + username + ' ' +
                        "\n\t-- name: " + this.getName() + ' ' +
                        "\n\t-- number of playlists : " + this.getNumPlaylists() + ' ' +
                        "\n\t-- followers: " + this.getNumFollowers() + ' ' +
                        "\n\t-- users followed: " + this.getNumFollowings() + ' ' +
                        "\n\t-- top ten artists: ");

        for (Artist artist : this.getTopArtistsBoth()) // TODO unsure which getTopArtist to use
        {
            profile.append("\n\t").append(artist);
        }

        return profile.toString();
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public List<User> getFollowers() throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet rs = stmt
            .executeQuery("SELECT \"User\".* FROM \"User\", \"FollowsUser\" "
                + "WHERE \"followeeEmail\" = '" + email + "' "
                + "AND \"User\".email = \"followerEmail\"");

        while (rs.next()) {
            users.add(new User(
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("creationDate"),
                rs.getString("lastAccessDate"),
                rs.getString("firstName") + rs.getString("firstName"),
                rs.getInt("userNumFollowers")
            ));
        }
        return users;
    }

    public List<User> getFollowees() throws SQLException {
        List<User> users = new ArrayList<>();
        ResultSet rs = stmt
            .executeQuery("SELECT \"User\".* FROM \"User\", \"FollowsUser\" "
                + "WHERE \"followerEmail\" = '" + email + "' "
                + "AND \"User\".email = \"followeeEmail\"");

        while (rs.next()) {
            users.add(new User(
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("creationDate"),
                rs.getString("lastAccessDate"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getInt("userNumFollowers")
            ));
        }
        return users;
    }

    public int getNumPlaylists() {
        try {
            String query = "select * from num_playlists('" + this.email + "')";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
        }

        return 0;
    }

    public int getNumFollowers() {
        try {
            String query = "select * from num_followers('" + this.email + "')";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
        }

        return 0;
    }

    public int getNumFollowings() {
        try {
            String query = "select * from num_followings('" + this.email + "')";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
        }

        return 0;
    }

    public List<Artist> getTopArtistsPlayed() {
        List<Artist> topArtists = new ArrayList<>();
        try {
            ResultSet rs = stmt
                .executeQuery("select * from top_artists_played('" + this.email + "')");

            while (rs.next()) {
                topArtists.add(new Artist(
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
        }

        return topArtists;
    }

    public List<Artist> getTopArtistCollections() {
        List<Artist> topArtists = new ArrayList<>();
        try {
            ResultSet rs = stmt
                .executeQuery("select * from top_artists_collections('" + this.email + "')");

            while (rs.next()) {
                topArtists.add(new Artist(
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
        }

        return topArtists;
    }

    public List<Artist> getTopArtistsBoth() {
        List<Artist> topArtists = new ArrayList<>();
        try {
        ResultSet rs = stmt.executeQuery("select * from top_artists_both('" + this.email + "')");

            while (rs.next()) {
                topArtists.add(new Artist(
                    rs.getString("artistName")
                ));
            }
        } catch (SQLException e) {
        }

        return topArtists;
    }

    public List<Song> getTopFriendsSongs() {
        List<Song> topFriendsSongs = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("select * from top_friends_songs('" + this.email + "')");

            while (rs.next()) {
                topFriendsSongs.add(new Song(
                        rs.getString("songID"),
                        rs.getInt("length"),
                        rs.getString("title"),
                        rs.getString("songReleaseDate")
                ));
            }
        } catch (SQLException e) {
        }

        return topFriendsSongs;
    }

    public List<Song> getRecommendedSongs() {
        List<Song> recommendedSongs = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("select * from recommend_song_from_genre('" + this.email + "')");

            while (rs.next()) {
                recommendedSongs.add(new Song(
                        rs.getString("songID"),
                        rs.getInt("length"),
                        rs.getString("title"),
                        rs.getString("songReleaseDate")
                ));
            }
        } catch (SQLException e) {
        }

        return recommendedSongs;
    }
}
