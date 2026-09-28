package ma.youcode.lineperm.service;

import java.time.LocalDateTime;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.dao.LogDao;
import ma.youcode.lineperm.model.AccesLog;
import ma.youcode.lineperm.model.Fichier;
import ma.youcode.lineperm.model.User;

public class LogService
{
    private final LogDao logDao = new LogDao();
    private final FichierDao fichierDao = new FichierDao();

    public void enregistrer(User user, String nom, String action, String resultat)
    {
        Fichier fichier = fichierDao.findByNom(nom);
        logDao.save(new AccesLog(0, user.getId(), fichier == null ? 0 : fichier.getId(),
            user.getLogin(), nom, action, resultat, LocalDateTime.now()));
    }
}
