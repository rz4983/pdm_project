package Controller;

import static Controller.PostgresSSHTest.Database.closeConn;
import static Controller.PostgresSSHTest.Database.getConn;
import static Controller.PostgresSSHTest.Database.openConn;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;
import Model.QueryDB.Authentication;
import Model.QueryDB.RelationsManager;
import Model.QueryDB.Search;
import View.ptui;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

    private static User currentUser;

    Application() {
        currentUser = null;
    }

    public static void main(String[] args) throws SQLException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                closeConn();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }));
        try {
            openConn();

            if (getConn() == null) {
                System.out.println("Fatal: could not establish a connection.");
            } else {
                Application application = new Application();
                application.mainLoop();

            }
            closeConn();
        } catch (SQLException ignored) {
            System.out.println(ignored);
        } finally {
            closeConn();
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private String[] split(String input) {
        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }
        return matchList.toArray(new String[1]);
    }

    private void mainLoop() {
        try {
            this.getInput(System.in);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void getInput(InputStream in) throws SQLException {
        String text = null;
        Scanner scanner = new Scanner(in);
        ptui.help();
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            text = scanner.nextLine();
            String[] fields = split(text);
            if (currentUser == null && Arrays
                .asList("logout", "create", "play", "follow", "unfollow", "rename", "add", "remove",
                    "list", "share", "limit")
                .contains(fields[0].toLowerCase())) {
                System.out.println("Must be logged in to perform this action.");
            } else {
                switch (fields[0].toLowerCase()) {
                    case "login" -> {
                        String[] info = ptui.login();
                        System.out.println(Arrays.toString(info));
                        if (Authentication.validUser(null, info[0])) {
                            currentUser = Authentication.login(info[0], info[1]);
                            if (currentUser == null) {
                                System.out.println("Incorrect password.");
                                break;
                            }
                            System.out.println("Currently logged in as " + info[0]);
                        } else {
                            System.out.println("No user with email " + info[0]);
                        }
                    }
                    case "logout" -> {
                        currentUser = null;
                        System.out.println("Logout successful.");
                        System.out.print("\nClose session? y/[n]: ");
                        String response = scanner.nextLine();
                        if (response != null && response.toLowerCase().startsWith("y")) {
                            System.out.println("Exiting Program");
                            return;
                        }
                    }
                    case "join" -> {
                        String[] info = ptui.join();
                        currentUser =
                            Authentication.createUser(info[0], info[1], info[2], info[3], info[4]);
                        System.out.println("Currently logged in as " + info[1]);
                    }
                    case "search" -> {
                        if (fields.length == 2 || (fields.length == 3 && fields[2]
                            .matches("\\-\\-sort=\\-?\\w+$"))) {
                            // search term [--sort]
                            String category = "song";
                            String term = fields[1];
                            String sort = fields.length == 3 ? fields[2] : "--sort=song";
                            boolean ascending = !Character.toString(sort.charAt(7)).equals("-");

                            sort = sort.substring(7 + (ascending ? 0 : 1));
                            ptui.searchSongs(Search.searchSongs(category, term, sort, ascending));
                            break;
                        }
                        if (fields.length < 3 || fields.length > 4) {
                            System.out.println("Usage:"
                                + "\n\tsearch term"
                                + "\n\tsearch term --sort=sortCategory"
                                + "\n\tsearch term --sort=-sortCategory"
                                + "\n\tsearch category term"
                                + "\n\tsearch category term --sort=sortCategory"
                                + "\n\tsearch category term --sort=-sortCategory"
                                + "\n\ncategory may be -- [genre, song, artist, album]"
                                + "\nsort category may be -- [year, artist, genre, name]"
                                + "\nsort category may have a minus (-) in front"
                                +   "\n\tto indicate descending order."
                            );
                            break;
                        }
                        // search Song  <term> [--sort=name]
                        String category = fields[1];
                        String term = fields[2];
                        String sort = fields.length >= 4 ? fields[3] : "--sort=song";
                        boolean ascending = !Character.toString(sort.charAt(7)).equals("-");

                        if (!Arrays.asList("genre", "song", "artist", "album")
                            .contains(category.toLowerCase())) {
                            System.out.println(
                                "Category must be one of: \"genre\", \"song\", \"artist\", \"album\"");
                            break;
                        }
                        if (!sort.matches("\\-\\-sort=\\-?\\w+$")) {
                            System.out.println("Sorting usage: --sort=song");
                            break;
                        }
                        sort = sort.substring(7 + (ascending ? 0 : 1));
                        ptui.searchSongs(Search.searchSongs(category, term, sort, ascending));
                    }
                    case "delete" -> {
                        if (fields.length != 2) {
                            System.out.println("Usage: delete playlist-name");
                            break;
                        }
                        List<Playlist> searchResultPlaylist = Search
                            .searchPlaylist(fields[1]);
                        if (searchResultPlaylist.size() == 0) {
                            System.out.println("No playlist " + fields[1]);
                            break;
                        }
                        Playlist playlist =
                            searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                : ptui.pickPlaylist(searchResultPlaylist);

                        RelationsManager.deletePlaylist(playlist);
                        System.out.println("Deleted playlist.");
                    }

                    case "create" -> {
                        if (fields.length != 2) {
                            System.out.println("Usage: create playlist-name");
                            break;
                        }
                        RelationsManager.createPlaylist(fields[1]);
                        System.out.println("Create playlist " + fields[1]);
                    }
                    case "play" -> {
                        if (fields.length != 3 && fields.length != 4) {
                            System.out.println(
                                "Usage:\n\tplay song song-name\n\tplay song song-name playlist-name\n\tplay playlist playlist-name"
                            );
                            break;
                        }
                        switch (fields[1].toLowerCase()) {
                            case "song" -> {
                                List<Playlist> searchResultPlaylist = null;
                                Song song = null;
                                Playlist playlist = null;
                                List<Song> searchResultSong = null;
                                if (fields.length == 4) {
                                    searchResultPlaylist = Search.searchPlaylist(fields[2]);
                                    if (searchResultPlaylist.size() == 0) {
                                        System.out.println("No playlist " + fields[2]);
                                        break;
                                    }
                                    playlist =
                                        searchResultPlaylist.size() == 1 ? searchResultPlaylist
                                            .get(0)
                                            : ptui.pickPlaylist(searchResultPlaylist);
                                    searchResultSong = Search
                                        .searchSongFromPlaylist(playlist, fields[3]);
                                }

                                if (fields.length == 3) {
                                    searchResultSong = Search.searchSongs(fields[2]);
                                }
                                if (searchResultSong.size() == 0) {
                                    System.out.println("No song " + fields[2]);
                                    break;
                                }
                                song = searchResultSong.size() == 1 ? searchResultSong.get(0)
                                    : ptui.pickSong(searchResultSong);

                                ptui.play(song);
                            }

                            case "playlist" -> {
                                if (fields.length != 3) {
                                    System.out.println("Usage: play playlist playlist-name");
                                    break;
                                }
                                List<Playlist> searchResultPlaylist = Search
                                    .searchPlaylist(fields[2]);
                                if (searchResultPlaylist.size() == 0) {
                                    System.out.println("No playlist " + fields[2]);
                                    break;
                                }
                                Playlist playlist =
                                    searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                        : ptui.pickPlaylist(searchResultPlaylist);
                                ptui.play(playlist);
                            }
                        }
                    }
                    case "follow" -> {
                        if (fields.length != 2) {
                            System.out.println("Usage: follow another-email");
                            break;
                        }
                        if (fields[1] == currentUser.getEmail()) {
                            System.out.println("Cannot follow yourself.");
                            break;
                        }
                        User friend = Search.searchUser(fields[1]);
                        if (friend == null) {
                            System.out.println("No user with the email " + fields[1]);
                        } else {
                            currentUser.addFriend(friend);
                        }
                    }
                    case "unfollow" -> {
                        if (fields.length != 2) {
                            System.out.println("Usage: unfollow another-email");
                            break;
                        }
                        User following = Search.searchUser(fields[1]);
                        if (following == null) {
                            System.out.println("No user with the email " + fields[1]);
                            break;
                        }
                        currentUser.removeFriend(following);
                        System.out.println("You are now following " + following.getName());
                    }
                    case "rename" -> {
                        if (fields.length != 3) {
                            System.out.println("Usage: rename playlist-name new-name");
                            break;
                        }
                        List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[1]);
                        if (searchResultPlaylist.size() == 0) {
                            System.out.println("No playlist " + fields[1]);
                            break;
                        }
                        Playlist playlist =
                            searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                : ptui.pickPlaylist(searchResultPlaylist);

                        RelationsManager.rename(playlist, fields[2]);
                        System.out.println("Renamed the playlist to " + fields[2]);
                    }
                    case "add", "remove" -> {
                        if (fields.length != 4) {
                            System.out.println(
                                "Usage: \n\t" + fields[0] + " song playlist-name song-name\n\t"
                                    + fields[0] + " album playlist-name album-name");
                            break;
                        }
                        switch (fields[1].toLowerCase()) {
                            case "song" -> {
                                List<Song> searchResultSong = Search.searchSongs(fields[3]);
                                if (searchResultSong.size() == 0) {
                                    System.out.println("No song " + fields[3]);
                                    break;
                                }
                                Song song = searchResultSong.size() == 1 ? searchResultSong.get(0)
                                    : ptui.pickSong(searchResultSong);

                                List<Playlist> searchResultPlaylist = Search
                                    .searchPlaylist(fields[2]);
                                if (searchResultPlaylist.size() == 0) {
                                    System.out.println("No playlist " + fields[2]);
                                    break;
                                }
                                Playlist playlist =
                                    searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                        : ptui.pickPlaylist(searchResultPlaylist);

                                if (fields[0].equalsIgnoreCase("add")) {
                                    RelationsManager.addSong(song, playlist);
                                    System.out.println("added the song to the playlist.");
                                } else {
                                    RelationsManager.removeSong(song, playlist);
                                    System.out.println("removed the song to the playlist.");
                                }
                            }
                            case "album" -> {
                                List<Album> searchResultAlbum = Search.searchAlbum(fields[3]);
                                if (searchResultAlbum.size() == 0) {
                                    System.out.println("No album " + fields[3]);
                                    break;
                                }
                                Album album =
                                    searchResultAlbum.size() == 1 ? searchResultAlbum.get(0)
                                        : ptui.pickAlbum(searchResultAlbum);

                                List<Playlist> searchResultPlaylist = Search
                                    .searchPlaylist(fields[2]);
                                if (searchResultPlaylist.size() == 0) {
                                    System.out.println("No playlist " + fields[2]);
                                    break;
                                }
                                Playlist playlist =
                                    searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                        : ptui.pickPlaylist(searchResultPlaylist);

                                if (fields[0].equalsIgnoreCase("add")) {
                                    RelationsManager.addAlbum(album, playlist);
                                    System.out.println("added the album to the playlist.");
                                } else {
                                    RelationsManager.removeAlbum(album, playlist);
                                    System.out.println("removed the album to the playlist.");
                                }
                            }
                        }
                    }
                    case "list" -> {
                        if (fields.length > 2) {
                            System.out.println("Usage: \n\tlist\n\tlist playlist-name");
                            break;
                        }
                        if (fields.length == 2) {
                            List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[1]);
                            if (searchResultPlaylist.size() == 0) {
                                System.out.println("No playlist " + fields[1]);
                                break;
                            }
                            Playlist playlist =
                                searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                    : ptui.pickPlaylist(searchResultPlaylist);
                            ptui.searchSongs(playlist.getSongs());
                        } else {
                            ptui.list(currentUser.getPlaylists());
                        }
                    }
                    case "share" -> {
                        if (fields.length != 3) {
                            System.out.println("Usage: share playlist-name email");
                            break;
                        }
                        List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[2]);
                        if (searchResultPlaylist.size() == 0) {
                            System.out.println("No playlist " + fields[1]);
                            break;
                        }
                        Playlist playlist =
                            searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                : ptui.pickPlaylist(searchResultPlaylist);

                        User friend = Search.searchUser(fields[2]);
                        if (friend == null) {
                            System.out.println("No user with the email " + fields[1]);
                            break;
                        }
                        RelationsManager.sharePlaylist(playlist, friend);
                        System.out.println("The playlist has been shared with " + fields[2]);
                    }

                    case "limit" -> {
                        if (fields.length != 2 || !fields[1].matches("//d+")) {
                            System.out.println("Usage: limit number");
                            break;
                        }
                        Search.setLimit(Integer.parseInt(fields[1]));
                        System.out.println(
                            "Each search will now display a maximum of " + fields[1] + "results");
                    }
                    case "help" -> {
                        ptui.help();
                    }

                    case "quit" -> {
                        System.out.println("Exitting session.");
                        return;
                    }
                }
            }
            System.out.print("> ");
        }
    }
}
