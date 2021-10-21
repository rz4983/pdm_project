package JavaMVC.src.Controller;

import JavaMVC.src.Model.Entities.Playlist;
import JavaMVC.src.Model.Entities.User;
import JavaMVC.src.Model.Search;

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
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            text = new String(scanner.nextLine());
            System.out.println(text);
            System.out.print("> ");
        }
    }
}
