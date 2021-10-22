package View;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;

import java.util.Collection;

public class ptui {

    public static void help() {
        System.out.println(
                """
                    _                                                \s
                  ,´ `.                                              \s
            ______|___|______________________________________________
                  |  /                       _..-´|                 \s
                  | /                  _..-´´_..-´|                 \s
            ______|/__________________|_..-´´_____|__________|\\______
                 ,|                   |           |          | \\    \s
                / |                   |           |          | ´    \s
            ___/__|___________________|___________|__________|_______
              / ,´| `.                |      ,d88b|          |      \s
             | .  |   \\            __ |      88888|       __ |      \s
            _|_|__|____|_________,d88b|______`Y88P'_____,d88b|_______
             |  ` |    |         88888|                 88888|      \s
             `.   |   /          `Y88P'                 `Y88P'      \s
            ___`._|_.´_______________________________________________
                  |                                                 \s
                , |                                                 \s
                '.´
            """);

        System.out.println("login  -- Show the login prompt");
        System.out.println("logout -- Logout of the application");
        System.out.println("join   -- Join the platform");
        System.out.println("create -- Create <playlist name>");
        System.out.println("search -- search <category [song]> <term>");
        System.out.println("play   -- play <song name | Song ID>");
        System.out.println("follow -- follow <user email>");
        System.out.println("rename -- rename <playlist ID> <new name>");
        System.out.println(
                "add    -- <\"Song\" | \"Album\"> <Category [Song]> <ID | Title> <--sort=Genre|.."
                        + " [default]>");
        System.out.println(
                "remove -- <\"Song\" | \"Album\"> <Category [Song]> <ID | Title> <--sort=Genre|.."
                        + " [default]>");
        System.out.println("list   -- list all playlist");
        System.out.println("share  -- share <playlist Name | ID> email");
        System.out.println("help   -- Print this message.");
        System.out.println("quit   -- Exit the application.");
    }

    public static Playlist pickPlaylist(Collection<Playlist> searchPlaylist) {
        return null;
    }

    public static void list(Collection<Playlist> playlists) {}

    public static Song pickSong(Collection<Song> songs) {
        return null;
    }

    public static void play(Song song) {}

    public static String[] join() {
        return null;
    }

    public static String[] login() {
        return null;
    }

    public static Album pickAlbum(Collection<Album> albums) {
        return null;
    }

    public static void searchAlbums(Collection<Album> albums) {}

    public static void searchSongs(Collection<Song> songs) {}
}
