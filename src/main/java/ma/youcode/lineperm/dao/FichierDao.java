package ma.youcode.lineperm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.User;

public class FichierDao extends AbstractDao<Fichier>
{
    @Override
    public void save(Fichier fichier)
    {
        User proprietaire = new UserDao().findByLogin(fichier.getProprietaire());
        if (proprietaire == null)
        {
            throw new IllegalArgumentException("Proprietaire introuvable : " + fichier.getProprietaire());
        }
        String sqlQuery = "insert into files (nom, droits, user_id) values (?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setString(1, fichier.getNom());
            stmt.setString(2, formatDroits(fichier));
            stmt.setInt(3, proprietaire.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in save: " + e.getMessage(), e);
        }
    }

    public List<Fichier> findAll()
    {
        List<Fichier> fichiers = new ArrayList<>();
        String sqlQuery = "select f.*, u.login from files f join users u on u.id = f.user_id order by f.id";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery);
             ResultSet result = stmt.executeQuery())
        {
            while (result.next()) fichiers.add(mapRow(result));
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findAll: " + e.getMessage(), e);
        }
        return fichiers;
    }

    public Fichier findByNom(String nom)
    {
        String sqlQuery = "select f.*, u.login from files f join users u on u.id = f.user_id where f.nom = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setString(1, nom);
            try (ResultSet result = stmt.executeQuery())
            {
                return result.next() ? mapRow(result) : null;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findByNom: " + e.getMessage(), e);
        }
    }
    private String formatDroits(Fichier fichier)
    {
        return (fichier.isReadProp() ? "r" : "-")
            + (fichier.isWriteProp() ? "w" : "-")
            + (fichier.isDeleteProp() ? "d" : "-") + "|"
            + (fichier.isReadOther() ? "r" : "-")
            + (fichier.isWriteOther() ? "w" : "-")
            + (fichier.isDeleteOther() ? "d" : "-");
    }

    private Fichier mapRow(ResultSet result) throws SQLException
    {
        String droits = result.getString("droits");
        if (droits == null || !droits.matches("[r-][w-][d-]\\|[r-][w-][d-]"))
        {
            throw new SQLException("Droits invalides pour le fichier " + result.getInt("id"));
        }
        return new Fichier(result.getInt("id"), result.getString("nom"), result.getString("login"),
            droits.charAt(0) == 'r', droits.charAt(1) == 'w', droits.charAt(2) == 'd',
            droits.charAt(4) == 'r', droits.charAt(5) == 'w', droits.charAt(6) == 'd');
    }

    @Override
    public Fichier findById(int id)
    {
        String sqlQuery = "select f.*, u.login from files f join users u on f.user_id = u.id where f.id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, id);
            try (ResultSet result = stmt.executeQuery())
            {
                if (result.next())
                {
                    return mapRow(result);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findById: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Fichier> findByProprietaire(int userId)
    {
        String sqlQuery = "select f.*, u.login from files f join users u on f.user_id = u.id where f.user_id = ?";
        List<Fichier> fichiers = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, userId);
            try (ResultSet result = stmt.executeQuery())
            {
                while (result.next())
                {
                    fichiers.add(mapRow(result));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findByProprietaire: " + e.getMessage(), e);
        }
        return fichiers;
    }

    public void updateDroits(int id, Fichier fichier)
    {
        String sqlQuery = "update files set droits = ? where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setString(1, formatDroits(fichier));
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in updateDroits: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id)
    {
        String sqlQuery = "delete from files where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in delete for ID " + id + ": " + e.getMessage(), e);
        }
    }
}
