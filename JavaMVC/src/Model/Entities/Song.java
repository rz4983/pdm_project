package Model.Entities;

/**
 * Class representing a song entity in DB.
 *
 * @author Raman Zatsarenko | rz4983@rit.edu
 * @author Austin Couch | alc9026@rit.edu
 * @author Srikar Sundaram | ss8931@rit.edu
 * @author Ricky Gupta | rg4825@rit.edu
 * @version 2021.10.24.1
 */
public class Song {
    /** The unique alphanumeric string that identifies this song. */
    private final String songID;
    /** The length of the song in ms. */
    private final int length;
    /** The name of the song. */
    private final String title;
    /** The date the song was released in format yyyy-mm-dd (or yyyy if full date is unavailable). */
    private final String songReleaseDate;

    /**
     * Constructor is called after needed data from DB is queried.
     *
     * @param songID unique string identifier
     * @param length length of song in ms
     * @param title name of the song
     * @param songReleaseDate the date in which the song was released
     */
    public Song(String songID, int length, String title, String songReleaseDate) {
        this.songID = songID;
        this.length = 0;
        this.title = title;
        this.songReleaseDate = songReleaseDate;
    }

    //TODO helper methods

    /**
     * TODO
     */
    public void playSong() {
        // mark song as played
    }

    /**
     * @return TODO
     */
    @Override
    public String toString() {
        return "Song{" +
                "songID='" + songID + '\'' +
                ", length=" + length +
                ", title='" + title + '\'' +
                ", songReleaseDate='" + songReleaseDate + '\'' +
                '}';
    }
}
