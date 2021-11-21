package Model.QueryDB;


import Model.Entities.Genre;
import Model.Entities.Song;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * other 5 commands will be here as they do not modify the database, they are only display selects
 */
public class Recommendation {

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
        }
    }

    private static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        date.setMonth((date.getMonth() - 1) % 12);
        // return "2000-01-01";
        // for testing because we dont have any plays that recent for most users.
        return dateFormat.format(date);
    }


    public static List<Song> getTopRollingSongs() {
        List<Song> topRollingSongs = new ArrayList<>();
        try {
            ResultSet rs = stmt
                .executeQuery("select * from top_rolling_songs('" + getDate() + "')");

            while (rs.next()) {
                topRollingSongs.add(new Song(
                    rs.getString("songID"),
                    rs.getInt("length"),
                    rs.getString("title"),
                    rs.getString("songReleaseDate")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return topRollingSongs;
    }

    public static List<Genre> getTopGenreSongs() {
        List<Genre> topGenreSongs = new ArrayList<>();
        String date = getDate().substring(0, 8) + "01";
        try {
            ResultSet rs = stmt.executeQuery(
                "select * from top_genres('" + getDate().substring(0, 8) + "01" + "')");

            while (rs.next()) {
                topGenreSongs.add(new Genre(
                    rs.getString("genreID"),
                    rs.getString("genreName")
                ));
            }
        } catch (SQLException e) {
        }

        return topGenreSongs;
    }
}
