package JavaMVC.src.Model.Entities;

public class Song {
    private final String songID;
    private final String length;
    private final String title;
    private final String songReleaseDate;

    Song(String songID, String length, String title, String songReleaseDate) {
        this.songID = songID;
        this.length = length;
        this.title = title;
        this.songReleaseDate = songReleaseDate;
    }

    Song(String songID) {
        this.songID = songID;
        this.length = null;
        this.title = null;
        this.songReleaseDate = null;
    }
}
