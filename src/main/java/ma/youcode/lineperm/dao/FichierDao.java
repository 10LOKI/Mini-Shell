package ma.youcode.lineperm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.Droits;

public class FichierDao extends AbstractDao<Fichier>
{
    @Override
    public void save(Fichier fichier)
    {
        String sqlQuery = "insert into fichiers (nom, proprietaire_id, read_prop, write_prop, delete_prop, read_other, write_other, delete_other) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            Droits d = fichier.getDroits();
            stmt.setString(1, fichier.getNom());
            stmt.setInt(2, fichier.getProprietaireId());
            stmt.setBoolean(3, d.isReadProp());
            stmt.setBoolean(4, d.isWriteProp());
            stmt.setBoolean(5, d.isDeleteProp());
            stmt.setBoolean(6, d.isReadOther());
            stmt.setBoolean(7, d.isWriteOther());
            stmt.setBoolean(8, d.isDeleteOther());
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in save: " + e.getMessage(), e);
        }
    }

    private Fichier mapRow(ResultSet result) throws SQLException
    {
        Droits d = new Droits(
            result.getBoolean("read_prop"),
            result.getBoolean("write_prop"),
            result.getBoolean("delete_prop"),
            result.getBoolean("read_other"),
            result.getBoolean("write_other"),
            result.getBoolean("delete_other")
        );
        return new Fichier(result.getInt("id"), result.getString("nom"), result.getInt("proprietaire_id"), d);
    }

    @Override
    public Fichier findById(int id)
    {
        String sqlQuery = "select * from fichiers where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, id);
            try (ResultSet result = stmt.executeQuery())
            {
                if (result.next())
                    return mapRow(result);
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
        String sqlQuery = "select * from fichiers where proprietaire_id = ?";
        List<Fichier> fichiers = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, userId);
            try (ResultSet result = stmt.executeQuery())
            {
                while (result.next())
                    fichiers.add(mapRow(result));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findByProprietaire: " + e.getMessage(), e);
        }
        return fichiers;
    }

    public void updateDroits(int id, Droits droits)
    {
        String sqlQuery = "update fichiers set read_prop=?, write_prop=?, delete_prop=?, read_other=?, write_other=?, delete_other=? where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setBoolean(1, droits.isReadProp());
            stmt.setBoolean(2, droits.isWriteProp());
            stmt.setBoolean(3, droits.isDeleteProp());
            stmt.setBoolean(4, droits.isReadOther());
            stmt.setBoolean(5, droits.isWriteOther());
            stmt.setBoolean(6, droits.isDeleteOther());
            stmt.setInt(7, id);
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
        String sqlQuery = "delete from fichiers where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in delete: " + e.getMessage(), e);
        }
    }
}