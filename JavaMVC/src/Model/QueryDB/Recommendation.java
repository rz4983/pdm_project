package Model.QueryDB;


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
 *
 */
public class Recommendation {

    private static Statement stmt;

    static {
        try {
            stmt = Controller.PostgresSSHTest.Database.getConn().createStatement();
        } catch (SQLException e) {
        }
    }

    private String getDate(){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
        return dateFormat.format(date);
    }


    public List<Song> getTopRollingSongs() {
        List<Song> topRollingSongs = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery("Call top_rolling_songs('" + this.getDate() + "')");

            while (rs.next()) {
                topRollingSongs.add(new Song(
                        rs.getString("songID"),
                        rs.getInt("length"),
                        rs.getString("title"),
                        rs.getString("songReleaseDate")
                ));
            }
        } catch (SQLException e) {
        }

        return topRollingSongs;
    }

    public List<Song> getTopGenreSongs() {
        List<Song> topGenreSongs = new ArrayList<>();
        String date = this.getDate().substring(0, 8) + "01";
        try {
            ResultSet rs = stmt.executeQuery("Call top_genres('" + this.getDate().substring(0, 8) + "01" + "')");

            while (rs.next()) {
                topGenreSongs.add(new Song(
                        rs.getString("songID"),
                        rs.getInt("length"),
                        rs.getString("title"),
                        rs.getString("songReleaseDate")
                ));
            }
        } catch (SQLException e) {
        }

        return topGenreSongs;
    }
}
