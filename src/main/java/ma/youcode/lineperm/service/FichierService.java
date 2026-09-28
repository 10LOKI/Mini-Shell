package ma.youcode.lineperm.service;

import java.util.List;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.access.ControleAcces;

public class FichierService
{
    private final FichierDao fichierDao = new FichierDao();
    private final Path data = Path.of(System.getProperty("lineperm.data", "data"));

    private boolean fichierExiste(String nom)
    {
        return fichierDao.findByNom(nom) != null;
    }
    private boolean nomValide(String nom)
    {
        if (nom == null || nom.isBlank() || nom.equals(".") || nom.equals("..") || nom.equalsIgnoreCase("users.db") || nom.equalsIgnoreCase("fichiers.db")) return false;
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
            Path chemin = data.resolve(nom);
            Files.createDirectories(data);
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
        int i = 0;
        List<Fichier> fichiers = fichierDao.findAll();
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            String droits = "";
            droits += fichier.isReadProp()    ? "r" : "-";
            droits += fichier.isWriteProp()   ? "w" : "-";
            droits += fichier.isDeleteProp()  ? "d" : "-";
            droits += "|";
            droits += fichier.isReadOther()   ? "r" : "-";
            droits += fichier.isWriteOther()  ? "w" : "-";
            droits += fichier.isDeleteOther() ? "d" : "-";
            System.out.println(droits + " " + fichier.getProprietaire() + "  " + fichier.getNom());
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

        if (!creerFichierSurDisque(nom))
        {

            return (null);
        }
        try
        {
            fichierDao.save(fichier);
        }
        catch (RuntimeException e)
        {
            try { Files.deleteIfExists(data.resolve(nom)); }
            catch (IOException cleanup) { e.addSuppressed(cleanup); }
            throw e;
        }
        return fichierDao.findByNom(nom);
    }

    public String lire(String nom, User currentUser)
    {
        int i = 0;
        List<Fichier> fichiers = fichierDao.findAll();
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.getNom().equals(nom))
            {
                if (!ControleAcces.estAutorise(currentUser.getLogin(), fichier, 'r'))
                {
                    System.out.println("permission denied");
                    return (null);
                }
                try
                {
                    Path chemin = data.resolve(nom);
                    return Files.readString(chemin);
                }
                catch (IOException e)
                {
                    System.err.println("Cannot read the file: " + e.getMessage());
                    return (null);
                }
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (null);
    }

    public boolean ecrire(String nom, String contenu, User currentUser)
    {
        int i = 0;
        List<Fichier> fichiers = fichierDao.findAll();
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.getNom().equals(nom))
            {
                if (!ControleAcces.estAutorise(currentUser.getLogin(), fichier, 'w'))
                {
                    System.out.println("permission denied");
                    return (false);
                }
                try
                {
                    Path chemin = data.resolve(nom);
                    Files.writeString(chemin, contenu);
                    return (true);
                }
                catch (IOException e)
                {
                    System.err.println("Cannot write to the file: " + e.getMessage());
                    return (false);
                }
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (false);
    }

    public boolean supprimer(String nom, User currentUser)
    {
        int i = 0;
        List<Fichier> fichiers = fichierDao.findAll();
        while (i < fichiers.size())
        {
            Fichier fichier = fichiers.get(i);
            if (fichier.getNom().equals(nom))
            {
                if (!ControleAcces.estAutorise(currentUser.getLogin(), fichier, 'd'))
                {
                    System.out.println("permission denied");
                    return (false);
                }
                try
                {
                    Connection connection = DBConnection.getInstance().getConnection();
                    connection.setAutoCommit(false);
                    try
                    {
                        fichierDao.delete(fichier.getId());
                        Files.deleteIfExists(data.resolve(nom));
                        connection.commit();
                    }
                    catch (IOException | SQLException | RuntimeException e)
                    {
                        connection.rollback();
                        throw e;
                    }
                    finally
                    {
                        connection.setAutoCommit(true);
                    }
                }
                catch (IOException | SQLException e)
                {
                    System.err.println("Erreur lors de la suppression: " + e.getMessage());
                    return (false);
                }

                return (true);
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (false);
    }

    public boolean changerPerm(String nom, String cible, char droit, boolean valeur, User currentUser)
    {
        int i = 0;
        List<Fichier> fichiers = fichierDao.findAll();
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
                    if (cible.equals("prop"))       fichier.setReadProp(valeur);
                    else if (cible.equals("other")) fichier.setReadOther(valeur);
                    else { System.out.println("cible invalid"); return (false); }
                }
                else if (droit == 'w')
                {
                    if (cible.equals("prop"))       fichier.setWriteProp(valeur);
                    else if (cible.equals("other")) fichier.setWriteOther(valeur);
                    else { System.out.println("cible invalid"); return (false); }
                }
                else if (droit == 'd')
                {
                    if (cible.equals("prop"))       fichier.setDeleteProp(valeur);
                    else if (cible.equals("other")) fichier.setDeleteOther(valeur);
                    else { System.out.println("cible invalid"); return (false); }
                }
                else
                {
                    System.out.println("droit invalid");
                    return (false);
                }
                fichierDao.updateDroits(fichier.getId(), fichier);
                return (true);
            }
            i++;
        }
        System.out.println("Fichier introuvable");
        return (false);
    }

}
