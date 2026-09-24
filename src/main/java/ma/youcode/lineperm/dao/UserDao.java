package ma.youcode.lineperm.dao;

import ma.youcode.lineperm.model.User;
import java.sql.*;
public class UserDao extends AbstractDao<User>
{
    public User     findByLogin(String login)
    {
        String  sqlQuery = "select * from users where login = ?";
    }
    @Override
    public void     delete(int id)
    {

    }

    @Override
    public User    findById(int id)
    {
        return (User);
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
            System.out.println("Testing my code");
        }
        catch (SQLException e)
        {
            System.out.println("Database error : " + e.getMessage());
        }
    }
}
