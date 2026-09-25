import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ma.youcode.lineperm.model.User;

public class loki {
        @Override
    public User    findById(int id)
    {
        String sqlQuery = "select * from users where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setInt(1, id);
            try (ResultSet result = stmt.executeQuery())
            {
                if (result.next())
                {
                    User user = new User(result.getInt("id") ,result.getString("login"), result.getString("password"));
                    // System.out.println("he's found");
                    return (user);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Database error in findById : " + e.getMessage(), e);
        }
        return null;
    }


        
    @Override
    public void     delete(int id)
    {
        String sqlQuery = "delete * from users where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery)) 
        {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) 
        {
            throw new RuntimeException("Database error in delete for ID " + id + ": " + e.getMessage(), e);
        }
    }


}
