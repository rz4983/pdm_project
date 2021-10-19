package JavaMVC.src.Model.Entities;

import java.util.Collection;

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
            String creationDate,
            String lastAccessDate,
            String firstName,
            String lastName,
            String userNumFollowers) {
        this.email = email;
        this.username = username;
        this.creationDate = Long.parseLong(creationDate);
        this.lastAccessDate = Long.parseLong(lastAccessDate);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userNumFollowers = Long.parseLong(userNumFollowers);
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

    public Collection<Song> getCollection(String collectionName) {
        return null;
    }
}