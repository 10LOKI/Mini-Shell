package ma.youcode.lineperm.db;

import java.sql.*;

public class DBConnection
{
    private static DBConnection instance;
    private final Connection connection;

    private DBConnection() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("lineperm.db", "audit.db"));
        try
        {
            initialiser();
        }
        catch (SQLException e)
        {
            connection.close();
            throw e;
        }
    }

    private void initialiser() throws SQLException
    {
        try (Statement stmt = connection.createStatement())
        {
            stmt.execute("pragma foreign_keys = on");
            stmt.execute("pragma busy_timeout = 5000");
        }
        connection.setAutoCommit(false);
        try (Statement stmt = connection.createStatement())
        {
            stmt.execute("create table if not exists users (id integer primary key autoincrement, login text not null unique, password text not null)");
            stmt.execute("create table if not exists files (id integer primary key autoincrement, nom text not null, droits text not null, user_id integer not null references users(id))");
            boolean logsExist = false;
            boolean snapshotExists = false;
            try (ResultSet result = stmt.executeQuery("pragma table_info(logs)"))
            {
                while (result.next())
                {
                    logsExist = true;
                    if (result.getString("name").equals("fichier_nom")) snapshotExists = true;
                }
            }
            if (!logsExist)
            {
                stmt.execute(logsSchema("logs"));
            }
            else if (!snapshotExists)
            {
                // Preserve IDs and history while allowing logs for missing/deleted files.
                stmt.execute(logsSchema("logs_migration"));
                stmt.execute("insert into logs_migration (id, user_id, file_id, action, resultat, quand, fichier_nom) "
                    + "select l.id, l.user_id, f.id, l.action, l.resultat, l.quand, coalesce(f.nom, '[inconnu]') "
                    + "from logs l left join files f on f.id = l.file_id");
                stmt.execute("drop table logs");
                stmt.execute("alter table logs_migration rename to logs");
            }
            stmt.execute("create unique index if not exists files_nom_unique on files(nom)");
            connection.commit();
        }
        catch (SQLException e)
        {
            connection.rollback();
            throw e;
        }
        finally
        {
            connection.setAutoCommit(true);
        }
    }

    private String logsSchema(String table)
    {
        return "create table " + table + " (id integer primary key autoincrement, "
            + "user_id integer not null references users(id), file_id integer references files(id) on delete set null, "
            + "action text not null, resultat text not null, quand text not null, fichier_nom text not null)";
    }

    public static DBConnection getInstance() throws SQLException
    {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getConnection()
    {
        return connection;
    }
}
