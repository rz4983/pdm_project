package Model;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;

import java.util.Collection;

/**
 * FIXME This class manages all relation not between user and some other entity. The logic behind
 * this decision is because we only need to keep in memory anything the user is not interacting with
 * right now. So it manages everything but relations between (current) user and other users, and
 * (current) user and their playlists.
 */
public class RelationsManager {
    public static void addSong(Song songID, Playlist playlist) {}

    public static void removeSong(Song songID, Playlist playlist) {}

    public static void addAlbum(Album album, Playlist playlist) {}

    public static void removeAlbum(Album album, Playlist playlist) {}

    public static void sharePlaylist(Playlist searchPlaylist, User searchUser) {}

    public static void rename(Collection<Playlist> searchPlaylist, String field) {}

    public static void createPlaylist(String playlistName) {}
}
