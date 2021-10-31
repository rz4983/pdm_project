package Controller;

import static Controller.PostgresSSHTest.Database.closeConn;
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
        try {
            openConn();
            Application application = new Application();
            application.mainLoop();
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
            switch (fields[0].toLowerCase()) {
                case "login" -> {
                    String[] info = ptui.login();
                    System.out.println(Arrays.toString(info));
                    if (Authentication.validUser(null, info[0])) {
                        currentUser = Authentication.login(info[0], info[1]);
                        if (currentUser == null) {
                            System.out.println("Incorrect password.");
                            continue;
                        }
                        System.out.println("Currently logged in as " + info[0]);
                    } else {
                        System.out.println("No user with email " + info[0]);
                    }
                }
                case "logout" -> {
                    currentUser = null;
                }
                case "join" -> {
                    String[] info = ptui.join();
                    currentUser =
                        Authentication.createUser(info[0], info[1], info[2], info[3], info[4]);
                    System.out.println("Currently logged in as " + info[1]);
                }
                case "search" -> {
                    // search Song  <term> [--sort=name]
                    String category = fields[1];
                    String term = fields[2];
                    String sort = fields.length >= 4 ? fields[3] : "--sort=song";
                    boolean ascending = Character.toString(sort.charAt(7)).equals("-");

                    if (!Arrays.asList("genre", "song", "artist", "album")
                        .contains(category.toLowerCase())) {
                        System.out.println(
                            "Category must be one of: \"genre\", \"song\", \"artist\", \"album\"");
                        continue;
                    }
                    if (!sort.matches("\\-\\-sort=\\w+$")) {
                        System.out.println("Sorting usage: --sort=song");
                        continue;
                    }
                    sort = sort.substring(7 + (ascending ?  0 : 1));
                    ptui.searchSongs(Search.searchSongs(category, term, sort, ascending));
                }

                case "create" -> {
                    RelationsManager.createPlaylist(fields[1]);
                }
                case "play" -> {
                    List<Playlist> playlist = Search.searchPlaylist(fields[1]);
                    if (playlist != null) {
                        ptui.play(ptui.pickPlaylist(playlist));
                    } else {
                        ptui.play(ptui.pickSong(Search.searchSongs(fields[1])));
                    }
                }
                case "follow" -> {
                    User friend = Search.searchUser(fields[1]);
                    if (friend == null) {
                        System.out.println("No user with the email " + fields[1]);
                    } else {
                        currentUser.addFriend(friend);
                    }
                }

                case "unfollow" -> {
                    User friend = Search.searchUser(fields[1]);
                    if (friend == null) {
                        System.out.println("No user with the email " + fields[1]);
                    }
                    currentUser.removeFriend(friend);
                }
                case "rename" -> {
                    List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[1]);
                    if (searchResultPlaylist.size() == 0) {
                        System.out.println("No playlist " + fields[1]);
                        continue;
                    }
                    Playlist playlist =
                        searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                            : ptui.pickPlaylist(searchResultPlaylist);

                    RelationsManager.rename(playlist, fields[2]);
                }
                case "add", "remove" -> {
                    switch (fields[1].toLowerCase()) {
                        case "song" -> {
                            List<Song> searchResultSong = Search.searchSongs(fields[2]);
                            if (searchResultSong.size() == 0) {
                                System.out.println("No song " + fields[2]);
                                continue;
                            }
                            Song song = searchResultSong.size() == 1 ? searchResultSong.get(0)
                                : ptui.pickSong(searchResultSong);

                            List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[2]);
                            if (searchResultPlaylist.size() == 0) {
                                System.out.println("No playlist " + fields[2]);
                                continue;
                            }
                            Playlist playlist =
                                searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                    : ptui.pickPlaylist(searchResultPlaylist);

                            if (fields[0].equalsIgnoreCase("add")) {
                                RelationsManager.addSong(song, playlist);
                            } else {
                                RelationsManager.removeSong(song, playlist);
                            }
                        }
                        case "album" -> {
                            List<Album> searchResultAlbum = Search.searchAlbum(fields[2]);
                            if (searchResultAlbum.size() == 0) {
                                System.out.println("No album " + fields[2]);
                                continue;
                            }
                            Album album = searchResultAlbum.size() == 1 ? searchResultAlbum.get(0)
                                : ptui.pickAlbum(searchResultAlbum);

                            List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[2]);
                            if (searchResultPlaylist.size() == 0) {
                                System.out.println("No playlist " + fields[2]);
                                continue;
                            }
                            Playlist playlist =
                                searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                                    : ptui.pickPlaylist(searchResultPlaylist);

                            if (fields[0].equalsIgnoreCase("add")) {
                                RelationsManager.addAlbum(album, playlist);
                            } else {
                                RelationsManager.removeAlbum(album, playlist);
                            }
                        }
                    }
                    ;
                }
                case "list" -> {
                    ptui.list(currentUser.getPlaylists());
                }
                case "share" -> {
                    List<Playlist> searchResultPlaylist = Search.searchPlaylist(fields[2]);
                    if (searchResultPlaylist.size() == 0) {
                        System.out.println("No playlist " + fields[1]);
                        continue;
                    }
                    Playlist playlist =
                        searchResultPlaylist.size() == 1 ? searchResultPlaylist.get(0)
                            : ptui.pickPlaylist(searchResultPlaylist);

                    User friend = Search.searchUser(fields[2]);
                    if (friend == null) {
                        System.out.println("No user with the email " + fields[1]);
                        continue;
                    }
                    RelationsManager.sharePlaylist(playlist, friend);
                }
                case "help" -> {
                    ptui.help();
                }

                case "quit" -> {
                    return;
                }
            }
            System.out.print("> ");
        }
    }
}
