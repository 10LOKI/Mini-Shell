package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.Fichier;

public class ControleAcces
{
    public static boolean estAutorise(String user, Fichier fichier, char droit)
    {
        if (user.equals(fichier.getProprietaire()))
        {
            if (droit == 'r') return (fichier.isReadProp());
            if (droit == 'w') return (fichier.isWriteProp());
            if (droit == 'd') return (fichier.isDeleteProp());
        }
        else
        {
            if (droit == 'r') return (fichier.isReadOther());
            if (droit == 'w') return (fichier.isWriteOther());
            if (droit == 'd') return (fichier.isDeleteOther());
        }
        return (false);
    }
}
