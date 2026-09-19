package ma.youcode.lineperm;

import ma.youcode.lineperm.service.LogAnalyzerService;

public class Main
{
    public static void main(String[] args)
    {
        LogAnalyzerService analyzer = new LogAnalyzerService("src/main/resources/acces.log");

        // System.out.println("Total actions: " + analyzer.nbrActions());
        System.out.println("Total de refus : " + analyzer.nbrRefus());
        
    }
}