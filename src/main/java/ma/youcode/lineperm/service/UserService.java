package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.User;
import org.mindrot.jbcrypt.BCrypt;
import ma.youcode.lineperm.dao.UserDao;


public class UserService
{

    private User currentUser;
    private final UserDao userdao = new UserDao();

    public User     getUserById(int id)
    {
        User user = userdao.findById(id);
        if (user == null)
        {
            System.out.println("acun utilisateur avec ce Id ");
        }
        return (user);
    }

    public User     getCurrentUser()
    {
        return (currentUser);
    }
    public String signup (String login , String password)
    {
        if (login == null || login.trim().isEmpty() || login.length() < 3 || login.contains(" ") || login.contains(":"))
        {
            return "Login invalide.";
        }
        if (password == null || password.trim().isEmpty())
        {
            return "Invalide password";
        }
        if (currentUser != null)
        {
            return "deja user connectez";
        }
        if (userdao.findByLogin(login) != null)
        {
            return "Ce login existe deja";
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(login, hash);
        try
        {
            userdao.save(user);
        }
        catch (IllegalArgumentException e)
        {
            return (e.getMessage());
        }

        return "Compte cree par succes";
    }


    public String login (String login , String password)
    {
        if (currentUser != null)
        {
            return "deja user connectez";
        }
        if (login == null || login.trim().isEmpty() || login.contains(" ") || login.contains(":"))
        {
            return "Login ou password invalide.";
        }
        if (password == null || password.trim().isEmpty())
        {
            return "Login ou password invalide.";
        }
        User user = userdao.findByLogin(login);
        if (user == null)
        {
            return "login ou password incorrect";
        }
        boolean motDePasseCorrect = BCrypt.checkpw(password, user.getPasswordHash());
        if (!motDePasseCorrect)
        {
            return "login ou mot de pass incorrect";
        }

        currentUser = user;
        return "Bienvenue " + user.getLogin();
    }

    public String logout ()
    {
        if (currentUser == null)
        {
            return "no user is connected";
        }
        currentUser = null;
        return "disconnected";
    }

    public boolean estConnecte()
    {
        return currentUser != null;
    }

}