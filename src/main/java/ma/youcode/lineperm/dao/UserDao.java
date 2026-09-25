package ma.youcode.lineperm.dao;

import java.sql.*;
import ma.youcode.lineperm.model.User;
public class UserDao extends AbstractDao<User>
{
    public User findByLogin(String login) 
    {
        String sqlQuery = "select * from users where login = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery)) 
        {
            stmt.setString(1, login);
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
            throw new RuntimeException("Database error in findByLogin: " + e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public void     delete(int id)
    {
        String sqlQuery = "delete * from users where id = ?";
    }

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
    public void     save(User user)
    {
        String sqlQuery = "insert into users (login,password)  values (?,?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlQuery))
        {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPasswordHash());
            stmt.executeUpdate();
            // System.out.println("Testing my code");
        }
        catch (SQLException e)
        {
            System.out.println("Database error : " + e.getMessage());
        }
    }
}
