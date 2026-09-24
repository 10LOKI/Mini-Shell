package ma.youcode.lineperm.dao;

import ma.youcode.lineperm.model.User;
import java.sql.*;
public class UserDao extends AbstractDao<User>
{
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
        }
        catch (SQLException e)
        {
            System.out.println("Database error : " + e.getMessage());
        }
    }
}
