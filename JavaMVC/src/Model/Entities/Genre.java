package JavaMVC.src.Model.Entities;

public class Genre {
    private final String GenreID;
    private final String title;

    Genre(String GenreID, String title) {
        this.GenreID = GenreID;
        this.title = title;
    }

    Genre(String GenreID) {
        // Create all of this by fetching form the database.
        this(GenreID, null);
    }
}
