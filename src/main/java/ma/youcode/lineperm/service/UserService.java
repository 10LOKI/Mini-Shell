package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.User;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
}