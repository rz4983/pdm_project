package Model.QueryDB;

import Controller.PostgresSSHTest.Database;
import Model.Entities.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Authentication {

    private static SQLException SQLException;

    /**
     * Create a user and adds to the database.
     *
     * @param username  Username to be created with
     * @param password  password the user can log with again.
     * @param email     User's email
     * @param firstName User's first name
     * @param lastName  User's last name
     * @return User instance.
     */

    //private static Connection conn;
    public static User createUser(
        String email, String username, String password, String firstName, String lastName) {
        // TODO Auto-generated method stub
        // TODO call valid user check for values before creating query string

        String QUERY = "INSERT" + " INTO" + " USER" + " VALUES" +
            " ('" + email + "', '" + username + "', '" + password + "', '" + firstName + "', "
            + lastName + ");";

        // Try statement grabbing connection variable from database.java
        try (Connection conn = Controller.PostgresSSHTest.Database.getConn()) {
            // Create SQL statement
            Statement stmt = conn.createStatement();

            // Create Result statement
            boolean returnsResults = stmt.execute(QUERY);

            // Throws sql exception if insert returns a value as it should not
            if (returnsResults) {
                throw SQLException;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Check if a username or email is valid
     *
     * @param username username to check against usernames | or "" or null to avoid check.
     * @param email    email to check against emails | or "" or null to avoid check.
     * @return True if an only if email and username are ok.
     */
    public static boolean validUser(String username, String email) throws SQLException {
        if (email != null && !email.equals("")) {
            Statement stmt = Database.getConn().createStatement();
            ResultSet rs = stmt
                .executeQuery("SELECT * FROM \"User\" WHERE \"User\".EMAIL = '" + email + "'");

            if (!rs.next()) {
                return false;
            }
        }

        if (username != null && !username.equals("")) {
            Statement stmt = Database.getConn().createStatement();
            ResultSet rs = stmt
                .executeQuery("SELECT * FROM \"User\" WHERE \"User\".USERNAME = '" + username + "'");

            return rs.next();
        }
        return true;
    }

    /**
     * @param email    email to log user as
     * @param password password of the user.
     * @return User instance
     */
    public static User login(String email, String password) throws SQLException {
        Statement stmt = Database.getConn().createStatement();
        ResultSet rs = stmt
            .executeQuery("SELECT * FROM \"User\" WHERE \"User\".EMAIL = '" + email + "'");

        if (!rs.next()) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todaysdate = dateFormat.format(date);
        System.out.println("Today's date : " + todaysdate);

        return new User(
            rs.getString("email"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("creationDate"),
            rs.getString("lastAccessDate"),
            rs.getString("firstName"),
            rs.getInt("userNumFollowers")
        );
    }
}
