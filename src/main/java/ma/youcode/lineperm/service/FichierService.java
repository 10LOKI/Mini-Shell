package ma.youcode.lineperm.service;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.access.ControleAcces;

public class    FichierService
{
    private List <Fichier> fichiers;
    public  FichierService()
    {
        this.fichiers = new ArrayList<>();
        Fichier fichier = new Fichier("test.txt","ayoub");
        fichiers.add(fichier);
    }

    public void lister()
    {
        int i;

        i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            String droits = "";

            if (fichier.isRead_prop())
            {
                droits += "r";
            }
            else
            {
                droits += "-";
            }
            if (fichier.isWrite_prop())
            {
                droits += "w";
            }
            else
            {
                droits += "-";
            }
            if (fichier.isDelete_prop())
            {
                droits += "d";
            }
            else
            {
                droits += "-";
            }

            droits += "|";

            if (fichier.isRead_other())
            {
                droits += "r";
            }
            else
            {
                droits += "-";
            }
            if (fichier.isWrite_other())
            {
                droits += "w";
            }
            else
            {
                droits += "-";
            }
            if (fichier.isDelete_other())
            {
                droits += "d";
            }
            else
            {
                droits += "-";
            }

            System.out.println(droits + fichier.get_proprietaire() + "  " + fichier.get_nom());

            i++;
        }
    }

    public Fichier  creer(String nom, User currentUser)
    {
        int i;
        i = 0;

        while (i < fichiers.size())
        {
            if (fichiers.get(i).get_nom().equals(nom))
            {
                System.out.println("Le fichier deja existe");
                return (null);
            }
            i++;
        }
        i = 0;
        while (i < nom.length())
        {
            char c;
            c = nom.charAt(i);
            if (!((c >= 97 && c <= 122) || (c >= 48 && c <= 57) || (c >= 65 && c <= 90) || c == 95 || c == 45 || c == 46))
            {
                System.out.println("nom de fichier invalid");
                return (null);
            }
            i++;
        }
        Fichier fichier;
        fichier = new Fichier(nom , currentUser.getLogin());
        fichiers.add(fichier);
        try
        {
            Path chemin;
            chemin = Path.of("data").resolve(nom);

            Files.createDirectories(Path.of("data"));
            Files.createFile(chemin);
        }
        catch (IOException e)
        {
            System.out.println("Erreur lors de la creation du fichier");
            return (null);
        }
        return (fichier);
    }

    public String lire()
    {
        return "";
    }
    public String   lire(String nom , User currentUser)
    {
        int i;
        i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.get_nom().equals(nom))
            {
                if (!ControleAcces.estAutorise(currentUser.getLogin(),fichier , 'r'))
                {
                    System.out.println("permission denied");
                    return null;
                }
                try
                {
                    Path chemin = Path.of("data").resolve(nom);
                    return Files.readString(chemin);
                }
                catch(IOException e)
                {
                    System.err.println("Cannot read the file" + e);
                    return null;
                }
            }
            i++;
        }
        return null;
    }
    public boolean ecrire()
    {
        return true;
    }
    public boolean changer_perm()
    {
        return true;
    }
    public void sauvegarder()
    {

    }
}