package ma.youcode.lineperm;

import ma.youcode.lineperm.service.LogAnalyzerService;

public class Main
{
    public static void main(String[] args)
    {
        LogAnalyzerService analyzer = new LogAnalyzerService("src/main/resources/acces.log");

        // System.out.println("Total actions: " + analyzer.nbrActions());
        // System.out.println("Total de refus : " + analyzer.nbrRefus());
        // System.out.println("utilisateur distincts : " + analyzer.utilisateurDistinct());
        // System.out.println("Total d'actions : " + analyzer.utilisateurAction());
        System.out.println("topFichiers : " + analyzer.topFichiers());
        System.out.println("topFichiers : " + analyzer.accesRefus("Ayoub"));
        System.out.println("topFichiers : " + analyzer.plusActif());
        System.out.println("topFichiers : " + analyzer.actionsType());
    }
}