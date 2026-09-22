package ma.youcode.lineperm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private DBConnection() throws SQLException
    {
        String url = "jdbc:sqlite:audit.db";
        this.connection = DriverManager.getConnection(url);
    }

    public static DBConnection getInstance() throws SQLException
    {
        if (instance == null)
        {
            instance = new DBConnection();
        }
        return  (instance);
    }

    public Connection getConnection()
    {
        return (this.connection);
    }
}
// back nekhdem biha 
// Connection conn = DBConnection.getInstance().getConnection();
