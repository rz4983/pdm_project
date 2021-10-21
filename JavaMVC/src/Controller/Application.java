package Controller;

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
            }
            System.out.print("> ");
        }
    }

    private void help() {
        System.out.println("""
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
        System.out.println("play   -- play<song name | Song ID>");
        System.out.println("follow -- follow <user email>");
        System.out.println("rename -- rename <playlist ID> <new name>");
        System.out.println("add    -- <\"Song\" | \"Album\"> <Category [Song]> <ID | Title> <--sort=Genre|.. [default]>");
        System.out.println("list   -- list all playlist");
        System.out.println("share  -- share <playlist Name | ID> email");
        System.out.println("help   -- Print this message.");
    }
}
