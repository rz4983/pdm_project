package Model.Entities;

/**
 * Class representing an Artist entity
 *
 * @author  Raman Zatsarenko | rz4983@rit.edu
 * @author  Austin Couch | alc9026@rit.ed
 * @author  Srikar Sundaram | ss8931@rit.edu
 * @author  Ricky Gupta | rg4825@rit.edu
 *
 * @version 2021.10.24.1
 *
 */
public class Artist {

    /** Non-unique string for the name of this artist entity. */
    private final String name;

    /**
     * Constructor called after needed data is queried from database
     *
     * @param name non-unique String name
     */
    Artist(String name) {
        this.name = name;
    }

    /**
     * @return string representation of this artist entity formatted for ptui
     */
    @Override
    public String toString() {
        return String.format("Artist: %s", this.name);
    }
}
