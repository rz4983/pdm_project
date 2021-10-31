package Model.Entities;

/**
 * Class representing a Genre entity
 *
 * @author  Raman Zatsarenko | rz4983@rit.edu
 * @author  Austin Couch | alc9026@rit.ed
 * @author  Srikar Sundaram | ss8931@rit.edu
 * @author  Ricky Gupta | rg4825@rit.edu
 *
 * @version 2021.10.24.1
 *
 */
public class Genre {

    /** Unique alphanumeric String identifying this genre entity. */
    private final String genreID;
    /** Non-unique String for the name of this genre entity. */
    private final String genreName;

    /**
     * Constructor called after needed data is queried from database
     *
     * @param genreID unique String identifier
     * @param genreName non-unique String name
     */
    public Genre(String genreID, String genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
    }

    /**
     * @return string representation of genre formatted for ptui
     */
    @Override
    public String toString() {
        return String.format("Genre: %s", this.genreName);
    }
}
