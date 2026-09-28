package ma.youcode.lineperm.service;

import ma.youcode.lineperm.dao.LogDao;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import ma.youcode.lineperm.model.AccesLog;

public class LogAnalyzerService
{
    private final LogDao logDao = new LogDao();

    public long nbrActions()
    {
        return logDao.compterTotal();
    }

    public long nbrRefus()
    {
        return logDao.compterRefuses();
    }

    public List<String> utilisateurDistinct()
    {
        return logDao.findAll().stream().map(AccesLog::getUtilisateur).distinct().collect(Collectors.toList());
    }

    public Map<String, Long> utilisateurAction()
    {
        return logDao.findAll().stream().collect(Collectors.groupingBy(AccesLog::getUtilisateur, Collectors.counting()));
    }

    public List<Map.Entry<String, Long>> topFichiers()
    {
        return logDao.findAll().stream()
            .collect(Collectors.groupingBy(AccesLog::getFichier, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(3)
            .collect(Collectors.toList());
    }

    public List<AccesLog> accesRefus(String utilisateur)
    {
        return logDao.findAll().stream()
            .filter(l -> l.getUtilisateur().equals(utilisateur) && l.getResultat().equals("REFUSE"))
            .collect(Collectors.toList());
    }

    public Optional<Map.Entry<String, Long>> plusActif()
    {
        return logDao.findAll().stream()
            .collect(Collectors.groupingBy(AccesLog::getUtilisateur, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue());
    }

    public Map<String, Long> actionsType()
    {
        return logDao.findAll().stream().collect(Collectors.groupingBy(AccesLog::getAction, Collectors.counting()));
    }
}
