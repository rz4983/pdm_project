package Model.Entities;

/**
 * Class representing an Album entity
 *
 * @author  Raman Zatsarenko | rz4983@rit.edu
 * @author  Austin Couch | alc9026@rit.ed
 * @author  Srikar Sundaram | ss8931@rit.edu
 * @author  Ricky Gupta | rg4825@rit.edu
 *
 * @version 2021.10.24.1
 *
 */
public class Album {

    /** Unique alphanumeric string identifying this entity. */
    private final String AlbumID;
    /** Non-unique string for the title of this album entity. */
    private final String title;
    /** String in format y-m-d or just y */
    private final String AlbumReleaseDate;

    // fields
    // Constructor
    // toString()
    // accessor methods, getter/setter

    /**
     * Constructor called after needed data is queried from database
     *
     * @param AlbumID unique String identifier
     * @param title non-unique String title of album
     * @param AlbumReleaseDate String release date of album
     */
    Album(String AlbumID, String title, String AlbumReleaseDate) {
        this.AlbumID = AlbumID;
        this.title = title;
        this.AlbumReleaseDate = AlbumReleaseDate;
    }

    // TODO accessor methods

    /**
     * @return string representation of album formatted for ptui
     */
    @Override
    public String toString() {
        return "Album{" +
                "AlbumID='" + AlbumID + '\'' +
                ", title='" + title + '\'' +
                ", AlbumReleaseDate='" + AlbumReleaseDate + '\'' +
                '}';
    }
}
