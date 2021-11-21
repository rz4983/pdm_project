package View;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.QueryDB.Authentication;
import Model.QueryDB.RelationsManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

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

        System.out.println("login    -- Show the login prompt");
        System.out.println("logout   -- Logout of the application");
        System.out.println("join     -- Join the platform");
        System.out.println("create   -- create playlist-name");
        System.out.println("delete   -- delete playlist-name");
        System.out.println("share    -- share playlist-name another-email");
        System.out.println("play     -- " +
            "\n\t\tplay song song-name\n\t\tplay song song-name playlist-name\n\t\tplay playlist playlist-name\n\t\tplay album album-name");
        System.out.println("follow      -- follow another-email");
        System.out.println("unfollow    -- unfollow another-email");
        System.out.println("rename      -- rename playlist-name new-name");
        System.out.println(
            "add         -- \n\t\tadd song playlist-name song-name\n\t\tadd album playlist-name album-name");
        System.out.println(
            "remove      -- \n\t\tremove song playlist-name song-name\n\t\tremove album playlist-name album-name");
        System.out.println("list        -- \n\t\tlist\n\t\tlist playlist-name");
        System.out.println("search      -- "
            + "\n\t\tsearch term"
            + "\n\t\tsearch term --sort=sortCategory"
            + "\n\t\tsearch term --sort=-sortCategory"
            + "\n\t\tsearch category term"
            + "\n\t\tsearch category term --sort=sortCategory"
            + "\n\t\tsearch category term --sort=-sortCategory"
            + "\n\t\tcategory may be -- [genre, song, artist, album]"
            + "\n\t\tsort category may be -- [year, artist, genre, name]"
            + "\n\t\tsort category may have a minus (-) in front"
            + "\n\t\tto indicate descending order."
        );
        System.out.println("whoifollow  -- Displays who you follow");
        System.out.println("myfollowers -- Displays your followers");
        System.out.println("recommend   -- The recommendation system" +
                                "\n\t\trec[ommend] past-30-days"+
                                "\n\t\trec[ommend] friends"+
                                "\n\t\trec[ommend] genres"+
                                "\n\t\trec[ommend] history"
        );
        System.out.println("myprofile   -- display your profile");
        System.out.println("limit       -- limit number.");
        System.out.println("help        -- Print this message.");
        System.out.println("quit        -- Exit the application.");
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
            Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile(
                    "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            System.out.print("Enter email: ");
            inputs[0] = in.nextLine(); // username
            if (!VALID_EMAIL_ADDRESS_REGEX.matcher(inputs[0]).find()) {
                System.out.println("Use an actual email.");
                continue;
            }
            if (!Authentication.validUser(null, inputs[0])) {
                break;
            }
            System.out.println("Email is used with another account. Try another email.");
        }
        while (true) {

            System.out.print("Enter username: ");
            inputs[1] = in.nextLine(); // username
            if (!Authentication.validUser(inputs[1], null)) {
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
            System.out.println("Passwords do not match. Try again");
        }

        System.out.print("Enter first name: ");
        inputs[3] = in.nextLine();
        System.out.print("Enter last name: ");
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

    public static void play(Playlist playlist) throws SQLException {
        List<Song> songs = playlist.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("Now playing: " + " -- " + (i + 1) + " -- " + songs.get(i));
            RelationsManager.playSong(songs.get(i));
        }
    }

    public static void play(Song song) throws SQLException {
        System.out.println("Now playing: " + " -- " + song);
        RelationsManager.playSong(song);
    }

    public static void play(Album album) throws SQLException {
        List<Song> songs = album.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("Now playing: " + " -- " + (i + 1) + " -- " + songs.get(i));
            RelationsManager.playSong(songs.get(i));

        }
    }
}

