package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.User;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserService
{
    private Map<String,User> users;
    private User currentUser;
    
    private static final String USERS_FILE = "data/users.db";

    public UserService()
    {
        this.users = new HashMap<>();
        this.currentUser = null;
        charger(USERS_FILE);
    }
    public User   getCurrentUser()
    {
        return (currentUser);
    }
    public void charger(String filePath)
    {
        Path path = Path.of(filePath);

        if (!Files.exists(path))
        {
            return;
        }

        try
        {
            List<String> lignes = Files.readAllLines(path);
            int i = 0;

            while (i < lignes.size())
            {
                String ligne = lignes.get(i);

                if (!ligne.trim().isEmpty())
                {
                    String[] parts = ligne.split(":", 2);
                    User user = new User(parts[0], parts[1]);
                    users.put(parts[0], user);
                }
                i++;
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur de chargement");
        }
    }
    public String signup (String login , String password)
    {
        if (login == null || login.trim().isEmpty() || login.contains(" ") || login.contains(":"))
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
        if (users.containsKey(login))
        {
            return "Ce login existe deja";
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(login, hash);
        users.put(login, user);
        sauvegarder();
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
        User user = users.get(login);
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

    public void sauvegarder()
    {
        try
        {
            Files.createDirectories(Path.of("data"));
            StringBuilder sb = new StringBuilder();
            for (User u : users.values())
                sb.append(u.getLogin()).append(":").append(u.getPasswordHash()).append("\n");
            Files.writeString(Path.of(USERS_FILE), sb.toString());
        }
        catch (IOException e)
        {
            System.err.println("Erreur de sauvegarde users: " + e.getMessage());
        }
    }
}