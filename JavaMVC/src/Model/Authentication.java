package Model;

import Model.Entities.User;

import java.security.InvalidParameterException;

import javax.naming.AuthenticationException;

public class Authentication {

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
    public static User createUser(
            String username, String password, String email, String firstName, String lastName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Check if a username or email is valid is valid
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
