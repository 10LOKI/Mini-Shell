package ma.youcode.lineperm.db;

import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() throws SQLException
    {

    }

    public static DBConnection getInstance() throws SQLException
    {

    }

    public Connection getConnection()
    {
        
    }
}
