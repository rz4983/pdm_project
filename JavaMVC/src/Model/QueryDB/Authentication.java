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
        String email, String username, String password, String firstName, String lastName)
        throws SQLException {
        // TODO Auto-generated method stub
        // TODO call valid user check for values before creating query string

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todaysdate = dateFormat.format(date);

        String QUERY =
            "insert into \"User\" values ('" + email + "', '" + username + "', '" + password
                + "', '" + todaysdate + "', '" + todaysdate + "', '" + firstName + "', '"
                + lastName + "', 0)";

        // Try statement grabbing connection variable from database.java
        Connection conn = Controller.PostgresSSHTest.Database.getConn();
        // Create SQL statement
        Statement stmt = conn.createStatement();

        // Create Result statement
        boolean returnsResults = stmt.execute(QUERY);

        return login(email, password);
    }

    /**
     * Check if a username or email is valid
     *
     * @param username username to check against usernames | or "" or null to avoid check.
     * @param email    email to check against emails | or "" or null to avoid check.
     * @return True if an only if email and username are ok.
     */
    public static boolean validUser(String username, String email) throws SQLException {
        boolean result = true;
        if (email != null && !email.equals("")) {
            Statement stmt = Database.getConn().createStatement();
            ResultSet rs = stmt
                .executeQuery("SELECT * FROM \"User\" WHERE \"User\".EMAIL = '" + email + "'");

            result = rs.next();
            rs.close();
        }

        if (result && username != null && !username.equals("")) {
            Statement stmt = Database.getConn().createStatement();
            ResultSet rs = stmt
                .executeQuery(
                    "SELECT * FROM \"User\" WHERE \"User\".USERNAME = '" + username + "'");

            result = rs.next();
            rs.close();
        }
        return result;
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
            rs.close();
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todaysdate = dateFormat.format(date);

        Statement dateUpdater = Database.getConn().createStatement();
        String up = rs.getString("password");


        if (!up.equals(password)) {
            return null;
        }

        dateUpdater.executeUpdate(
            "UPDATE \"User\" SET \"lastAccessDate\" = '" + todaysdate + "' WHERE email = '"
                + email + "'");

        User user = new User(
            rs.getString("email"),
            rs.getString("username"),
            rs.getString("creationDate"),
            rs.getString("lastAccessDate"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getInt("userNumFollowers")
        );
        rs.close();

        return user;
    }
}
