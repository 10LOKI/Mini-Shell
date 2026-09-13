package ma.youcode.lineperm.service;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.User;

public class    FichierService
{
    private List <Fichier> fichiers;
    public  FichierService()
    {
        this.fichiers = new ArrayList<>();
        charger();
    }
    private boolean fichierExiste(String nom)
    {
        int i = 0;
        while (i < fichiers.size())
        {
            if (fichiers.get(i).getNom().equals(nom))
                return (true);
            i++;
        }
        return (false);
    }

    private boolean nomValide(String nom)
    {
        int i = 0;
        while (i < nom.length())
        {
            char c = nom.charAt(i);
            if (!((c >= 97 && c <= 122) || (c >= 48 && c <= 57) || (c >= 65 && c <= 90) || c == 95 || c == 45 || c == 46))
                return (false);
            i++;
        }
        return (true);
    }
    private boolean creerFichierSurDisque(String nom)
    {
        try
        {
            Path chemin = Path.of("data").resolve(nom);
            Files.createDirectories(Path.of("data"));
            Files.createFile(chemin);
            return (true);
        }
        catch (IOException e)
        {
            System.err.println("Erreur lors de la creation du fichier: " + e.getMessage());
            return (false);
        }
    }
    public void lister()
    {
        int i;

        i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            String droits = "";

            droits += fichier.isReadProp()   ? "r" : "-";
            droits += fichier.isWriteProp()  ? "w" : "-";
            droits += fichier.isDeleteProp() ? "d" : "-";
            droits += "|";
            droits += fichier.isReadOther()   ? "r" : "-";
            droits += fichier.isWriteOther()  ? "w" : "-";
            droits += fichier.isDeleteOther() ? "d" : "-";

            System.out.println(droits + fichier.getProprietaire() + "  " + fichier.getNom());

            i++;
        }
    }

    public Fichier creer(String nom, User currentUser)
    {
        if (fichierExiste(nom))
        {
            System.out.println("Le fichier deja existe");
            return (null);
        }
        if (!nomValide(nom))
        {
            System.out.println("nom de fichier invalid");
            return (null);
        }
        Fichier fichier = new Fichier(nom, currentUser.getLogin());
        fichiers.add(fichier);
        if (!creerFichierSurDisque(nom))
        {
            fichiers.remove(fichier);
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
            if (fichier.getNom().equals(nom))
            {
                if (!fichier.estAutorisee(currentUser.getLogin(), 'r'))
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
    public boolean ecrire(String nom, String contenu, User currentUser)
    {
        int i;
        i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.getNom().equals(nom))
            {
                if (!fichier.estAutorisee(currentUser.getLogin(), 'w'))
                {
                    System.out.println("permission denied");
                    return (false);
                }
                try
                {
                    Path chemin = Path.of("data").resolve(nom);
                    Files.writeString(chemin, contenu);
                    return (true);
                }
                catch (IOException e)
                {
                    System.err.println("Cannot write to the file " + e);
                    return (false);
                }
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (false);
    }
    public boolean changer_perm(String nom, String cible, char droit, boolean valeur, User currentUser)
    {
        int i;
        i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.getNom().equals(nom))
            {
                if (!fichier.getProprietaire().equals(currentUser.getLogin()))
                {
                    System.out.println("permission denied");
                    return (false);
                }
                if (droit == 'r')
                {
                    if (cible.equals("prop"))        fichier.setReadProp(valeur);
                    else if (cible.equals("other"))  fichier.setReadOther(valeur);
                    else { System.out.println("cible invalid"); return (false); }
                }
                else if (droit == 'w')
                {
                    if (cible.equals("prop"))        fichier.setWriteProp(valeur);
                    else if (cible.equals("other"))  fichier.setWriteOther(valeur);
                    else { System.out.println("cible invalid"); return (false); }
                }
                else if (droit == 'd')
                {
                    if (cible.equals("prop"))        fichier.setDeleteProp(valeur);
                    else if (cible.equals("other"))  fichier.setDeleteOther(valeur);
                    else { System.out.println("cible invalid"); return (false); }
                }
                else
                {
                    System.out.println("droit invalid");
                    return (false);
                }
                return (true);
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (false);
    }

    public boolean supprimer(String nom, User currentUser)
    {
        int i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.getNom().equals(nom))
            {
                if (!fichier.estAutorisee(currentUser.getLogin(), 'd'))
                {
                    System.out.println("permission denied");
                    return (false);
                }
                try
                {
                    Files.deleteIfExists(Path.of("data").resolve(nom));
                }
                catch (IOException e)
                {
                    System.err.println("Erreur lors de la suppression: " + e.getMessage());
                    return (false);
                }
                fichiers.remove(i);
                return (true);
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (false);
    }

    public void sauvegarder()
    {
        try
        {
            Path chemin = Path.of("data/fichiers.db");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < fichiers.size())
            {
                Fichier f = fichiers.get(i);
                sb.append(f.getNom()).append(":")
                .append(f.getProprietaire()).append(":")
                .append(f.isReadProp()).append(":")
                .append(f.isWriteProp()).append(":")
                .append(f.isDeleteProp()).append(":")
                .append(f.isReadOther()).append(":")
                .append(f.isWriteOther()).append(":")
                .append(f.isDeleteOther()).append("\n");
                i++;
            }
            Files.writeString(chemin, sb.toString());
        }
        catch (IOException e)
        {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    public void charger()
    {
        Path chemin = Path.of("data/fichiers.db");
        if (!Files.exists(chemin))
            return;
        try
        {
            List<String> lignes = Files.readAllLines(chemin);
            int i = 0;
            while (i < lignes.size())
            {
                String ligne = lignes.get(i);
                if (!ligne.trim().isEmpty())
                {
                    String[] parts = ligne.split(":");
                    Fichier f = new Fichier(
                        parts[0], parts[1],
                        Boolean.parseBoolean(parts[2]),
                        Boolean.parseBoolean(parts[3]),
                        Boolean.parseBoolean(parts[4]),
                        Boolean.parseBoolean(parts[5]),
                        Boolean.parseBoolean(parts[6]),
                        Boolean.parseBoolean(parts[7])
                    );
                    fichiers.add(f);
                }
                i++;
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur lors du chargement: " + e.getMessage());
        }
    }
}