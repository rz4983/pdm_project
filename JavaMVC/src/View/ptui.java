package View;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.QueryDB.Authentication;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ptui {

    private static final Scanner in = new Scanner(System.in);

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

    public static Playlist pickPlaylist(List<Playlist> playlists) {
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println((i + 1) + " -- " + playlists.get(i));
        }

        while (true) {
            System.out.print("Choose the index: ");
            String chosen = in.nextLine();
            try {
                int i = Integer.parseInt(chosen);
                if (i > 0 && i <= playlists.size()) {
                    return playlists.get(i - 1);
                }
                System.out.println("Please enter a number between 1 and " + playlists.size());
            } catch (NumberFormatException er) {
                System.out.println("Please enter the index.");
            }
        }
    }

    public static void list(List<Playlist> playlists) {
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println((i + 1) + " -- " + playlists.get(i));
        }
    }

    public static Song pickSong(List<Song> songs) {
        for (int i = 0; i < songs.size(); i++) {
            System.out.println((i + 1) + " -- " + songs.get(i));
        }

        while (true) {
            System.out.print("Choose the index: ");
            String chosen = in.nextLine();
            try {
                int i = Integer.parseInt(chosen);
                if (i > 0 && i <= songs.size()) {
                    return songs.get(i - 1);
                }
                System.out.println("Please enter a number between 1 and " + songs.size());
            } catch (NumberFormatException er) {
                System.out.println("Please enter the index.");
            }
        }
    }

    public static void play(Song song) {}

    /**
     * Get user input for all required fields for creating a user. Asks user for, in this order: 1.
     * Username -- Must not already exist 2. Email -- Must not already exist 3. Password -- read
     * securely and confirmed twice. 4. First name & Last name.
     *
     * @return A list of string of inputs.
     */
    public static String[] join() throws SQLException {
        String[] inputs = new String[5];

        while (true) {
            System.out.print("Enter username: ");
            inputs[0] = in.nextLine(); // username
            if (!Authentication.validUser(inputs[0], null)) {
                break;
            }
            System.out.println("Username is taken. Try another username.");
        }

        while (true) {
            System.out.print("Enter email: ");
            inputs[0] = in.nextLine(); // username
            if (!Authentication.validUser(null, inputs[1])) {
                break;
            }
            System.out.println("Email is used with another account. Try another email.");
        }

        while (true) {
            System.out.print("Enter username: ");
            inputs[1] = in.nextLine(); // username
            if (!Authentication.validUser(inputs[0], null)) {
                break;
            }
            System.out.println("Username is taken. Try another username.");
        }

        while (true) {
            System.out.print("Enter password: ");
            inputs[2] =
                    System.console() != null
                            ? new String(System.console().readPassword())
                            : in.nextLine();
            System.out.print("Confirm password: ");
            String confirmed =
                    System.console() != null
                            ? new String(System.console().readPassword())
                            : in.nextLine();
            if (confirmed.equals(inputs[2])) {
                break;
            }
            System.out.println("Username is taken. Try another username.");
        }

        System.out.print("Enter first name: ");
        inputs[3] = in.nextLine();
        System.out.print("Enter first name: ");
        inputs[4] = in.nextLine();

        return inputs;
    }

    /**
     * Ask user for email and password.
     *
     * @return String[2] with email and password.
     */
    public static String[] login() {
        String[] inputs = new String[2];

        System.out.print("Enter email: ");
        inputs[0] = in.nextLine();

        System.out.print("Enter password: ");
        inputs[1] =
                System.console() != null
                        ? new String(System.console().readPassword())
                        : in.nextLine();

        return inputs;
    }

    /**
     * Prints a list of albums using {@link Album#toString}
     *
     * @param albums List of albums
     * @return The album that was picked.
     */
    public static Album pickAlbum(List<Album> albums) {
        for (int i = 0; i < albums.size(); i++) {
            System.out.println((i + 1) + " -- " + albums.get(i));
        }

        while (true) {
            System.out.print("Choose the index: ");
            String chosen = in.nextLine();
            try {
                int i = Integer.parseInt(chosen);
                if (i > 0 && i <= albums.size()) {
                    return albums.get(i - 1);
                }
                System.out.println("Please enter a number between 1 and " + albums.size());
            } catch (NumberFormatException er) {
                System.out.println("Please enter the index.");
            }
        }
    }

    public static void searchAlbums(List<Album> albums) {
        for (int i = 0; i < albums.size(); i++) {
            System.out.println((i + 1) + " -- " + albums.get(i));
        }
    }

    public static void searchSongs(List<Song> songs) {
        for (int i = 0; i < songs.size(); i++) {
            System.out.println((i + 1) + " -- " + songs.get(i));
        }
    }

    public static void play(Playlist playlist) {
        List<Song> songs = playlist.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("Now playing: " + " -- " + (i + 1) + " -- " + songs.get(i));
        }
    }
}
