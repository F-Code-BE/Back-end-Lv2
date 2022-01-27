package patterns;

import java.sql.Connection;
import java.sql.DriverManager;

public class Singleton {
    private static Connection instance;

    private Singleton() {
        String DB_URL = "jdbc:sqlserver://fapdb.database.windows.net:1433;database=fapdb;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        String USER_NAME = "fapdb@fapdb";
        String PASSWORD = "F@pdb1234";
        instance = getConnection(DB_URL, USER_NAME, PASSWORD);
    }

    public static Connection getInstance() {
        if (instance == null) {
            new Singleton();
            return instance;
        } else
            return instance;

    }

    private static Connection getConnection(String dbURL, String userName,
            String password) {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(dbURL, userName, password);
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }
}
