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
    
    public UserService()
    {
        this.users = new HashMap<>();
        this.currentUser = null;
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
                    String login = parts[0];
                    String hash = parts[1];
                    User user = new User(login, hash);
                    users.put(login, user);
                }
                i++;
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur de chargement : " + e.getMessage());
        }
    }
    public String signup (String login , String password)
    {
        if (users.containsKey(login))
        {
            return "Ce login existe deja";
        }
        if (currentUser != null)
        {
            return "deja user connectez";
        }
        if (login == null || login.trim().isEmpty() || login.contains(" ") || login.contains(":"))
        {
            return "Login invalide.";
        }
        if (password == null || password.trim().isEmpty())
        {
            return "Invalide password";
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(login ,hash);
        users.put(login , user);

        return "Compte cree par succes";
    }


    public void login (String login , String password)
    {
        
    }


    public void logout ();
}