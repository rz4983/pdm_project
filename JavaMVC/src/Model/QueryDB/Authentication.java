package Model.QueryDB;

import Model.Entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication {

    private static SQLException SQLException;

    /**
     * Create a user and adds to the database.
     *
     * @param username Username to be created with
     * @param password password the user can log with again.
     * @param email User's email
     * @param firstName User's first name
     * @param lastName User's last name
     * @return User instance.
     */

    //private static Connection conn;

    public static User createUser(
        String email, String username, String password, String firstName, String lastName) {
        // TODO Auto-generated method stub
        // TODO call valid user check for values before creating query string

        String QUERY = "INSERT" + " INTO" + " USER" + " VALUES" +
                " ('" + email + "', '" + username + "', '" + password + "', '" + firstName + "', " + lastName + ");";

        // Try statement grabbing connection variable from database.java
        try (Connection conn = Controller.PostgresSSHTest.Database.getConn()){
            // Create SQL statement
            Statement stmt = conn.createStatement();

            // Create Result statement
            boolean returnsResults = stmt.execute(QUERY);

            // Throws sql exception if insert returns a value as it should not
            if (returnsResults){
                throw SQLException;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * Check if a username or email is valid
     *
     * @param username username to check against usernames | or "" or null to avoid check.
     * @param email email to check against emails | or "" or null to avoid check.
     * @return True if an only if email and username are ok.
     */
    public static boolean validUser(String username, String email) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param email email to log user as
     * @param password password of the user.
     * @return User instance
     */
    public static User login(String email, String password) {
        // TODO Auto-generated method stub
        return null;
    }
}
