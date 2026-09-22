package ma.youcode.lineperm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private final Connection connection;
    private static final String PROTOCOLE = "jdbc";
    private static final String DRIVER = "sqlite";
    private static final String DB = "audit.db";

    private DBConnection() throws SQLException
    {
        String url = PROTOCOLE + ":" + DRIVER+ ":" + DB;
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
