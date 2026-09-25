package ma.youcode.lineperm.model;

public class User
{
    private final int       id;
    private final String    login;
    private final String    passwordHash;

    public User(String login, String passwordHash) 
    {
        this(0, login, passwordHash);
    }

    public User(int id, String login, String passwordHash) 
    {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public int      getId()
    {
        return (id);
    }
    
    public String getLogin()
    {
        return (login);
    }

    public String getPasswordHash()
    {
        return (passwordHash);
    }

    @Override
    public String toString()
    {
        return "User{login = '" + login + "'}";
    }
}
