package Controller.PostgresSSHTest;

import com.jcraft.jsch.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private final static int lport = 5432;
    private final static String rhost = "starbug.cs.rit.edu";
    private final static int rport = 5432;
    private final static String user = Credentials.USER.toString();
    private final static String password = Credentials.PASSWORD.toString();
    private final static String databaseName = "p320_09";

    private final static String driverName = "org.postgresql.Driver";
    private static Connection conn = null;
    private static Session session = null;

    public static Connection getConn() {
        return conn;
    }

    public static void openConn() throws SQLException{
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.setConfig(
                    "PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "localhost", rport);
            System.out.println("Port Forwarded");

            // Assigned port could be different from 5432 but rarely happens
            String url = "jdbc:postgresql://localhost:" + assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverName);
            conn = DriverManager.getConnection(url, props);
            System.out.println("Database connection established");

        } catch (Exception ignored) {}
    }

    public static void closeConn() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            System.out.println("Closing Database Connection");
            conn.close();
        }
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }
}
