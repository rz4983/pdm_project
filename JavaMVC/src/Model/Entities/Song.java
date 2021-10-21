package JavaMVC.src.Model.Entities;

public class Song {
    private final String songID;
    private final int length;
    private final String title;
    private final int songReleaseDate;

    public Song(String songID, int length, String title, int songReleaseDate) {
        this.songID = songID;
        this.length = 0;
        this.title = title;
        this.songReleaseDate = songReleaseDate;
    }

    public Song(String songID) {
        // Create all of this by fetching form the database.
        this(songID, 0, null, 0);
    }

    public void playSong() {
        // mark song as played
    }
}
