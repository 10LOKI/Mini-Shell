package ma.youcode.lineperm.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import ma.youcode.lineperm.model.AccesLog;

public class LogDao extends AbstractDao<AccesLog>
{
    @Override
    public void save(AccesLog log)
    {
        String sqlQuery = "insert into logs (user_id, file_id, action, resultat, quand) "
            + "select u.id, f.id, ?, ?, ? from users u cross join files f where u.id = ? and f.id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setString(1, log.getAction());
            stmt.setString(2, log.getResultat());
            stmt.setString(3, log.getDateHeure().toString());
            stmt.setInt(4, log.getUserId());
            stmt.setInt(5, log.getFichierId());
            if (stmt.executeUpdate() == 0)
            {
                throw new IllegalArgumentException("Utilisateur ou fichier inexistant pour ce log");
            }
        }
        catch (SQLException e)
        {
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed"))
            {
                throw new IllegalArgumentException("Utilisateur ou fichier inexistant pour ce log", e);
            }
            throw new RuntimeException("Database error in save: " + e.getMessage(), e);
        }
    }

    @Override
    public AccesLog findById(int id)
    {
        String sqlQuery = "select l.*, u.login, f.nom from logs l "
            + "left join users u on u.id = l.user_id left join files f on f.id = l.file_id where l.id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, id);
            try (ResultSet result = stmt.executeQuery())
            {
                if (result.next())
                {
                    return new AccesLog(result.getInt("id"), result.getInt("user_id"), result.getInt("file_id"),
                        result.getString("login"), result.getString("nom"), result.getString("action"),
                        result.getString("resultat"), LocalDateTime.parse(result.getString("quand").replace(' ', 'T')));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findById: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(int id)
    {
        throw new UnsupportedOperationException("Un log est immuable : suppression interdite.");
    }

    public int compterTotal()
    {
        return scalar("select count(*) from logs");
    }

    public int compterRefuses()
    {
        return scalar("select count(*) from logs where resultat = 'REFUSE'");
    }

    public int userDistincts()
    {
        return scalar("select count(distinct user_id) from logs");
    }

    public Map<String, Integer> actionsByUser()
    {
        return groupBy("select u.login, count(*) from logs l join users u on u.id = l.user_id "
            + "group by u.id, u.login order by u.login");
    }

    public Map<String, Integer> topFichiers(int limite)
    {
        if (limite < 0)
        {
            throw new IllegalArgumentException("La limite doit etre positive ou nulle");
        }
        String sqlQuery = "select f.nom, count(*) as nb from logs l join files f on f.id = l.file_id "
            + "group by f.nom order by nb desc, f.nom limit ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, limite);
            return readGroups(stmt);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in topFichiers: " + e.getMessage(), e);
        }
    }

    public int refusesByUser(String username)
    {
        String sqlQuery = "select count(*) from logs l join users u on u.id = l.user_id "
            + "where u.login = ? and l.resultat = 'REFUSE'";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setString(1, username);
            try (ResultSet result = stmt.executeQuery())
            {
                return result.next() ? result.getInt(1) : 0;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in refusesByUser: " + e.getMessage(), e);
        }
    }

    private int scalar(String sqlQuery)
    {
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery);
             ResultSet result = stmt.executeQuery())
        {
            return result.next() ? result.getInt(1) : 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in scalar: " + e.getMessage(), e);
        }
    }

    private Map<String, Integer> groupBy(String sqlQuery)
    {
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            return readGroups(stmt);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in groupBy: " + e.getMessage(), e);
        }
    }

    private Map<String, Integer> readGroups(PreparedStatement stmt) throws SQLException
    {
        Map<String, Integer> groups = new LinkedHashMap<>();
        try (ResultSet result = stmt.executeQuery())
        {
            while (result.next())
            {
                groups.put(result.getString(1), result.getInt(2));
            }
        }
        return groups;
    }
}
