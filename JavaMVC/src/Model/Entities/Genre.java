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

    //TODO finish documentation
    /** Unique alphanumeric string identifying this genre entity. */
    private final String GenreID;
    /** Non-unique string for the name of this genre entity. */
    private final String genreName;

    /**
     * Constructor called after needed data is queried from database
     *
     * @param genreID unique String identifier
     * @param genreName non-unique String for the name of this artist
     */
    public Genre(String genreID, String genreName) {
        GenreID = genreID;
        this.genreName = genreName;
    }

    // TODO accessor methods

    /**
     * @return string representation of genre formatted for ptui
     */
    @Override
    public String toString() {
        return "Genre{" +
                "GenreID='" + GenreID + '\'' +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}
