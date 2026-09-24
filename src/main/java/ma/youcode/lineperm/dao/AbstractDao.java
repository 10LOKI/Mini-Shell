package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.SQLException;
import ma.youcode.lineperm.db.DBConnection;

public abstract class AbstractDao<T> implements Dao<T>{
    protected Connection    getConnection() throws SQLException
    {
        return (DBConnection.getInstance().getConnection());
    }
}