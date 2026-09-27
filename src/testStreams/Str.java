package testStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import ma.youcode.lineperm.model.AccesLog;

public class Str {

    private List<AccesLog> logs = new ArrayList<>();

    public Str(String cheminFichier) {
        try {
            List<String> lignes = Files.readAllLines(Path.of(cheminFichier));
            for (String ligne : lignes) {
                String[] champs = ligne.split(";");
                if (champs.length == 6) {
                    logs.add(new AccesLog(champs[0], champs[1], champs[2], champs[3], champs[4], champs[5]));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de chargement du fichier : " + e.getMessage());
        }
    }

    // public List<String> utilisateurs()
    // {
    //     return logs.stream().map(l -> l.getUtilisateur()).distinct().toList();
    // }
    
    public Map<String, Long> actionsParUtilisateurs()
    {
        return logs.stream().map(l -> l.getUtilisateur());
    }









    // 1. Compter le nombre total de logs
    public long nombreLogs() {
        // TODO : votre stream ici
        return 0;
    }

    // 2. Compter le nombre d'accès refusés (resultat == "REFUSE")
    public long nbrRefus() {
        // TODO : votre stream ici
        return 0;
    }

    // 3. Obtenir la liste des utilisateurs sans doublons
    public List<String> utilisateursUniques() {
        // TODO : votre stream ici
        return Collections.emptyList();
    }

    // 4. Compter le nombre d'actions par utilisateur (Map<Utilisateur, Nombre>)
    public Map<String, Long> actionsParUtilisateur() {
        // TODO : votre stream ici
        return Collections.emptyMap();
    }

    // 5. Obtenir le Top 3 des fichiers les plus demandés
    public List<Map.Entry<String, Long>> topFichiers() {
        // TODO : votre stream ici
        return Collections.emptyList();
    }

    // --- METHODE MAIN POUR VOS TESTS ---
    public static void main(String[] args) {
        Str exercice = new Str("src/main/resources/acces.log");

        System.out.println("1. Nombre total : " + exercice.nombreLogs());
        System.out.println("2. Nombre de refus : " + exercice.nbrRefus());
        System.out.println("3. Utilisateurs uniques : " + exercice.utilisateursUniques());
        System.out.println("4. Actions par utilisateur : " + exercice.actionsParUtilisateur());
        System.out.println("5. Top 3 des fichiers : " + exercice.topFichiers());
    }
}