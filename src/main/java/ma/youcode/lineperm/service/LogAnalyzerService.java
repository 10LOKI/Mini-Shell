package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.AccesLog;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class        LogAnalyzerService
{
    private list<AccesLog>  logs;

    public LogAnalyzerService(String cheminFichier)
    {
        this.logs = new ArrayList<>();
        chargerLogs(cheminFichier);
    }

    

    public long                 nbrActions()
    {

    }
    public long                 nbrRefus()
    {

    }
    public List<String>         utilisateurDistinct()
    {

    }
    public Map<String, Long>    utilisateurAction()
    {

    }
    public List<Map.Entry<String, Long>>    topFichiers()
    {

    }
    public List<AccesLog>       accesRefus()
    {

    }
    public Optional<Map.Entry<String, Long>>    plusActif()
    {

    }
    public Map<String, Long>    actionsType()
    {

    }
}