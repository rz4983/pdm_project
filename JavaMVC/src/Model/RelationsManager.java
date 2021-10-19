package JavaMVC.src.Model;


/**
 * FIXME
 * This class manages all relation not between user and some other entity.
 * The logic behind this decision is because we only need to keep in memory
 * anything the user is not interacting with right now.
 * So it manages everything but relations between (current) user and
 * other users, and (current) user and their playlists.
 */
public class RelationsManager {
    public static void addSong(String songID, String collectionID) {}

    public static void removeSong(String songID, String collectionID) {}

    public static void addAlbum(String AlbumID, String collectionID) {}

    public static void removeAlbum(String AlbumID, String collectionID) {}
}
