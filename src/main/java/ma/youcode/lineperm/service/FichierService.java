package ma.youcode.lineperm.service;

import java.util.List;
import java.util.ArrayList;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.User;

public class    FichierService
{
    private List <Fichier> fichiers;
    public  FichierService()
    {
        this.fichiers = new ArrayList<>();
    }

    public Fichier creer(String nom , User currentUser)
    {
        return null;
    }
    public void lister()
    {
        int i;

        i = 0;
        while (i < fichiers.size())
        {
            Fichier fichier = fichier.get(i);
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

            if (fichier.isRead_other)
            {
                droits += "r";
            }
            else
            {
                droits += "-";
            }
            if (fichier.isWrite_other)
            {
                droits += "w";
            }
            else
            {
                droits += "-";
            }
            if (fichier.isDelete_other)
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
    public String lire()
    {

    }
    public boolean ecrire()
    {

    }
    public boolean changer_perm()
    {

    }
    public void sauvegarder()
    {

    }
}