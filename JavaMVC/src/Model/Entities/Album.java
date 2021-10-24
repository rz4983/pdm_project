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

    /** Unique alphanumeric String identifying this album entity. */
    private final String AlbumID;
    /** Non-unique String for the title of this album entity. */
    private final String title;
    /** String in format y-m-d or just y */
    private final String AlbumReleaseDate;

    /**
     * Constructor called after needed data is queried from database
     *
     * @param AlbumID unique String identifier
     * @param title non-unique String title
     * @param AlbumReleaseDate String release date in y-m-d or y
     */
    Album(String AlbumID, String title, String AlbumReleaseDate) {
        this.AlbumID = AlbumID;
        this.title = title;
        this.AlbumReleaseDate = AlbumReleaseDate;
    }

    /**
     * @return string representation of album formatted for ptui
     */
    @Override
    public String toString() {
        //TODO reformat toString()
        return "Album{" +
                "AlbumID='" + AlbumID + '\'' +
                ", title='" + title + '\'' +
                ", AlbumReleaseDate='" + AlbumReleaseDate + '\'' +
                '}';
    }
}
