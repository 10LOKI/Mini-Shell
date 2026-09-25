package ma.youcode.lineperm.dao;

import java.sql.*;
import ma.youcode.lineperm.model.User;
public class UserDao extends AbstractDao<User>
{
    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Delete operation is not supported by UserDao.");
    }

    @Override
    public User findById(int id) {
        throw new UnsupportedOperationException("FindById operation is not supported by UserDao.");
    }
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
            throw new RuntimeException("Database error in save: " + e.getMessage(), e);
        }
    }
}
