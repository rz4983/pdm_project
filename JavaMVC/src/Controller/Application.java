package JavaMVC.src.Controller;

import Model.Entities.Playlist;
import Model.Entities.User;
import Model.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        String newLine = System.getProperty("line.separator");
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

        System.out.println("login -- Show the login prompt");
        System.out.println("logout -- Logout of the application");
        System.out.println("join -- Join the playform");
        System.out.println("search -- ");
        System.out.println("play -- ");
        System.out.println("follow -- ");
        System.out.println("rename -- ");
        System.out.println("add -- ");
        System.out.println("list -- ");
        System.out.println("share -- ");
        System.out.println("help -- ");
    }
}
