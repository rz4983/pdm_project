package Controller;

import Model.Entities.Album;
import Model.Entities.Playlist;
import Model.Entities.Song;
import Model.Entities.User;
import Model.QueryDB.Authentication;
import Model.QueryDB.RelationsManager;
import Model.QueryDB.Search;
import View.ptui;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

    private User currentUser;

    Application() {
        this.currentUser = null;
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.mainLoop();
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
        this.getInput(System.in);
    }

    private void getInput(InputStream in) {
        String text = null;
        Scanner scanner = new Scanner(in);
        ptui.help();
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            text = scanner.nextLine();
            String[] fields = text.split("\\s+(?!\\s)");
            switch (fields[0].toLowerCase()) {
                case "login" -> {
                    String[] info = ptui.login();
                    if (Authentication.validUser(null, info[0])) {
                        this.currentUser = Authentication.login(info[0], info[1]);
                        // TODO check for null.
                    }
                }
                case "logout" -> {
                    this.currentUser = null;
                }
                case "join" -> {
                    String[] info = ptui.join();
                    this.currentUser =
                        Authentication.createUser(info[0], info[1], info[2], info[3], info[4]);
                }
                case "search" -> {
                    switch (fields[1].toLowerCase()) {
                        case "song" -> ptui.searchSongs(Search.searchSongs("term"));
                        case "album" -> ptui.searchAlbums(Search.searchAlbum("", "", "", true));
                    }
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
                    currentUser.addFriend(Search.searchUser(fields[1]));
                }

                case "unfollow" -> {
                    currentUser.removeFriend(Search.searchUser(fields[1]));
                }
                case "rename" -> {
                    RelationsManager.rename(ptui.pickPlaylist(Search.searchPlaylist(fields[1])), fields[2]);
                }
                case "add", "remove" -> {
                    switch (fields[1].toLowerCase()) {
                        case "song" -> {
                            Song song = ptui.pickSong(Search.searchSongs("term"));
                            Playlist playlist = ptui.pickPlaylist(Search.searchPlaylist(fields[2]));

                            if (fields[0].equalsIgnoreCase("add")) {
                                RelationsManager.addSong(song, playlist);
                            } else {
                                RelationsManager.removeSong(song, playlist);
                            }
                        }
                        case "album" -> {
                            Album album =
                                ptui.pickAlbum(
                                    Search.searchAlbum(
                                        "category", "term", "default", false));
                            Playlist playlist = ptui.pickPlaylist(Search.searchPlaylist(fields[2]));

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
                    ptui.list(this.currentUser.getPlaylists());
                }
                case "share" -> {
                    RelationsManager.sharePlaylist(
                        ptui.pickPlaylist(Search.searchPlaylist(fields[1])),
                        Search.searchUser(fields[2]));
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
