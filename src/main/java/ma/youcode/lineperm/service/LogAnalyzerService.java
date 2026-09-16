package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.AccesLog;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class        LogAnalyzerService
{
    private List<AccesLog>  logs;

    public LogAnalyzerService(String cheminFichier)
    {
        this.logs = new ArrayList<>();
        chargerLogs(cheminFichier);
    }

    private void    chargerLogs(String cheminFichier)
    {
        try
        {
            List<String> lignes = Files.readAllLines(Path.of(cheminFichier));
            int i;
            i = 0;
            while(i < lignes.size())
            {
                String[] champs = lignes.get(i).split(";");
                if (champs.length == 6)
                {
                    logs.add(new AccesLog(champs[0], champs[1], champs[2], champs[3], champs[4], champs[5]));
                }
                i ++;
            }
        }
        catch (IOException e)
        {
            System.out.printl("Impossible de charger le fichier de logs :" + e.getMessage());
        }
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