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
    private final String GenreID;

    private final String genreName;


    public Genre(String genreID, String genreName) {
        GenreID = genreID;
        this.genreName = genreName;
    }
}
