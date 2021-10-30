package Model.Entities;

import Controller.Application;
import Controller.PostgresSSHTest.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
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
    /** Unique email of the user. */
    private final String email;
    /** Unique username of the user. */
    private final String username;
    /** Unix time stamp of when the account was created. */
    private final String creationDate;
    /** Unix time stamp of when the most recent access time was. */
    private String lastAccessDate;
    /** The first name of the user. */
    private final String firstName;
    /** The last name of the user. */
    private final String lastName;
    /** The number of users that follows this user. */
    private long userNumFollowers;

    /**
     * Constructor is called after needed data from DB is queried.
     *
     * @param email email address of the user
     * @param username username of the user
     * @param creationDate the unix time stamp this user was created
     * @param lastAccessDate the unix time stamp this user account was last accessed
     * @param firstName first name of the user
     * @param lastName last name of the user
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

    /**
     * TODO
     *
     * @param lastAccessDate
     */
    public void setLastAccessDate(String lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    /**
     * TODO
     *
     * @param user
     */
    public void addFriend(User user) {
        this.userNumFollowers++;
    }

    /**
     * TODO
     *
     * @param user
     */
    public void removeFriend(User user) {
        
    }

    /**
     * TODO
     *
     * @return
     */
    public List<Playlist> getPlaylists() throws SQLException {
        ArrayList<Playlist> playlists = new ArrayList<>();
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery(
                "SELECT \"Playlist\".* FROM \"Make\", \"Playlist\""
                    + "      \"Make\".\"playlistID\" = \"Playlist\".\"playlistID\" AND"
                    + "      \"Make\".email = '" + email + "''");

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

    /**
     * @return TODO
     */
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", creationDate=" + creationDate +
                ", lastAccessDate=" + lastAccessDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userNumFollowers=" + userNumFollowers +
                '}';
    }
}
