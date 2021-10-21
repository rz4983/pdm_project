package Controller;

import static View.ptui.help;

import Model.Entities.Playlist;
import Model.Entities.User;
import Model.Search;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Application {
    private User currentUser;
    private Collection<Playlist> playlists;

    Application() {
        this.currentUser = null;
        this.playlists = new ArrayList<>();
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.mainLoop();
    }

    private void mainLoop() {
        this.getInput(System.in);
    }

    private void getInput(InputStream in) {
        String text = null;
        Scanner scanner = new Scanner(in);
        help();
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            text = new String(scanner.nextLine());
            String[] fields = text.split("\\s+");
            switch (fields[0].toLowerCase()) {
                case "login" -> {
                    // TODO
                }
                case "logout" -> {
                    // TODO
                }
                case "join" -> {
                    // TODO
                }
                case "search" -> {
                    // TODO
                }

                case "create" -> {
                    // TODO
                }
                case "play" -> {
                    // TODO
                }
                case "follow" -> {
                    // TODO
                }
                case "rename" -> {
                    // TODO
                }
                case "add" -> {
                    // TODO
                }
                case "list" -> {
                    // TODO
                }
                case "share" -> {
                    // TODO
                }
                case "help" -> {
                    help();
                }

                case "quit" -> {
                    return;
                }
            }
            System.out.print("> ");
        }
    }

}
