package Model.Entities;

public class Album {
    private final String AlbumID;
    private final String title;
    private final String AlbumReleaseDate;

    Album(String AlbumID, String title, String AlbumReleaseDate) {
        this.AlbumID = AlbumID;
        this.title = title;
        this.AlbumReleaseDate = AlbumReleaseDate;
    }

    Album(String AlbumID) {
        // Create all of this by fetching form the database.
        this(AlbumID, null, null);
    }
}
