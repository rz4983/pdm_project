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
            e.printStackTrace();
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
        String query = "CALL add_follower('" + this.email + "', '" + user.getEmail() + "');";
        stmt.execute(query);
        this.userNumFollowers++;
    }

    public void removeFriend(User user) throws SQLException {
        String query = "CALL remove_follower('" + this.email + "', '" + user.getEmail() + "');";
        stmt.execute(query);
        this.userNumFollowers--;
    }

    public List<Playlist> getPlaylists() throws SQLException {
        ArrayList<Playlist> playlists = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery(
                "SELECT \"Playlist\".* FROM \"Make\", \"Playlist\" WHERE "
                    + "      \"Make\".\"playlistID\" = \"Playlist\".\"playlistID\" AND "
                    + "      \"Make\".email = '" + email + "'");

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

    // TODO
    public String toString() {
        return "User: " +
            "email: " + email + ' ' +
            "-- username: " + username + ' ' +
            "-- name: " + firstName + ' '
             + lastName + ' ' +
            "-- followers: " + userNumFollowers;
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
                rs.getString("firstName" ) + rs.getString("firstName" ),
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
}
