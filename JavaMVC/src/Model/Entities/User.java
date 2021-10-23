package Model.Entities;

import java.util.Collection;
import java.util.List;

public class User {
    private final String email;
    private final String username;
    private final long creationDate;
    private long lastAccessDate;
    private final String firstName;
    private final String lastName;
    private long userNumFollowers;

    public User(
            String email,
            String username,
            long creationDate,
            long lastAccessDate,
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

    public void setLastAccessDate(long lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public void addFriend(User user) {
        this.userNumFollowers++;
    }

    public void removeFriend(User user) {
        this.userNumFollowers--;
    }

    public List<Playlist> getPlaylists() {
        // TODO sql query here.
        return null;
    }
}
