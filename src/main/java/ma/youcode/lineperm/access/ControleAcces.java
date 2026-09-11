package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.Fichier;
public class    ControleAcces
{
    public static boolean   estAutorise(String user , Fichier fichier , char droit)
    {
        if (user.equals(fichier.get_proprietaire()))
        {
            if (droit == 'r')
            return (fichier.isRead_prop());
            if (droit == 'w')
            return (fichier.iswrite_prop());
            if (droit == 'd')
            return (fichier.isDelete_prop());
        }
        else
        {
            if (droit == 'r')
            return (fichier.isRead_other());
            if (droit == 'w')
            return (fichier.isWrite_other());
            if (droit == 'd')
            return (fichier.isDelete_other());
        }
        return (false);
    }
}